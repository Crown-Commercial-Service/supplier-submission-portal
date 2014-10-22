package uk.gov.gds.dm;

import com.amazon.s3shell.S3Store;
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
        System.out.println(new String(upload.asBytes()));
        Document document = Document
                .forListing(listingId)
                .withName(documentName)
                .forQuestion(questionId)
                .withBytes(upload.asBytes())
                .forSupplier(supplierName).build();
//                .fromFile(upload.asFile()).build();

        document.pushDocumentToStorage();

        return document;
    }
}
