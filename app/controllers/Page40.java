package controllers;

import models.Listing;
import models.Page;
import play.data.validation.Error;

import java.util.Map;
import java.util.List;

public class Page40 extends AuthenticatingController {

    private static final Long PAGE_ID = 40l;

    public static void savePage(Long listingId, String p40q1, String p40q2, String p40q3,
                                String p40q1assurance, String p40q2assurance, String p40q3assurance) {

        Listing listing = Listing.getByListingId(listingId);

        // Validate all fields on this page requiring validation
        validation.required(p40q1).key("p40q1");
        validation.maxSize(p40q1, 40);
        validation.required(p40q1assurance).key("p40q1assurance");
        validation.maxSize(p40q1assurance, 50);
        if (!listing.lot.equals("SaaS")) {
            validation.required(p40q2).key("p40q2");
            validation.maxSize(p40q2, 10);
            validation.required(p40q2assurance).key("p40q2assurance");
            validation.maxSize(p40q2assurance, 50);
        }
        validation.required(p40q3).key("p40q3");
        validation.maxSize(p40q3, 10);
        validation.required(p40q3assurance).key("p40q3assurance");
        validation.maxSize(p40q3assurance, 50);

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

        Page page = new Page(listingId, PAGE_ID);
        page.responses.put("p40q1", p40q1);
        page.responses.put("p40q2", p40q2);
        page.responses.put("p40q3", p40q3);
        page.responses.put("p40q1assurance", p40q1assurance);
        page.responses.put("p40q2assurance", p40q2assurance);
        page.responses.put("p40q3assurance", p40q3assurance);
        page.insert();
        listing.addResponsePage(page, PAGE_ID);
        redirect(listing.nextPageUrl(PAGE_ID, listing.id));
    }

}
