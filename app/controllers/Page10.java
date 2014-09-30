package controllers;

import com.google.gson.Gson;
import models.Listing;
import models.Page;
import play.mvc.Controller;

import play.data.validation.*;
import play.data.validation.Error;

import java.util.ArrayList;
import java.util.Map;
import java.util.List;

public class Page10 extends Controller {

    private static final Long PAGE_ID = 10l;

    public static void savePage(Long listingId, String[] p10q1, String p10q2, String p10q3, String p10q4, String p10q5) {

        Listing listing = Listing.getByListingId(listingId);

        //TODO: Validate all fields on this page requiring validation
        validation.required(p10q1).key("p10q1");
        validation.required(p10q2).key("p10q2");
        validation.required(p10q3).key("p10q3");
        validation.required(p10q4).key("p10q4");
        validation.required(p10q5).key("p10q5");

        if(validation.hasErrors()) {
            //flash.error("%s", validation.errors());

            for(Map.Entry<String, List<Error>> entry : validation.errorsMap().entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue().get(0).message();

                flash.put(key, value);
            }
            System.out.println(flash);
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
