package controllers;

import com.google.gson.Gson;
import models.Listing;
import models.Page;
import play.data.validation.Error;

import java.util.Map;
import java.util.List;

public class Page24 extends AuthenticatingController {

    private static final Long PAGE_ID = 24l;

    public static void savePage(Long listingId, String[] p24q1, String[] p24q2, String p24q3, String p24q4, String p24q5, String p24q6,
                                String p24q7, String p24q8, String p24q9, String p24q10, String p24q11, String p24q1assurance,
                                String p24q2assurance, String p24q3assurance, String p24q4assurance, String p24q5assurance,
                                String p24q6assurance, String p24q7assurance, String p24q8assurance, String p24q9assurance,
                                String p24q10assurance, String p24q11assurance) {

        Listing listing = Listing.getByListingId(listingId);

        // Validate all fields on this page requiring validation
        if (!listing.lot.equals("SCS")) {
            // SaaS, PaaS, IaaS
            validation.required(p24q1).key("p24q1");
            validation.required(p24q2).key("p24q2");
            validation.required(p24q4).key("p24q4");
            validation.required(p24q5).key("p24q5");
            validation.required(p24q6).key("p24q6");

            validation.required(p24q1assurance).key("p24q1");
            validation.required(p24q2assurance).key("p24q2");
            validation.required(p24q4assurance).key("p24q4");
            validation.required(p24q5assurance).key("p24q5");
            validation.required(p24q6assurance).key("p24q6");

            // Validate percentage: .max() catches anything not a number
            validation.required(p24q10).key("p24q10");
            validation.max(p24q10, 99.99999999999999).key("p24q10").message("validationNotANumber");
            validation.required(p24q10assurance).key("p24q10");

            if (!listing.lot.equals("SaaS")) {
                //PaaS, IaaS
                validation.required(p24q7).key("p24q7");
                validation.required(p24q8).key("p24q8");
                validation.required(p24q9).key("p24q9");
                validation.required(p24q7assurance).key("p24q7");
                validation.required(p24q8assurance).key("p24q8");
                validation.required(p24q9assurance).key("p24q9");
            }
        }

        // Everything
        validation.required(p24q3).key("p24q3");
        validation.required(p24q3assurance).key("p24q3");

        if(validation.hasErrors()) {
            flash.put("body", params.get("body"));
            for(Map.Entry<String, List<Error>> entry : validation.errorsMap().entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue().get(0).message();

                flash.put(key, value);
            }
            System.out.println(flash);
            redirect(String.format("/page/%d/%d", PAGE_ID, listing.id));
        }

        // Save the form data as a Page into the correct page index
        Gson gson = new Gson();
        Page page = new Page(listingId, PAGE_ID);
        if (p24q1 != null) {
            page.responses.put("p24q1", gson.toJson(p24q1));
            page.responses.put("p24q1assurance", p24q1assurance);
        }
        else {page.responses.put("p24q1", null);
        }
        if (p24q2 != null) {
            page.responses.put("p24q2", gson.toJson(p24q2));
            page.responses.put("p24q2assurance", p24q2assurance);
        }
        else {page.responses.put("p24q2", null);
        }
        page.responses.put("p24q3", p24q3);
        page.responses.put("p24q4", p24q4);
        page.responses.put("p24q5", p24q5);
        page.responses.put("p24q6", p24q6);
        page.responses.put("p24q7", p24q7);
        page.responses.put("p24q8", p24q8);
        page.responses.put("p24q9", p24q9);
        page.responses.put("p24q10", p24q10);
        page.responses.put("p24q11", p24q11);

        page.responses.put("p24q3assurance", p24q3assurance);
        page.responses.put("p24q4assurance", p24q4assurance);
        page.responses.put("p24q5assurance", p24q5assurance);
        page.responses.put("p24q6assurance", p24q6assurance);
        page.responses.put("p24q7assurance", p24q7assurance);
        page.responses.put("p24q8assurance", p24q8assurance);
        page.responses.put("p24q9assurance", p24q9assurance);
        page.responses.put("p24q10assurance", p24q10assurance);
        page.responses.put("p24q11assurance", p24q11assurance);
        page.insert();
        listing.addResponsePage(page, PAGE_ID, supplierDetailsFromCookie.get("supplierEmail"));
        redirect(listing.nextPageUrl(PAGE_ID, listing.id));
    }

}
