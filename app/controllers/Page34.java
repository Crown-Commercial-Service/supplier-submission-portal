package controllers;

import models.Listing;
import models.Page;
import play.data.validation.Error;

import java.util.List;
import java.util.Map;


public class Page34 extends AuthenticatingController {

    // TODO: ONly half questions here now - rest to p35
    private static final Long PAGE_ID = 34l;

    public static void savePage(Long listingId, String p34q1, String p34q2, String p34q3, String p34q4, String p34q5,
                                String p34q1assurance, String p34q2assurance, String p34q3assurance, String p34q4assurance, String p34q5assurance) {

        Listing listing = Listing.getByListingId(listingId);

        // Validate all fields on this page requiring validation
        validation.required(p34q1).key("p34q1");
        validation.maxSize(p34q1, 10);
        validation.required(p34q2).key("p34q2");
        validation.maxSize(p34q2, 10);
        validation.required(p34q3).key("p34q3");
        validation.maxSize(p34q3, 10);
        validation.required(p34q4).key("p34q4");
        validation.maxSize(p34q4, 10);
        validation.required(p34q1assurance).key("p34q1assurance");
        validation.maxSize(p34q1assurance, 50);
        validation.required(p34q2assurance).key("p34q2assurance");
        validation.maxSize(p34q2assurance, 50);
        validation.required(p34q3assurance).key("p34q3assurance");
        validation.maxSize(p34q3assurance, 50);
        validation.required(p34q4assurance).key("p34q4assurance");
        validation.maxSize(p34q4assurance, 50);
        if (!listing.lot.equals("SaaS")) {
            validation.required(p34q5).key("p34q5");
            validation.maxSize(p34q5, 10);
            validation.required(p34q5assurance).key("p34q5assurance");
            validation.maxSize(p34q5assurance, 50);
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
        page.responses.put("p34q1", p34q1);
        page.responses.put("p34q2", p34q2);
        page.responses.put("p34q3", p34q3);
        page.responses.put("p34q4", p34q4);
        page.responses.put("p34q5", p34q5);
        page.insert();
        listing.addResponsePage(page, PAGE_ID);
        redirect(listing.nextPageUrl(PAGE_ID, listing.id));
    }

}
