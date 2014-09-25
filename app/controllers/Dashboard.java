package controllers;

import models.Listing;
import play.mvc.Controller;
import siena.*;
import uk.gov.gds.dm.DevUtils;

import java.util.List;

public class Dashboard extends Controller {

    public static void home() {
        System.out.println("TESTing properties on homepage: " + Fixtures.getContentProperties());


        String supplierId = DevUtils.randomSupplierId();
        System.out.println(" SUPPLIER: " + supplierId + " - " + Listing.allBySupplierId(supplierId));
        List<Listing> listings = Listing.allBySupplierId(supplierId);
        render(listings, supplierId);
    }
}
