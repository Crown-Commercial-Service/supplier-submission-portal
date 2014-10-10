package controllers;

import com.google.gson.Gson;
import models.Listing;
import models.Page;
import play.data.validation.Error;

import java.util.ArrayList;
import java.util.Map;
import java.util.List;

public class Page24 extends AuthenticatingController {

    private static final Long PAGE_ID = 24l;

    public static void savePage(Long listingId, String[] p24q1, String[] p24q2, String p24q3, String p24q4, String p24q5, String p24q6
            , String p24q7, String p24q8, String p24q9, String p24q10, String p24q11) {

        Listing listing = Listing.getByListingId(listingId);

        // TODO: Validate all fields on this page requiring validation
        if (!listing.lot.equals("SCS")) {
            // SaaS, PaaS, IaaS
            validation.required(p24q1).key("p24q1");
            validation.required(p24q2).key("p24q2");
            validation.required(p24q4).key("p24q4");
            validation.required(p24q5).key("p24q5");
            validation.required(p24q6).key("p24q6");

            // Validate percentage: .max() catches anything not a number
            validation.required(p24q11).key("p24q11");
            validation.max(p24q11, 99.99999999999999).key("p24q11").message("validationNotANumber");

            if (!listing.lot.equals("SaaS")) {
                //PaaS, IaaS
                validation.required(p24q7).key("p24q7");
                validation.required(p24q8).key("p24q8");
                validation.required(p24q9).key("p24q9");
                validation.required(p24q10).message("p24q10");
            }
        }

        // Everything
        validation.required(p24q3).key("p24q3");

        if(validation.hasErrors()) {

            for(Map.Entry<String, List<Error>> entry : validation.errorsMap().entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue().get(0).message();

                flash.put(key, value);
            }
            System.out.println(flash);
            redirect(String.format("/page/%d/%d", PAGE_ID, listing.id));
        }

        // Save the form data as a Page into the correct page index
        Page page = new Page(listingId, PAGE_ID);
        if (p24q1 != null) {
            page.addFieldToPageResponse("p24q1", p24q1);
        }
        else {page.responses.put("p24q1", null);
        }
        if (p24q2 != null) {
            page.addFieldToPageResponse("p24q2", p24q2);
        }
        else {
            page.responses.put("p24q2", new ArrayList<String>());
            page.addFieldToPageResponse("p24q2");
        }
        page.addFieldToPageResponse("p24q3", p24q3);
        page.addFieldToPageResponse("p24q4", p24q4);
        page.addFieldToPageResponse("p24q5", p24q5);
        page.addFieldToPageResponse("p24q6", p24q6);
        page.addFieldToPageResponse("p24q7", p24q7);
        page.addFieldToPageResponse("p24q8", p24q8);
        page.addFieldToPageResponse("p24q9", p24q9);
        page.addFieldToPageResponse("p24q10", p24q10);
        page.addFieldToPageResponse("p24q11", p24q11);
        page.insert();
        listing.addResponsePage(page, PAGE_ID);
        redirect(listing.nextPageUrl(PAGE_ID, listing.id));
    }

}
