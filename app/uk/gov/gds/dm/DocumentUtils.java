package uk.gov.gds.dm;

import models.Document;
import org.apache.commons.io.FilenameUtils;
import play.data.Upload;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

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
            documentName = URLEncoder.encode(documentName, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            // won't happen
        }
        Document document = Document
                .forListing(listingId)
                .withName(documentName)
                .forQuestion(questionId)
                .forSupplier(supplierName)
                .fromFile(upload.asFile()).build();

        document.pushDocumentToStorage();

        return document;
    }
}
