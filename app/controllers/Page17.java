package controllers;

import models.Listing;
import models.Page;
import play.mvc.Controller;

public class Page17 extends Controller {

    private static final Long PAGE_ID = 17l;

    public static void savePage(Long listingId, String p17q1, String p17q2) {

        Listing listing = Listing.getByListingId(listingId);
        
        // Validate all fields on this page requiring validation
        validation.required(p17q1).message("p17q1 : null");
        validation.required(p17q2).message("p17q2 : null");
        if(validation.hasErrors()) {
            flash.error("%s", validation.errors());
            redirect(String.format("/page/%d/%d", PAGE_ID, listing.id));
        }

        // Save the form data as a Page into the correct page index
        Page page = new Page(listingId, PAGE_ID);
        page.responses.put("p17q1", p17q1);
        page.responses.put("p17q2", p17q2);
        page.insert();
        listing.addResponsePage(page, PAGE_ID);
        redirect(listing.nextPageUrl(PAGE_ID, listing.id));
    }
}
