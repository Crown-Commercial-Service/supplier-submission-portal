package controllers;

import models.Listing;
import models.Page;
import play.mvc.Controller;

public class Page30 extends Controller {

    private static final Long PAGE_ID = 30l;

    public static void savePage(Long listingId, String p30q1, String p30q2, String p30q3, String p30q4, String p30q5) {

        Listing listing = Listing.getByListingId(listingId);

        validation.required(p30q1).message("p30q1:null");
        if (!listing.lot.equals("SCS")) {
            validation.required(p30q2).message("p30q2:null");
            validation.required(p30q3).message("p30q3:null");
            validation.required(p30q4).message("p30q4:null");
            if (!listing.lot.equals("SaaS")) {
                validation.required(p30q5).message("p30q5:null");
            }
        }
        if(validation.hasErrors()) {
            flash.error("%s", validation.errors());
            redirect(String.format("/page/%d/%d", PAGE_ID, listing.id));
        }

        Page page = new Page(listingId, PAGE_ID);
        page.responses.put("p30q1", p30q1);
        page.responses.put("p30q2", p30q2);
        page.responses.put("p30q3", p30q3);
        page.responses.put("p30q4", p30q4);
        page.responses.put("p30q5", p30q5);
        page.insert();
        listing.addResponsePage(page, PAGE_ID);
        redirect(listing.nextPageUrl(PAGE_ID, listing.id));
    }

    public static void savePage(Long listingId, String p30q1) {
        savePage(listingId, p30q1, null, null, null, null);
    }

    public static void savePage(Long listingId, String p30q1, String p30q2, String p30q3, String p30q4) {
        savePage(listingId, p30q1, p30q2, p30q3, p30q4, null);
    }
}
