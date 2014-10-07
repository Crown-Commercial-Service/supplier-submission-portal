package controllers;

import models.Listing;
import models.Page;
import play.data.validation.Error;

import java.util.Map;
import java.util.List;

public class Page36 extends AuthenticatingController {

    private static final Long PAGE_ID = 36l;

    public static void savePage(Long listingId, String p36q1, String p36q2, String p36q3,
                                String p36q1assurance, String p36q2assurance, String p36q3assurance) {

        Listing listing = Listing.getByListingId(listingId);

        // Validate all fields on this page requiring validation
        validation.required(p36q1).key("p36q1");
        validation.maxSize(p36q1, 40);
        validation.required(p36q1assurance).key("p36q1assurance");
        validation.maxSize(p36q1assurance, 50);
        if (!listing.lot.equals("SaaS")) {
            validation.required(p36q2).key("p36q2");
            validation.maxSize(p36q2, 10);
            validation.required(p36q2assurance).key("p36q2assurance");
            validation.maxSize(p36q2assurance, 50);
        }
        validation.required(p36q3).key("p36q3");
        validation.maxSize(p36q3, 10);
        validation.required(p36q3assurance).key("p36q3assurance");
        validation.maxSize(p36q3assurance, 50);

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
        page.responses.put("p36q1", p36q1);
        page.responses.put("p36q2", p36q2);
        page.responses.put("p36q3", p36q3);
        page.insert();
        listing.addResponsePage(page, PAGE_ID);
        redirect(listing.nextPageUrl(PAGE_ID, listing.id));
    }

}
