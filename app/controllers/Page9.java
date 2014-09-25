package controllers;

import models.Listing;
import models.Page;
import play.mvc.Controller;


public class Page9 extends Controller {

    private static final Long PAGE_ID = 9l;

    public static void savePage(Long listingId, String p9q1) {

        Listing listing = Listing.getByListingId(listingId);

        //Validate all fields on this page requiring validation
        validation.required(p9q1).message("p9q1 : null");
        if(validation.hasErrors()) {
            flash.error("%s", validation.errors());
            redirect(String.format("/page/%d/%d", PAGE_ID, listing.id));
        }
        
        // Save the form data as a Page into the correct page index
        Page page = new Page(listingId, PAGE_ID);
        page.responses.put("p9q1", p9q1);
        page.insert();
        listing.addResponsePage(page, PAGE_ID);
        redirect(listing.nextPageUrl(PAGE_ID, listing.id));
    }
}
