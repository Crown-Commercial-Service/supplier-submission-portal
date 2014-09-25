package controllers;

import models.Listing;
import models.Page;
import play.mvc.Controller;

public class Page35 extends Controller {

    private static final Long PAGE_ID = 35l;

    public static void savePage(Long listingId, String p35q1) {

        Listing listing = Listing.getByListingId(listingId);
        
        validation.required(p35q1).message("p35q1:null");
        if(validation.hasErrors()) {
            flash.error("%s", validation.errors());
            redirect(String.format("/page/%d/%d", PAGE_ID, listing.id));
        }
        
        Page page = new Page(listingId, PAGE_ID);
        page.responses.put("p35q1", p35q1);
        page.insert();
        listing.addResponsePage(page, PAGE_ID);
        redirect(listing.nextPageUrl(PAGE_ID, listing.id));
    }
}
