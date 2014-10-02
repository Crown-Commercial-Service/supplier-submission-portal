package controllers;

import models.Listing;
import play.Logger;

import java.util.List;
import java.util.Map;

public class Dashboard extends AuthenticatingController {

    public static void home() {
        Map<String, String> supplierDetails = supplierDetailsFromCookie;
        List<Listing> listings = Listing.all(Listing.class).filter("supplierId", supplierDetails.get("supplierId")).fetch();
        Logger.info("System property is : " + System.getProperty("ssp.cookie.enc"));
        render(listings, supplierDetails);
    }
}
