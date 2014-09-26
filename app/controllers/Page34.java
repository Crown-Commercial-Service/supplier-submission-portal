package controllers;

import models.Listing;
import models.Page;
import play.mvc.Controller;

public class Page34 extends Controller {

    private static final Long PAGE_ID = 34l;

    public static void savePage(Long listingId, String p34q1) {

        Listing listing = Listing.getByListingId(listingId);

        // TODO: Validate all fields on this page requiring validation
//        validation.required(p34q1).message("p34q1:null");
        if(validation.hasErrors()) {
            flash.error("%s", validation.errors());
            redirect(String.format("/page/%d/%d", PAGE_ID, listing.id));
        }
        
        Page page = new Page(listingId, PAGE_ID);
        page.responses.put("p34q1", p34q1);
        page.insert();
        listing.addResponsePage(page, PAGE_ID);
        redirect(listing.nextPageUrl(PAGE_ID, listing.id));
    }
}
