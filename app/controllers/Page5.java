package controllers;

import models.Listing;
import models.Page;
import play.mvc.Controller;

public class Page5 extends Controller {

    private static final Long PAGE_ID = 5l;

    public static void savePage(Long listingId, String p5q1, String p5q2) {

        Listing listing = Listing.getByListingId(listingId);

        // Validate all fields on this page requiring validation
        validation.required(p5q1).message("p5q1 : null");
        validation.required(p5q2).message("p5q2 : null");

        if(validation.hasErrors()) {
            flash.error("%s", validation.errors());
            redirect(String.format("/page/%d/%d", PAGE_ID, listing.id));
        }

        int index = listing.pageSequence.indexOf(PAGE_ID);

        Page page = new Page(listingId, PAGE_ID);
        page.responses.put("p5q1", p5q1);
        page.responses.put("p5q2", p5q2);

        page.insert();
        listing.addResponsePage(page, PAGE_ID);
        redirect(listing.nextPageUrl(PAGE_ID, listing.id));
    }
}
