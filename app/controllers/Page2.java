package controllers;

import com.google.gson.Gson;
import models.Listing;
import models.Page;
import play.data.validation.*;
import play.data.validation.Error;
import play.mvc.Controller;

import java.util.List;
import java.util.Map;

public class Page2 extends Controller {

    private static final Long PAGE_ID = 2l;

    public static void savePage(Long listingId, String[] p2q1) {
        
        Listing listing = Listing.getByListingId(listingId);
        
        // Validate all fields on this page requiring validation
        validation.required(p2q1).message("Please choose at least one answer.");
        if(validation.hasErrors()) {
            //flash.error("%s", validation.errors());

            for(Map.Entry<String, List<Error>> entry : validation.errorsMap().entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue().get(0).message();

                flash.put(key, value);
            }
            redirect(String.format("/page/%d/%d", PAGE_ID, listing.id));
        }
        
        // Save the form data as a Page into the correct page index
        Gson gson = new Gson();
        Page page = new Page(listingId, PAGE_ID);
        page.responses.put("p2q1", gson.toJson(p2q1));
        page.insert();
        listing.addResponsePage(page, PAGE_ID);
        redirect(listing.nextPageUrl(PAGE_ID, listing.id));
    }
}
