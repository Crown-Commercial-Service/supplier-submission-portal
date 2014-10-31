package controllers;

import models.Listing;
import uk.gov.gds.dm.ListingUtils;
import play.Logger;

import java.util.List;
import java.util.Map;

public class Dashboard extends AuthenticatingController {

    public static void home() {
        Map<String, String> supplierDetails = supplierDetailsFromCookie;

        Long currentTime = System.currentTimeMillis();
        List<Listing> listings = Listing.all(Listing.class).filter("supplierId",
                getSupplierId()).order("-lastUpdated").fetch();
        Long queryTime = System.currentTimeMillis() - currentTime;
        List<Listing> completedListings = ListingUtils.getCompletedListings(listings);
        Long completedTime = System.currentTimeMillis() - (currentTime + queryTime);
        listings.removeAll(completedListings);  //Listings object now only contains draft services

        Logger.error(String.format("Dashboard timings: %d %d", queryTime, completedTime));
        render(listings, completedListings, supplierDetails);
    }
}
