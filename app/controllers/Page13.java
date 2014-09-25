package controllers;

import models.Listing;
import models.Page;
import play.mvc.Controller;

public class Page13 extends Controller {

    private static final Long PAGE_ID = 13l;

    public static void savePage(Long listingId, String p13q1, String p13q2, String p13q3) {

        Listing listing = Listing.getByListingId(listingId);
        
        // Validate all fields on this page requiring validation
        validation.required(p13q1).message("p13q1 : null");
        validation.required(p13q2).message("p13q2 : null");
        validation.required(p13q3).message("p13q3 : null");

        if(validation.hasErrors()) {
            flash.error("%s", validation.errors());
            redirect(String.format("/page/%d/%d", PAGE_ID, listing.id));
        }
        
        int index = listing.pageSequence.indexOf(PAGE_ID);

        // Save the form data as a Page into the correct page index
        Page page = new Page(listingId, PAGE_ID);
        page.responses.put("p13q1", p13q1);
        page.responses.put("p13q2", p13q2);
        page.responses.put("p13q3", p13q3);

        page.insert();
        listing.addResponsePage(page, PAGE_ID);
        redirect(listing.nextPageUrl(PAGE_ID, listing.id));
    }
}
