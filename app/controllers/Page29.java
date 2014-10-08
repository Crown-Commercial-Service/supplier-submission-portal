package controllers;

import models.Listing;
import models.Page;
import play.data.validation.Error;

import java.util.List;
import java.util.Map;

public class Page29 extends AuthenticatingController {

    // TODO: REMOVE QUESTIONS NO LONGER HERE
    
    private static final Long PAGE_ID = 29l;

    public static void savePage(Long listingId, String p29q1, String p29q2, String p29q3, String p29q4, String p29q5, String p29q6,
                                String p29q7, String p29q8, String p29q9, String p29q10, String p29q11, String p29q12, String p29q13) {

        Listing listing = Listing.getByListingId(listingId);

        // TODO: Validate all fields on this page requiring validation
        // Q9 is the only one for all lots
        validation.required(p29q9).key("p29q9");
        if (!listing.lot.equals("SCS")) {
            if (!listing.lot.equals("SaaS")) {
                // Q1 for IaaS, PaaS only
                validation.required(p29q1).key("p29q1");
            }
            // Rest are for all lots except SCS
            validation.required(p29q2).key("p29q2");
            validation.required(p29q3).key("p29q3");
            validation.required(p29q4).key("p29q4");
            validation.required(p29q5).key("p29q5");
            validation.required(p29q6).key("p29q6");
            validation.required(p29q7).key("p29q7");
            validation.required(p29q8).key("p29q8");
            validation.required(p29q10).key("p29q10");
            validation.required(p29q11).key("p29q11");
            validation.required(p29q12).key("p29q12");
            validation.required(p29q13).key("p29q13");
        }

        if(validation.hasErrors()) {

            for(Map.Entry<String, List<Error>> entry : validation.errorsMap().entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue().get(0).message();

                flash.put(key, value);
            }
            System.out.println(flash);
            redirect(String.format("/page/%d/%d", PAGE_ID, listing.id));
        }

        Page page = new Page(listingId, PAGE_ID);
        page.responses.put("p29q1", p29q1);
        page.responses.put("p29q2", p29q2);
        page.responses.put("p29q3", p29q3);
        page.responses.put("p29q4", p29q4);
        page.responses.put("p29q5", p29q5);
        page.responses.put("p29q6", p29q6);
        page.responses.put("p29q7", p29q7);
        page.responses.put("p29q8", p29q8);
        page.responses.put("p29q9", p29q9);
        page.responses.put("p29q10", p29q10);
        page.responses.put("p29q11", p29q11);
        page.responses.put("p29q12", p29q12);
        page.responses.put("p29q13", p29q13);
        page.insert();
        listing.addResponsePage(page, PAGE_ID);
        redirect(listing.nextPageUrl(PAGE_ID, listing.id));
    }

}
