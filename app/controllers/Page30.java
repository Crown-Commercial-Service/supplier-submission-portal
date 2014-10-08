package controllers;

import models.Listing;
import models.Page;
import play.data.validation.Error;

import java.util.List;
import java.util.Map;

public class Page30 extends AuthenticatingController {

    // TODO: REMOVE QUESTIONS NO LONGER HERE
    
    private static final Long PAGE_ID = 30l;

    public static void savePage(Long listingId, String p30q1, String p30q2, String p30q3, String p30q4, String p30q5, String p30q6,
                                String p30q7, String p30q8, String p30q9, String p30q10, String p30q11, String p30q12, String p30q13) {

        Listing listing = Listing.getByListingId(listingId);

        // TODO: Validate all fields on this page requiring validation
        // Q9 is the only one for all lots
        validation.required(p30q9).key("p30q9");
        if (!listing.lot.equals("SCS")) {
            if (!listing.lot.equals("SaaS")) {
                // Q1 for IaaS, PaaS only
                validation.required(p30q1).key("p30q1");
            }
            // Rest are for all lots except SCS
            validation.required(p30q2).key("p30q2");
            validation.required(p30q3).key("p30q3");
            validation.required(p30q4).key("p30q4");
            validation.required(p30q5).key("p30q5");
            validation.required(p30q6).key("p30q6");
            validation.required(p30q7).key("p30q7");
            validation.required(p30q8).key("p30q8");
            validation.required(p30q10).key("p30q10");
            validation.required(p30q11).key("p30q11");
            validation.required(p30q12).key("p30q12");
            validation.required(p30q13).key("p30q13");
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
        page.responses.put("p30q1", p30q1);
        page.responses.put("p30q2", p30q2);
        page.responses.put("p30q3", p30q3);
        page.responses.put("p30q4", p30q4);
        page.responses.put("p30q5", p30q5);
        page.responses.put("p30q6", p30q6);
        page.responses.put("p30q7", p30q7);
        page.responses.put("p30q8", p30q8);
        page.responses.put("p30q9", p30q9);
        page.responses.put("p30q10", p30q10);
        page.responses.put("p30q11", p30q11);
        page.responses.put("p30q12", p30q12);
        page.responses.put("p30q13", p30q13);
        page.insert();
        listing.addResponsePage(page, PAGE_ID);
        redirect(listing.nextPageUrl(PAGE_ID, listing.id));
    }

}
