package controllers;

import models.Listing;
import models.Page;
import play.mvc.Controller;

public class Page26 extends Controller {

    private static final Long PAGE_ID = 26l;

    public static void savePage(Long listingId, String p26q1) {

        Listing listing = Listing.getByListingId(listingId);
        
        // TODO: Validate all fields on this page requiring validation
//        validation.required(p26q1).message("p26q1:null");
        if(validation.hasErrors()) {
            flash.error("%s", validation.errors());
            redirect(String.format("/page/%d/%d", PAGE_ID, listing.id));
        }
        
        Page page = new Page(listingId, PAGE_ID);
        page.responses.put("p26q1", p26q1);
        page.insert();
        listing.addResponsePage(page, PAGE_ID);
        redirect(listing.nextPageUrl(PAGE_ID, listing.id));
    }
}
