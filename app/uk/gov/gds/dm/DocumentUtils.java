package uk.gov.gds.dm;

import models.Document;
import org.apache.commons.io.FilenameUtils;
import play.data.Upload;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class DocumentUtils {

    public static final int MAX_FILE_SIZE = 5400000;
    private static final List<String> ALLOWED_FILE_EXTENSIONS = Arrays.asList(new String[]{ "pdf", "pda", "odt", "ods", "odp"});

    public static boolean validateDocumentFormat(Upload file){
        String fileType = FilenameUtils.getExtension(file.getFileName().toLowerCase());
        return (ALLOWED_FILE_EXTENSIONS.contains(fileType));
    }

    public static boolean validateDocumentFileSize(Upload file){
        return (file.getSize() <= MAX_FILE_SIZE);
    }

    public static Document storeDocument(Upload upload, String supplierName, long listingId, String questionId) {

        String documentName = upload.getFileName();
        try {
            documentName = URLEncoder.encode(documentName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // won't happen
        }
        Document document = Document
                .forListing(listingId)
                .withName(documentName)
                .forQuestion(questionId)
                .withBytes(upload.asBytes())
                .forSupplier(supplierName).build();

        document.pushDocumentToStorage();

        return document;
    }

    public static String s3DocumentFilename(Long serviceId, String questionId, String filename) {
        String fileType = FilenameUtils.getExtension(filename.toLowerCase());

        for(questionPageS3Filename x : questionPageS3Filename.values()){
            if(x.getQuestionId().equals(questionId)){
                return serviceId.toString() + "-" + x + "." + fileType;
            }
        }
        throw new IllegalArgumentException("That questionId (" + questionId + ") does not map to an S3 filename");
    }

    public static String s3ExportFilename(Long serviceId) {
        return "service-" + serviceId.toString() + ".json";
    }

    public static String dateString() {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }

    private enum questionPageS3Filename {
        P6Q1 ("p6q1", "service-definition-document"),
        P7Q3 ("p7q3", "terms-and-condtions"),
        P8Q6 ("p8q6", "pricing-document"),
        P8Q7 ("p8q7", "sfia-rate-card");

        private final String questionId;
        private final String fileNameRepresentation;

        questionPageS3Filename (String id, String string){
            this.questionId = id;
            this.fileNameRepresentation = string;
        }

        public String getQuestionId(){
            return questionId;
        }

        @Override
        public String toString() {
            return fileNameRepresentation;
        }
    }
}
