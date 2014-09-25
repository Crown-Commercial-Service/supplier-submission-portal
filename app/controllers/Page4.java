package controllers;

import models.Listing;
import models.Page;
import play.mvc.Controller;

public class Page4 extends Controller {

    private static final Long PAGE_ID = 4l;

    public static void savePage(Long listingId, String p4q1, String p4q2) {
        
        Listing listing = Listing.getByListingId(listingId);
        // Validate all fields on this page requiring validation
        validation.required(p4q1).message("Please enter something.");
        validation.required(p4q2).message("Please enter something else.");
        
        if(validation.hasErrors()) {
            flash.error("%s", validation.errors());
            redirect(String.format("/page/%d/%d", PAGE_ID, listing.id));
        }
        
        // Save the form data as a Page into the correct page index
        Page page = new Page(listingId, PAGE_ID);
        page.responses.put("p4q1", p4q1);
        page.responses.put("p4q2", p4q2);
        page.insert();
        listing.title = p4q1;
        listing.description = p4q2;
        listing.addResponsePage(page, PAGE_ID);
        redirect(listing.nextPageUrl(PAGE_ID, listing.id));
    }
}
