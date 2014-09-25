package controllers;

import models.Listing;
import models.Page;
import play.mvc.Controller;

public class Page28 extends Controller {

    private static final Long PAGE_ID = 28l;

    public static void savePage(Long listingId, String p28q1) {

        Listing listing = Listing.getByListingId(listingId);

        validation.required(p28q1).message("p28q1:null");
        if(validation.hasErrors()) {
            flash.error("%s", validation.errors());
            redirect(String.format("/page/%d/%d", PAGE_ID, listing.id));
        }
        
        Page page = new Page(listingId, PAGE_ID);
        page.responses.put("p28q1", p28q1);
        page.insert();
        listing.addResponsePage(page, PAGE_ID);
        redirect(listing.nextPageUrl(PAGE_ID, listing.id));
    }
}
