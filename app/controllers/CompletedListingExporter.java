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

public class CompletedListingExporter extends Controller {

    private static final S3Uploader uploader = new S3Uploader(String.valueOf(Play.configuration.get("s3.export.bucket.name")));
    
    public static void exportCompletedListingsAsJson() {
        
        List<Listing> listings = Listing.all(Listing.class).order("supplierId").fetch();
        List<Listing> completedListings = ListingUtils.getCompletedListings(listings);
        Queue queue = QueueFactory.getDefaultQueue();
        String dateString = DocumentUtils.dateString();
        for (Listing l: completedListings) {
            Logger.info("Adding job to queue: " + l.id);
            queue.add(withUrl("/cron/exportone").param("date", dateString).param("id", l.id.toString()));
        }
        ok();
    }
    
    public static void exportOneListing(String date, Long id) {
        Listing listing = Listing.getByListingId(id);
        Logger.info("Exporting listing: " + listing.id + " (" + listing.title + ")");
        String documentKey = String.format("%s/%s/%s", date, listing.supplierId, DocumentUtils.s3ExportFilename(listing.id));
        String documentUrl = uploader.upload(listing.toString().getBytes(), documentKey);
        Logger.info("  Listing " + listing.id + " exported to: " + documentUrl);
    }
}
