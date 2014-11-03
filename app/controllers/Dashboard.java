package controllers;

import models.Listing;
import uk.gov.gds.dm.ListingUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dashboard extends AuthenticatingController {

    public static void home() {

        List<Listing> listings = Listing.all(Listing.class).filter("supplierId",
                getSupplierId()).order("-lastUpdated").fetch(250);
        List<Listing> completedListings = ListingUtils.getCompletedListings(listings);
        listings.removeAll(completedListings);  //Listings object now only contains draft services

        Map<String,String> supplierDetails = new HashMap<String,String>();
        supplierDetails.put("supplierCompanyName", getSupplierName());
        supplierDetails.put("supplierId", getSupplierId());
        supplierDetails.put("esourcingId", getSupplierEsourcingId());
        
        render(listings, completedListings, supplierDetails);
    }
}
