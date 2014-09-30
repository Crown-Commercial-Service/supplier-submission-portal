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

public class Page33 extends Controller {

    private static final Long PAGE_ID = 33l;

    public static void savePage(Long listingId, String p33q1, String[] p33q2, String p33q3, String p33q4) {

        Listing listing = Listing.getByListingId(listingId);

        // TODO: Validate all fields on this page requiring validation
        if (!listing.lot.equals("SaaS")) {
            validation.required(p33q1).key("p33q1");
            validation.required(p33q2).key("p33q2");
        }
        validation.required(p33q3).key("p33q3");
        validation.required(p33q4).key("p33q4");

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

        Gson gson = new Gson();
        Page page = new Page(listingId, PAGE_ID);
        page.responses.put("p33q1", p33q1);
        if (p33q2 != null) {
            page.responses.put("p33q2", gson.toJson(p33q2));
        }
        else {
            page.responses.put("p33q2", null);
        }
        page.responses.put("p33q3", p33q3);
        page.responses.put("p33q4", p33q4);
        page.insert();
        listing.addResponsePage(page, PAGE_ID);
        redirect(listing.nextPageUrl(PAGE_ID, listing.id));
    }

}
