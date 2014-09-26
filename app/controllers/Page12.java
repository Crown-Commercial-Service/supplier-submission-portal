package controllers;

import models.Listing;
import models.Page;
import play.mvc.Controller;

// Not a real Controller - just a template that can serve as a skeleton for page controllers 

public class Page12 extends Controller {

    private static final Long PAGE_ID = 12l;

    public static void savePage(Long listingId, String p12q1) {

        Listing listing = Listing.getByListingId(listingId);
        
        // TODO: Validate all fields on this page requiring validation
//        validation.required(p12q1).message("p12q1:null");
        if(validation.hasErrors()) {
            flash.error("%s", validation.errors());
            redirect(String.format("/page/%d/%d", PAGE_ID, listing.id));
        }

        //Save the form data as a Page into the correct page index
        Page page = new Page(listingId, PAGE_ID);
        page.responses.put("p12q1", p12q1);
        page.insert();
        listing.addResponsePage(page, PAGE_ID);
        redirect(listing.nextPageUrl(PAGE_ID, listing.id));
    }
}
