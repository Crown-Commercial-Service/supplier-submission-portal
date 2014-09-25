package controllers;

import models.Listing;
import models.Page;
import play.mvc.Controller;

public class Page33 extends Controller {

    private static final Long PAGE_ID = 33l;

    public static void savePage(Long listingId, String p33q1, String p33q2, String p33q3, String p33q4) {

        Listing listing = Listing.getByListingId(listingId);

        if (!listing.lot.equals("SaaS")) {
            validation.required(p33q1).message("p33q1:null");
            validation.required(p33q2).message("p33q2:null");
        }
        validation.required(p33q3).message("p33q3:null");
        validation.required(p33q4).message("p33q4:null");
        if(validation.hasErrors()) {
            flash.error("%s", validation.errors());
            redirect(String.format("/page/%d/%d", PAGE_ID, listing.id));
        }
       
        Page page = new Page(listingId, PAGE_ID);
        page.responses.put("p33q1", p33q1);
        page.responses.put("p33q2", p33q2);
        page.responses.put("p33q3", p33q3);
        page.responses.put("p33q4", p33q4);
        page.insert();
        listing.addResponsePage(page, PAGE_ID);
        redirect(listing.nextPageUrl(PAGE_ID, listing.id));
    }

    public static void savePage(Long listingId, String p33q3, String p33q4) {
        savePage(listingId, null, null, p33q3, p33q4);
    }
}
