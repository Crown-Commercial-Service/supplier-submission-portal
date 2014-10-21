package controllers;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.files.*;
import models.Document;
import models.Listing;
import models.Page;
import org.apache.commons.io.FileUtils;
import play.Logger;
import play.data.Upload;
import play.data.validation.Error;
import play.i18n.Messages;
import uk.gov.gds.dm.DocumentUtils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.util.List;
import java.util.Map;

import static uk.gov.gds.dm.DocumentUtils.storeDocument;

public class Page6 extends AuthenticatingController {

    private static final Long PAGE_ID = 6l;
    private static final String QUESTION_ID = "p6q1";

    public static void savePage(Long listingId, Upload p6q1) {

        Listing listing = Listing.getByListingId(listingId);

        validation.required(p6q1).key("p6q1");

        if(p6q1 != null){
            if(!DocumentUtils.validateDocumentFormat(p6q1)){
                validation.addError("p6q1", Messages.getMessage("en", "validation.file.wrongFormat"));
            }
            if(!DocumentUtils.validateDocumentFileSize(p6q1)){
                validation.addError("p6q1", Messages.getMessage("en", "validation.file.tooLarge"));
            }
        }

try {
    // Get a file service
    FileService fileService = FileServiceFactory.getFileService();

// Create a new Blob file with mime-type "text/plain"
    AppEngineFile file = fileService.createNewBlobFile("application/pdf");
    Logger.info("CREATED FILE: " + file);
    String path = file.getFullPath();
    Logger.info("FILE FULL PATH: " + path);
// Write more to the file in a separate request
    file = new AppEngineFile(path);

// This time lock because we intend to finalize
    boolean lock = true;
    FileWriteChannel writeChannel = fileService.openWriteChannel(file, lock);
    Logger.info("Opened write channel...");
// This time we write to the channel directly
    writeChannel.write(ByteBuffer.wrap(p6q1.asBytes()));

// Now finalize
    writeChannel.closeFinally();
    Logger.info("File has been uploaded.");
    BlobKey blobKey = fileService.getBlobKey(file);
    Logger.info("BLOBKEY=" + blobKey);
} catch (Exception ex) {
    ex.printStackTrace();
}
        
//        try {
//            Document document = storeDocument(p6q1, getSupplierId(), listingId, QUESTION_ID);
//            document.insert();
//        } catch(Exception e) {
//            Logger.error(e, "Could not upload document to S3. Cause: %s", e.getMessage());
//            validation.addError("p6q1", Messages.getMessage("en", "validation.upload.failed"));
//        }

        if(validation.hasErrors()) {
            flash.put("body", params.get("body"));
            for(Map.Entry<String, List<Error>> entry : validation.errorsMap().entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue().get(0).message();

                flash.put(key, value);
            }
            redirect(String.format("/page/%d/%d", PAGE_ID, listing.id));
        }

        Page page = new Page(listingId, PAGE_ID);
        page.responses.put("p6q1", p6q1.getFileName());
        page.insert();
        listing.addResponsePage(page, PAGE_ID, supplierDetailsFromCookie.get("supplierEmail"));
        redirect(listing.nextPageUrl(PAGE_ID, listing.id));
    }

}
