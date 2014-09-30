package controllers;

import models.Listing;
import models.Page;
import play.mvc.Controller;

import play.data.validation.*;
import play.data.validation.Error;

import java.util.ArrayList;
import java.util.Map;
import java.util.List;

public class Page30 extends AuthenticatingController {

    private static final Long PAGE_ID = 30l;

    public static void savePage(Long listingId, String p30q1, String p30q2, String p30q3, String p30q4, String p30q5) {

        Listing listing = Listing.getByListingId(listingId);

        // TODO: Validate all fields on this page requiring validation
        validation.required(p30q1).key("p30q1");
        if (!listing.lot.equals("SCS")) {
            validation.required(p30q2).key("p30q2");
            validation.required(p30q3).key("p30q3");
            validation.required(p30q4).key("p30q4");
            if (!listing.lot.equals("SaaS")) {
                validation.required(p30q5).key("p30q5");
            }
        }

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

}
