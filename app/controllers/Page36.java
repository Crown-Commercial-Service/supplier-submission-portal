package controllers;

import models.Listing;
import models.Page;
import play.mvc.Controller;

public class Page36 extends Controller {

    private static final Long PAGE_ID = 36l;

    public static void savePage(Long listingId, String p36q1, String p36q2, String p36q3) {

        Listing listing = Listing.getByListingId(listingId);
        
        validation.required(p36q1).message("p36q1:null");
        if (!listing.lot.equals("SaaS")) {
            validation.required(p36q2).message("p36q2:null");
        }
        validation.required(p36q3).message("p36q2:null");
        if(validation.hasErrors()) {
            flash.error("%s", validation.errors());
            redirect(String.format("/page/%d/%d", PAGE_ID, listing.id));
        }
        
        Page page = new Page(listingId, PAGE_ID);
        page.responses.put("p36q1", p36q1);
        page.responses.put("p36q2", p36q2);
        page.responses.put("p36q3", p36q3);
        page.insert();
        listing.addResponsePage(page, PAGE_ID);
        redirect(listing.nextPageUrl(PAGE_ID, listing.id));
    }

    public static void savePage(Long listingId, String p36q1, String p36q3) {
        savePage(listingId, p36q1, null, p36q3);
    }
}
