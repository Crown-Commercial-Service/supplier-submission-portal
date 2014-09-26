package controllers;

import com.google.gson.Gson;
import models.Listing;
import models.Page;
import play.mvc.Controller;

public class Page8 extends Controller {

    private static final Long PAGE_ID = 8l;

    public static void savePage(Long listingId, String[][] p8q1, String p8q2, String p8q3, String p8q4, String p8q5, String p8q6, String p8q7) {

        Listing listing = Listing.getByListingId(listingId);
        
        if(p8q1 != null) {
            // TODO: Validate arrays
        }
        validation.required(p8q2).message("p8q2:null");
        validation.required(p8q3).message("p8q3:null");
        validation.required(p8q6).message("p8q6:null");
        validation.required(p8q7).message("p8q7:null");
        if (!listing.lot.equals("SCS")) {
            validation.required(p8q4).message("p8q4:null");
            validation.required(p8q5).message("p8q5:null");
        }

        if(validation.hasErrors()) {
            flash.error("%s", validation.errors());
            redirect(String.format("/page/%d/%d", PAGE_ID, listing.id));
        }

        Gson gson = new Gson();
        Page page = new Page(listingId, PAGE_ID);
        page.responses.put("p8q1", gson.toJson(p8q1));
        page.responses.put("p8q2", p8q2);
        page.responses.put("p8q3", p8q3);
        page.responses.put("p8q4", p8q4);
        page.responses.put("p8q5", p8q5);
        page.responses.put("p8q6", p8q6);
        page.responses.put("p8q7", p8q7);
        page.insert();
        listing.addResponsePage(page, PAGE_ID);
        redirect(listing.nextPageUrl(PAGE_ID, listing.id));
    }

    public static void savePage(Long listingId, String[][] p8q1, String p8q2, String p8q3, String p8q6, String p8q7) {
        savePage(listingId, p8q1, p8q2, p8q3, null, null, p8q6, p8q7);
    }
}
