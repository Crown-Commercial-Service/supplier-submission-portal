package controllers;

import models.Listing;
import models.Page;
import play.mvc.Controller;

import play.data.validation.*;
import play.data.validation.Error;

import java.util.ArrayList;
import java.util.Map;
import java.util.List;

public class Page27 extends Controller {

    private static final Long PAGE_ID = 27l;

    public static void savePage(Long listingId, String p27q1, String p27q2, String p27q3, String p27q4, String p27q5, String p27q6,
                                String p27q7, String p27q8, String p27q9, String p27q10, String p27q11, String p27q12, String p27q13) {

        Listing listing = Listing.getByListingId(listingId);

        // TODO: Validate all fields on this page requiring validation
        // Q9 is the only one for all lots
        validation.required(p27q9).key("p27q9");
        if (!listing.lot.equals("SCS")) {
            if (!listing.lot.equals("SaaS")) {
                // Q1 for IaaS, PaaS only
                validation.required(p27q1).key("p27q1");
            }
            // Rest are for all lots except SCS
            validation.required(p27q2).key("p27q2");
            validation.required(p27q3).key("p27q3");
            validation.required(p27q4).key("p27q4");
            validation.required(p27q5).key("p27q5");
            validation.required(p27q6).key("p27q6");
            validation.required(p27q7).key("p27q7");
            validation.required(p27q8).key("p27q8");
            validation.required(p27q10).key("p27q10");
            validation.required(p27q11).key("p27q11");
            validation.required(p27q12).key("p27q12");
            validation.required(p27q13).key("p27q13");
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
        page.responses.put("p27q1", p27q1);
        page.responses.put("p27q2", p27q2);
        page.responses.put("p27q3", p27q3);
        page.responses.put("p27q4", p27q4);
        page.responses.put("p27q5", p27q5);
        page.responses.put("p27q6", p27q6);
        page.responses.put("p27q7", p27q7);
        page.responses.put("p27q8", p27q8);
        page.responses.put("p27q9", p27q9);
        page.responses.put("p27q10", p27q10);
        page.responses.put("p27q11", p27q11);
        page.responses.put("p27q12", p27q12);
        page.responses.put("p27q13", p27q13);
        page.insert();
        listing.addResponsePage(page, PAGE_ID);
        redirect(listing.nextPageUrl(PAGE_ID, listing.id));
    }

}
