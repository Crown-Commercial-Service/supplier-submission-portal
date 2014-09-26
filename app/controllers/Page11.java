package controllers;

import models.Listing;
import models.Page;
import play.mvc.Controller;

public class Page11 extends Controller {

    private static final Long PAGE_ID = 11l;

    public static void savePage(Long listingId, String p11q1, String p11q2) {

        Listing listing = Listing.getByListingId(listingId);
        
        // TODO: Validate all fields on this page requiring validation
//        validation.required(p11q1).message("p11q1:null");
//        validation.required(p11q2).message("p11q2:null");
        if(validation.hasErrors()) {
            flash.error("%s", validation.errors());
            redirect(String.format("/page/%d/%d", PAGE_ID, listing.id));
        }

        // Save the form data as a Page into the correct page index
        Page page = new Page(listingId, PAGE_ID);
        page.responses.put("p11q1", p11q1);
        page.responses.put("p11q2", p11q2);
        page.insert();
        listing.addResponsePage(page, PAGE_ID);
        redirect(listing.nextPageUrl(PAGE_ID, listing.id));
    }
}
