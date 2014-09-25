package controllers;

import models.Listing;
import models.Page;
import play.mvc.Controller;

public class Page22 extends Controller {

    private static final Long PAGE_ID = 22l;

    public static void savePage(Long listingId, String p22q1, String p22q2, String p22q3, String p22q4, String p22q5) {

        Listing listing = Listing.getByListingId(listingId);
        
        // Validate all fields on this page requiring validation
        validation.required(p22q1).message("p22q1 : null");
        validation.required(p22q2).message("p22q2 : null");
        validation.required(p22q3).message("p22q3 : null");
        validation.required(p22q4).message("p22q4 : null");
        validation.required(p22q5).message("p22q5 : null");

        if(validation.hasErrors()) {
            flash.error("%s", validation.errors());
            redirect(String.format("/page/%d/%d", PAGE_ID, listing.id));
        }
        
        int index = listing.pageSequence.indexOf(PAGE_ID);

        // Save the form data as a Page into the correct page index
        Page page = new Page(listingId, PAGE_ID);
        page.responses.put("p22q1", p22q1);
        page.responses.put("p22q2", p22q2);
        page.responses.put("p22q3", p22q3);
        page.responses.put("p22q4", p22q4);
        page.responses.put("p22q5", p22q5);

        page.insert();
        listing.addResponsePage(page, PAGE_ID);
        redirect(listing.nextPageUrl(PAGE_ID, listing.id));
    }
}
