package controllers;

import models.Listing;
import models.Page;
import play.mvc.Controller;

public class Page31 extends Controller {

    private static final Long PAGE_ID = 31l;

    public static void savePage(Long listingId, String p31q1, String p31q2, String p31q3, String p31q4, String p31q5) {

        Listing listing = Listing.getByListingId(listingId);

        validation.required(p31q1).message("p31q1:null");
        validation.required(p31q2).message("p31q2:null");
        validation.required(p31q3).message("p31q3:null");
        validation.required(p31q4).message("p31q4:null");
        if (!listing.lot.equals("SaaS")) {
            validation.required(p31q5).message("p31q5:null");
        }
        if(validation.hasErrors()) {
            flash.error("%s", validation.errors());
            redirect(String.format("/page/%d/%d", PAGE_ID, listing.id));
        }
        
        Page page = new Page(listingId, PAGE_ID);
        page.responses.put("p31q1", p31q1);
        page.responses.put("p31q2", p31q2);
        page.responses.put("p31q3", p31q3);
        page.responses.put("p31q4", p31q4);
        page.responses.put("p31q5", p31q5);
        page.insert();
        listing.addResponsePage(page, PAGE_ID);
        redirect(listing.nextPageUrl(PAGE_ID, listing.id));
    }

    public static void savePage(Long listingId, String p31q1, String p31q2, String p31q3, String p31q4) {
        savePage(listingId, p31q1, p31q2, p31q3, p31q4, null);
    }
}
