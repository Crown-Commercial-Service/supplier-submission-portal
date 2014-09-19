package uk.gov.gds.dm;

import models.Listing;

public class FixtureDataSetup {
    
    public static Long createListing(String lot) {
        Listing listing = new Listing("test-supplier", lot);
        listing.insert();
        return listing.id;
    }
    
    public static void deleteListing(Long id) {
        Listing listing = Listing.getByListingId(id);
        listing.delete();
    }
}
