package controllers;

import models.Listing;
import models.Page;
import play.mvc.Controller;

public class Page4 extends Controller {

    private static final Long PAGE_ID = 4l;

    public static void savePage(Long listingId, String p4_q1, String p4_q2) {
        
        Listing listing = Listing.getByListingId(listingId);
        // Validate all fields on this page requiring validation
        validation.required(p4_q1).message("Please enter something.");
        validation.required(p4_q2).message("Please enter something else.");
        
        if(validation.hasErrors()) {
            flash.error("Validation failed: %s", validation.errors());
            redirect(String.format("/page/%d/%d", PAGE_ID, listing.id));
        }
        
        // Save the form data as a Page into the correct page index
        Page page = new Page(listingId, PAGE_ID);
        page.responses.put("p4_q1", p4_q1);
        page.responses.put("p4_q2", p4_q2);
        page.insert();
        listing.title = p4_q1;
        listing.description = p4_q2;
        listing.addResponsePage(page, PAGE_ID);
        redirect(listing.nextPageUrl(PAGE_ID, listing.id));
    }
}
