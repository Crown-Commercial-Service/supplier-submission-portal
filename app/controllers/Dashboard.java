package controllers;

import models.Listing;
import play.mvc.Controller;
import siena.*;
import uk.gov.gds.dm.DevUtils;

import java.util.List;

public class Dashboard extends Controller {

    public static void home() {
        String supplierId = DevUtils.randomSupplierId();
        List<Listing> listings = Listing.allBySupplierId(supplierId);
        render(listings, supplierId);
    }
}
