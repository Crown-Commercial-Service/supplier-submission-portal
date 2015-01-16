package controllers;

import com.google.appengine.api.datastore.*;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import models.Listing;
import play.Logger;
import play.Play;
import play.mvc.Controller;
import uk.gov.gds.dm.DocumentUtils;
import uk.gov.gds.dm.ListingToJSONConverter;
import uk.gov.gds.dm.S3Uploader;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withUrl;

public class ListingExporter extends Controller {

    private static final S3Uploader completedUploader = new S3Uploader(String.valueOf(Play.configuration.get("s3.completed.export.bucket.name")));
    private static final S3Uploader draftUploader = new S3Uploader(String.valueOf(Play.configuration.get("s3.draft.export.bucket.name")));

    private final static int EXPORT_PAGE_SIZE = 500;

    public static void exportCompletedListingsAsJson() {

        Queue queue = QueueFactory.getDefaultQueue();
        try {
            queue.add(withUrl("/cron/paginatedexport"));
        } catch (Exception ex) {
            Logger.error(ex, "Error adding export task to the queue");
        }
        ok();
    }

    public static void paginatedExport(String cursor) {
        Queue queue = QueueFactory.getDefaultQueue();
        String dateString = DocumentUtils.dateString();
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        FetchOptions fetchOptions = FetchOptions.Builder.withLimit(EXPORT_PAGE_SIZE);

        if (cursor != null) {
            fetchOptions.startCursor(Cursor.fromWebSafeString(cursor));
        }

        Query q = new Query("listing");
        PreparedQuery pq = datastore.prepare(q);
        QueryResultList<Entity> results = pq.asQueryResultList(fetchOptions);

        boolean thereWereResultsThisTime = false;
        for (Entity listing : results) {
            if ((boolean)listing.getProperty("serviceSubmitted")) {
                // It's a completed listing
                try {
                    queue.add(withUrl("/cron/exportcompleted").param("date", dateString).param("id", Long.toString(listing.getKey().getId())));
                } catch (Exception ex) {
                    Logger.error(ex, "Error adding a completed listing to the export queue");
                }
            } else {
                // It's a draft listing
                try {
                    queue.add(withUrl("/cron/exportdraft").param("date", dateString).param("id", Long.toString(listing.getKey().getId())));
                } catch (Exception ex) {
                    Logger.error(ex, "Error adding a draft listing to the export queue");
                }
            }
            thereWereResultsThisTime = true;
        }

        String cursorString = results.getCursor().toWebSafeString();

        if (thereWereResultsThisTime) {
            try {
                // We haven't got to the end yet - add a task to fetch the next page of listings
                queue.add(withUrl("/cron/paginatedexport").param("cursor", cursorString));
            } catch (Exception ex) {
                Logger.error(ex, "Error adding export task to the queue");
            }
        }
        ok();
    }

    public static void exportCompletedListing(String date, Long id) {
        exportListing(date, id, completedUploader);
    }

    public static void exportDraftListing(String date, Long id) {
        exportListing(date, id, draftUploader);
    }

    private static void exportListing(String date, Long id, S3Uploader uploader) {
        Listing listing = Listing.getByListingId(id);
        if(listing == null) {
            Logger.warn(String.format("Export listing: Invalid listing id [%d] provided", id));
            return;
        }
        String listingJSON = ListingToJSONConverter.convertToJson(listing);
        byte[] listingBytes;
        try {
            listingBytes = listingJSON.getBytes("UTF-8");
        } catch (java.io.UnsupportedEncodingException ex) {
            listingBytes = listingJSON.getBytes();
        }
        String documentKey = String.format("%s/%s/%s", date, listing.supplierId, DocumentUtils.s3ExportFilename(listing.id));
        String documentUrl = uploader.upload(listingBytes, documentKey);
        Logger.info(String.format("Uploaded listing to: %s", documentUrl));
    }
}
