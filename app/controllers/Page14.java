package controllers;

import models.Listing;
import models.Page;
import play.mvc.Controller;

public class Page14 extends Controller {

    private static final Long PAGE_ID = 14l;

    public static void savePage(Long listingId, String p14q1, String p14q2, String p14q3) {

        Listing listing = Listing.getByListingId(listingId);
        
        // Validate all fields on this page requiring validation
        validation.required(p14q1).message("p14q1 : null");
        validation.required(p14q2).message("p14q2 : null");
        validation.required(p14q3).message("p14q3 : null");

        if(validation.hasErrors()) {
            flash.error("%s", validation.errors());
            redirect(String.format("/page/%d/%d", PAGE_ID, listing.id));
        }
        
        int index = listing.pageSequence.indexOf(PAGE_ID);

        // Save the form data as a Page into the correct page index
        Page page = new Page(listingId, PAGE_ID);
        page.responses.put("p14q1", p14q1);
        page.responses.put("p14q2", p14q2);
        page.responses.put("p14q3", p14q3);

        page.insert();
        listing.addResponsePage(page, PAGE_ID);
        redirect(listing.nextPageUrl(PAGE_ID, listing.id));
    }
}
