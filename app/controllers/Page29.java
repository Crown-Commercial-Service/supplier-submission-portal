package controllers;

import models.Listing;
import models.Page;
import play.mvc.Controller;

public class Page29 extends Controller {

    private static final Long PAGE_ID = 29l;

    public static void savePage(Long listingId, String p29q1, String p29q2, String p29q3) {

        Listing listing = Listing.getByListingId(listingId);

        validation.required(p29q1).message("p29q1:null");
        validation.required(p29q2).message("p29q2:null");
        validation.required(p29q3).message("p29q3:null");
        if(validation.hasErrors()) {
            flash.error("%s", validation.errors());
            redirect(String.format("/page/%d/%d", PAGE_ID, listing.id));
        }

        Page page = new Page(listingId, PAGE_ID);
        page.responses.put("p29q1", p29q1);
        page.responses.put("p29q2", p29q2);
        page.responses.put("p29q3", p29q3);
        page.insert();
        listing.addResponsePage(page, PAGE_ID);
        redirect(listing.nextPageUrl(PAGE_ID, listing.id));
    }
}
