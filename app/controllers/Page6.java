package controllers;

import models.Listing;
import models.Page;
import play.mvc.Controller;

import java.io.File;

public class Page6 extends Controller {

    private static final Long PAGE_ID = 6l;

    // TODO: Missing properties in file

    public static void savePage(Long listingId, String p6q1 , String p6q2, File pg6doc) {

        Listing listing = Listing.getByListingId(listingId);

        // Validate all fields on this page requiring validation
        validation.required(p6q1).message("p6q1 : null");
        validation.required(p6q2).message("p6q2 : null");
        //validation.required(p6doc).message("p6_q3 : null");

        if(validation.hasErrors()) {
            flash.error("%s", validation.errors());
            redirect(String.format("/page/%d/%d", PAGE_ID, listing.id));
        }
        
        int index = listing.pageSequence.indexOf(PAGE_ID);

        Page page = new Page(listingId, PAGE_ID);
        page.responses.put("p6q1", p6q1);
        page.responses.put("p6q1", p6q2);
        // TODO: Document storage

        // ...etc. for all questions on page
        page.insert();
        listing.addResponsePage(page, PAGE_ID);
        redirect(listing.nextPageUrl(PAGE_ID, listing.id));
    }
}
