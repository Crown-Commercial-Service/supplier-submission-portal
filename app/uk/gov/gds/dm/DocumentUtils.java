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

    public static String s3Filename(String questionId, String filename) {
        String fileType = FilenameUtils.getExtension(filename.toLowerCase());
        return questionId + "." + fileType;
    }
}
