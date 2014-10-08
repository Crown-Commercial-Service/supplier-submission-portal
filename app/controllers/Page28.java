package controllers;

import models.Listing;
import models.Page;
import play.data.validation.Error;

import java.util.List;
import java.util.Map;

public class Page28 extends AuthenticatingController {

    // TODO: REMOVE QUESTIONS NO LONGER HERE
    
    private static final Long PAGE_ID = 28l;

    public static void savePage(Long listingId, String p28q1, String p28q2, String p28q3, String p28q4, String p28q5, String p28q6,
                                String p28q7, String p28q8, String p28q9, String p28q10, String p28q11, String p28q12, String p28q13) {

        Listing listing = Listing.getByListingId(listingId);

        // TODO: Validate all fields on this page requiring validation
        // Q9 is the only one for all lots
        validation.required(p28q9).key("p28q9");
        if (!listing.lot.equals("SCS")) {
            if (!listing.lot.equals("SaaS")) {
                // Q1 for IaaS, PaaS only
                validation.required(p28q1).key("p28q1");
            }
            // Rest are for all lots except SCS
            validation.required(p28q2).key("p28q2");
            validation.required(p28q3).key("p28q3");
            validation.required(p28q4).key("p28q4");
            validation.required(p28q5).key("p28q5");
            validation.required(p28q6).key("p28q6");
            validation.required(p28q7).key("p28q7");
            validation.required(p28q8).key("p28q8");
            validation.required(p28q10).key("p28q10");
            validation.required(p28q11).key("p28q11");
            validation.required(p28q12).key("p28q12");
            validation.required(p28q13).key("p28q13");
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
        page.responses.put("p28q1", p28q1);
        page.responses.put("p28q2", p28q2);
        page.responses.put("p28q3", p28q3);
        page.responses.put("p28q4", p28q4);
        page.responses.put("p28q5", p28q5);
        page.responses.put("p28q6", p28q6);
        page.responses.put("p28q7", p28q7);
        page.responses.put("p28q8", p28q8);
        page.responses.put("p28q9", p28q9);
        page.responses.put("p28q10", p28q10);
        page.responses.put("p28q11", p28q11);
        page.responses.put("p28q12", p28q12);
        page.responses.put("p28q13", p28q13);
        page.insert();
        listing.addResponsePage(page, PAGE_ID);
        redirect(listing.nextPageUrl(PAGE_ID, listing.id));
    }

}
