package controllers;

import models.Listing;
import models.Page;
import play.mvc.Controller;

public class Page18 extends Controller {

    private static final Long PAGE_ID = 18l;

    public static void savePage(Long listingId, String p18q1) {

        Listing listing = Listing.getByListingId(listingId);
        
        // Validate all fields on this page requiring validation
        validation.required(p18q1).message("p18q1 : null");

        if(validation.hasErrors()) {
            flash.error("%s", validation.errors());
            redirect(String.format("/page/%d/%d", PAGE_ID, listing.id));
        }
        
        int index = listing.pageSequence.indexOf(PAGE_ID);

        // Save the form data as a Page into the correct page index
        Page page = new Page(listingId, PAGE_ID);
        page.responses.put("p18q1", p18q1);

        page.insert();
        listing.addResponsePage(page, PAGE_ID);
        redirect(listing.nextPageUrl(PAGE_ID, listing.id));
    }
}
