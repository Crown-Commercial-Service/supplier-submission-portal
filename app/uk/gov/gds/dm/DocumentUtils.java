package uk.gov.gds.dm;

import models.Document;
import org.apache.commons.io.FilenameUtils;
import play.data.Upload;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class DocumentUtils {

    public static final int MAX_FILE_SIZE = 5400000;

    public static boolean validateDocumentFormat(Upload file){
        String fileType = FilenameUtils.getExtension(file.getFileName().toLowerCase());
        return (fileType.equals("pdf") || fileType.equals("odf") || fileType.equals("pda"));
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

    public static String s3Filename(Long serviceId, String questionId, String filename) {
        String fileType = FilenameUtils.getExtension(filename.toLowerCase());

        for(questionPageS3Filename x : questionPageS3Filename.values()){
            if(x.getQuestionId().equals(questionId)){
                return serviceId.toString() + "-" + x + "." + fileType;
            }
        }
        throw new IllegalArgumentException("That questionId (" + questionId + ") does not map to an S3 filename");
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
