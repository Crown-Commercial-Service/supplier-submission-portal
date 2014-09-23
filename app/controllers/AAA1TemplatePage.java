package controllers;

import models.Listing;
import models.Page;
import play.mvc.Controller;

import java.util.Arrays;

// Not a real Controller - just a template that can serve as a skeleton for page controllers 

public class AAA1TemplatePage extends Controller {

    private static final Long PAGE_ID = -1l;

    // TODO: This method will be very similar for all pages except for field-specific validation methods - how to factor out?
    public static void savePage(Long listingId /* question responses passed in e.g. String[] p1a_q1 */) {

        Listing listing = Listing.getByListingId(listingId);
        
        // TODO: Validate all fields on this page requiring validation

        if(validation.hasErrors()) {
            for(play.data.validation.Error error : validation.errors()) {
                System.out.println("Validation error: " + error.message());
            }
            flash.error("Validation failed: %s", validation.errors());
            redirect(String.format("/page/%d/%d", PAGE_ID, listing.id));
        }
        
        int index = listing.pageSequence.indexOf(PAGE_ID);
        //TODO: Save the form data as a Page into the correct page index
        Page page = new Page(listingId, PAGE_ID);
        // page.responses.put("p1a_q1", Arrays.asList(p1a_q1).toString());
        // ...etc. for all questions on page
        page.insert();
        listing.completedPages.add(index, page);
        listing.update();
        redirect(listing.nextPageUrl(PAGE_ID, listing.id));
    }
}
