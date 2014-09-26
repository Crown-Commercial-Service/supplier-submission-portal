package controllers;

import com.google.gson.Gson;
import models.Listing;
import models.Page;
import play.mvc.Controller;

public class Page10 extends Controller {

    private static final Long PAGE_ID = 10l;

    public static void savePage(Long listingId, String[] p10q1, String p10q2, String p10q3, String p10q4, String p10q5) {

        Listing listing = Listing.getByListingId(listingId);
        
        //TODO: Validate all fields on this page requiring validation
//        validation.required(p10q1).message("p10q1:null");
//        validation.required(p10q2).message("p10q2:null");
//        validation.required(p10q3).message("p10q3:null");
//        validation.required(p10q4).message("p10q4:null");
//        validation.required(p10q5).message("p10q5:null");
        if(validation.hasErrors()) {
            flash.error("%s", validation.errors());
            redirect(String.format("/page/%d/%d", PAGE_ID, listing.id));
        }

        //Save the form data as a Page into the correct page index
        Gson gson = new Gson();
        Page page = new Page(listingId, PAGE_ID);
        page.responses.put("p10q1", gson.toJson(p10q1));
        page.responses.put("p10q2", p10q2);
        page.responses.put("p10q3", p10q3);
        page.responses.put("p10q4", p10q4);
        page.responses.put("p10q5", p10q5);
        page.insert();
        listing.addResponsePage(page, PAGE_ID);
        redirect(listing.nextPageUrl(PAGE_ID, listing.id));
    }
}
