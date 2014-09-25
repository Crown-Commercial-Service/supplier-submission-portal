package controllers;

import models.Listing;
import models.Page;
import play.mvc.Controller;

import java.io.File;

// Not a real Controller - just a template that can serve as a skeleton for page controllers 

public class Page7 extends Controller {

    private static final Long PAGE_ID = -1l;


    public static void savePage(Long listingId, String p7q1, String p7q2, File p7doc) {

        Listing listing = Listing.getByListingId(listingId);
        
        //Validate all fields on this page requiring validation
        validation.required(p7q1).message("p7q1 : null");
        validation.required(p7q2).message("p7q2 : null");
        //validation.required(p7doc).message("p7doc : null");

        if(validation.hasErrors()) {
            flash.error("%s", validation.errors());
            redirect(String.format("/page/%d/%d", PAGE_ID, listing.id));
        }
        
        int index = listing.pageSequence.indexOf(PAGE_ID);

        //Save the form data as a Page into the correct page index
        Page page = new Page(listingId, PAGE_ID);
        page.responses.put("p7q1", p7q1);
        page.responses.put("p7q2", p7q1);
        // TODO: Document storage

        // ...etc. for all questions on page
        page.insert();
        listing.addResponsePage(page, PAGE_ID);
        redirect(listing.nextPageUrl(PAGE_ID, listing.id));
    }
}
