package controllers;

import models.Listing;
import play.Logger;
import play.Play;
import play.mvc.Controller;
import uk.gov.gds.dm.DocumentUtils;
import uk.gov.gds.dm.ListingUtils;
import uk.gov.gds.dm.S3Uploader;

import java.util.List;

public class CompletedListingExporter extends Controller {

    private static final S3Uploader uploader = new S3Uploader(String.valueOf(Play.configuration.get("s3.export.bucket.name")));
    
    public static void exportCompletedListingsAsJson() {
        
        List<Listing> listings = Listing.all(Listing.class).order("supplierId").fetch();
        List<Listing> completedListings = ListingUtils.getCompletedListings(listings);
        String dateString = DocumentUtils.dateString();
        for (Listing l: completedListings) {
            Logger.info("Exporting listing: " + l.id + " (" + l.title + ")");
            String documentKey = String.format("%s/%s/%s", dateString, l.supplierId, DocumentUtils.s3ExportFilename(l.id));
            String documentUrl = uploader.upload(l.toString().getBytes(), documentKey);
            Logger.info("  Listing " + l.id + " exported to: " + documentUrl);
        }
        redirect("/");
    }
}
