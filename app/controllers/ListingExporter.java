package controllers;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import models.Listing;
import play.Logger;
import play.Play;
import play.mvc.Controller;
import uk.gov.gds.dm.DocumentUtils;
import uk.gov.gds.dm.ListingUtils;
import uk.gov.gds.dm.S3Uploader;

import java.util.List;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withUrl;

public class ListingExporter extends Controller {

    private static final S3Uploader completedUploader = new S3Uploader(String.valueOf(Play.configuration.get("s3.completed.export.bucket.name")));
    private static final S3Uploader draftUploader = new S3Uploader(String.valueOf(Play.configuration.get("s3.draft.export.bucket.name")));
    
    public static void exportCompletedListingsAsJson() {
        
        List<Listing> listings = Listing.all(Listing.class).order("supplierId").fetch();
        List<Listing> completedListings = ListingUtils.getCompletedListings(listings);
        Queue queue = QueueFactory.getDefaultQueue();
        String dateString = DocumentUtils.dateString();
        Logger.info(String.format("Adding %s completed listings to export queue", completedListings.size()));
        for (Listing l: completedListings) {
            queue.add(withUrl("/cron/exportcompleted").param("date", dateString).param("id", l.id.toString()));
        }
        listings.removeAll(completedListings);
        // Listings now only contains drafts
        Logger.info(String.format("Adding %s draft listings to export queue", listings.size()));
        for (Listing l: listings) {
            queue.add(withUrl("/cron/exportdraft").param("date", dateString).param("id", l.id.toString()));
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
            badRequest();
        }
        String documentKey = String.format("%s/%s/%s", date, listing.supplierId, DocumentUtils.s3ExportFilename(listing.id));
        uploader.upload(listing.toString().getBytes(), documentKey);
    }
}
