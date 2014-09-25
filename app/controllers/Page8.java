package controllers;

import models.Listing;
import models.Page;
import play.mvc.Controller;

//TODO : Page needs completing

public class Page8 extends Controller {

    private static final Long PAGE_ID = 8l;

    public static void savePage(Long listingId) {

        Listing listing = Listing.getByListingId(listingId);
        
        // TODO: Validate all fields on this page requiring validation

        if(validation.hasErrors()) {
            flash.error("%s", validation.errors());
            redirect(String.format("/page/%d/%d", PAGE_ID, listing.id));
        }
        
        int index = listing.pageSequence.indexOf(PAGE_ID);
        //TODO: Save the form data as a Page into the correct page index
        Page page = new Page(listingId, PAGE_ID);
        // page.responses.put("p1_q1", Arrays.asList(p1_q1).toString());
        // ...etc. for all questions on page
        page.insert();
        listing.addResponsePage(page, PAGE_ID);
        redirect(listing.nextPageUrl(PAGE_ID, listing.id));
    }
}
