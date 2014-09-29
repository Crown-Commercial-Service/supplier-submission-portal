package controllers;

import models.Listing;
import play.mvc.Controller;
import uk.gov.gds.dm.DevUtils;

import java.util.List;

public class Dashboard extends Controller {

    public static void home() {
        String supplierId = DevUtils.randomSupplierId();
        List<Listing> listings = Listing.all(Listing.class).filter("supplierId", supplierId).fetch();
        render(listings, supplierId);
    }
}
