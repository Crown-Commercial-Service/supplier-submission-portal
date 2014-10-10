package controllers;

import models.Listing;
import models.Page;
import play.data.validation.Error;

import java.util.Map;
import java.util.List;

public class Page33 extends AuthenticatingController {

    private static final Long PAGE_ID = 33l;

    public static void savePage(Long listingId, String p33q1, String p33q2, String p33q3, String p33q4, String p33q5,
                                String p33q1assurance, String p33q2assurance, String p33q3assurance, String p33q4assurance, String p33q5assurance) {

        Listing listing = Listing.getByListingId(listingId);

        // Validate all fields on this page requiring validation
        validation.required(p33q1).key("p33q1");
        validation.maxSize(p33q1, 10);
        validation.required(p33q1assurance).key("p33q1assurance");
        validation.maxSize(p33q1assurance, 50);
        if (!listing.lot.equals("SCS")) {
            // SaaS, PaaS, IaaS
            validation.required(p33q2).key("p33q2");
            validation.maxSize(p33q2, 10);
            validation.required(p33q3).key("p33q3");
            validation.maxSize(p33q3, 10);
            validation.required(p33q4).key("p33q4");
            validation.maxSize(p33q4, 10);
            validation.required(p33q2assurance).key("p33q2assurance");
            validation.maxSize(p33q2assurance, 50);
            validation.required(p33q3assurance).key("p33q3assurance");
            validation.maxSize(p33q3assurance, 50);
            validation.required(p33q4assurance).key("p33q4assurance");
            validation.maxSize(p33q4assurance, 50);
            if (!listing.lot.equals("SaaS")) {
                // PaaS, IaaS
                validation.required(p33q5).key("p33q5");
                validation.maxSize(p33q5, 10);
                validation.required(p33q5assurance).key("p33q5assurance");
                validation.maxSize(p33q5assurance, 50);
            }
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
        page.responses.put("p33q1", p33q1);
        page.responses.put("p33q2", p33q2);
        page.responses.put("p33q3", p33q3);
        page.responses.put("p33q4", p33q4);
        page.responses.put("p33q5", p33q5);
        page.responses.put("p33q1assurance", p33q1assurance);
        page.responses.put("p33q2assurance", p33q2assurance);
        page.responses.put("p33q3assurance", p33q3assurance);
        page.responses.put("p33q4assurance", p33q4assurance);
        page.responses.put("p33q5assurance", p33q5assurance);
        page.insert();
        listing.addResponsePage(page, PAGE_ID);
        redirect(listing.nextPageUrl(PAGE_ID, listing.id));
    }

}
