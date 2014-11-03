package controllers;

import models.Listing;
import uk.gov.gds.dm.ListingUtils;

import java.util.List;
import java.util.Map;

public class Dashboard extends AuthenticatingController {

    public static void home() {
        Map<String, String> supplierDetails = supplierDetailsFromCookie;

        List<Listing> listings = Listing.all(Listing.class).filter("supplierId",
                getSupplierId()).order("-lastUpdated").fetch(250);
        List<Listing> completedListings = ListingUtils.getCompletedListings(listings);
        listings.removeAll(completedListings);  //Listings object now only contains draft services

        render(listings, completedListings, supplierDetails);
    }
}
