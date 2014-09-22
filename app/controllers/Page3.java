package controllers;

import models.Listing;
import models.Page;
import play.mvc.Controller;

import java.util.Arrays;


public class Page3 extends Controller {

    private static final Long PAGE_ID = 3l;

    public static void savePage(Long listingId, String[] p3_q1) {
        
        Listing listing = Listing.getByListingId(listingId);
        // Validate all fields on this page requiring validation
        validation.required(p3_q1).message("Please choose at least one answer.");
        
        if(validation.hasErrors()) {
            flash.error("Validation failed: %s", validation.errors());
            redirect(String.format("/page/%d/%d", PAGE_ID, listing.id));
        }
        
        // Save the form data as a Page into the correct page index
        int index = listing.pageSequence.indexOf(PAGE_ID);
        Page page = new Page(listingId, PAGE_ID);
        page.responses.put("p3_q1", Arrays.asList(p3_q1).toString());
        listing.completedPages.add(index, page);
        listing.update();
        redirect(listing.nextPageUrl(PAGE_ID, listing.id));
    }
}
