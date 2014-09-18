package controllers;

import models.Listing;
import models.Page;
import play.mvc.Controller;

import java.util.Arrays;


public class Page1c extends Controller {

    private static final String PAGE_ID = "1c";

    public static void savePage(Long listingId, String[] p1c_q1) {
        
        Listing listing = Listing.getByListingId(listingId);
        // Validate all fields on this page requiring validation
        validation.required(p1c_q1).message("Please choose at least one answer.");
        
        if(validation.hasErrors()) {
            flash.error("Validation failed: %s", validation.errors());
            redirect(String.format("/page/%s/%s", PAGE_ID, listing.id));
        }
        
        // Save the form data as a Page into the correct page index
        int index = listing.pageSequence.indexOf(PAGE_ID);
        Page page = new Page(listingId, PAGE_ID);
        page.responses.put("p1c_q1", Arrays.asList(p1c_q1).toString());
        listing.completedPages.add(index, page);
        listing.update();
        redirect(String.format("/page/%s/%s", listing.nextPage(PAGE_ID), listing.id));
    }
}
