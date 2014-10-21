package controllers;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.files.AppEngineFile;
import com.google.appengine.api.files.FileReadChannel;
import com.google.appengine.api.files.FileService;
import com.google.appengine.api.files.FileServiceFactory;
import org.apache.commons.io.FileUtils;
import play.Logger;
import play.mvc.Controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;

public class BlobDownloader extends Controller {
    
    public static void downloadBlob(String path) {
        path = "/blobstore/" + path;
        FileService fileService = FileServiceFactory.getFileService();
        AppEngineFile file = new AppEngineFile(path);
        BlobKey blobKey = fileService.getBlobKey(file);
        try {
            // Later, read from the file using the Files API
            boolean lock = false; // Let other people read at the same time
            FileReadChannel readChannel = fileService.openReadChannel(file, false);
            Logger.info("Opened read channel...");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
// Now read from the file using the Blobstore API
        BlobstoreService blobStoreService = BlobstoreServiceFactory.getBlobstoreService();
        // Start reading
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        long inxStart = 0;
        long inxEnd = 1024;
        boolean flag = false;

        do {
            try {
                Logger.info("Getting some bytes...");
                byte[] b = blobStoreService.fetchData(blobKey,inxStart,inxEnd);
                out.write(b);

                if (b.length < 1024)
                    flag = true;

                inxStart = inxEnd + 1;
                inxEnd += 1025;

            } catch (Exception e) {
                flag = true;
            }

        } while (!flag);
        Logger.info("FINISHED getting bytes.");
        byte[] filebytes = out.toByteArray();
        renderBinary(new ByteArrayInputStream(filebytes),"FROM_BLOBSTORE_FILENAME.pdf",new Long(filebytes.length));
        //FileUtils.writeByteArrayToFile(new File("/Users/kevinkeenoy/gdsworkspace/supplier-submission-portal/tmp/FROM_BLOB_STORE.pdf"), filebytes);

    }
}
