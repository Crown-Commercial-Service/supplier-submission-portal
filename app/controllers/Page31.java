package controllers;

import models.Listing;
import models.Page;
import play.data.validation.Error;
import java.util.Map;
import java.util.List;


public class Page31 extends AuthenticatingController {

    private static final Long PAGE_ID = 31l;

    public static void savePage(Long listingId, String p31q1, String p31q2, String p31q3, String p31q4, String p31q5,
                                String p31q1assurance, String p31q2assurance, String p31q3assurance, String p31q4assurance, String p31q5assurance) {

        Listing listing = Listing.getByListingId(listingId);

        // Validate all fields on this page requiring validation
        validation.required(p31q1).key("p31q1");
        validation.maxSize(p31q1, 10);
        validation.required(p31q2).key("p31q2");
        validation.maxSize(p31q2, 10);
        validation.required(p31q3).key("p31q3");
        validation.maxSize(p31q3, 10);
        validation.required(p31q4).key("p31q4");
        validation.maxSize(p31q4, 10);
        validation.required(p31q1assurance).key("p31q1assurance");
        validation.maxSize(p31q1assurance, 50);
        validation.required(p31q2assurance).key("p31q2assurance");
        validation.maxSize(p31q2assurance, 50);
        validation.required(p31q3assurance).key("p31q3assurance");
        validation.maxSize(p31q3assurance, 50);
        validation.required(p31q4assurance).key("p31q4assurance");
        validation.maxSize(p31q4assurance, 50);
        if (!listing.lot.equals("SaaS")) {
            validation.required(p31q5).key("p31q5");
            validation.maxSize(p31q5, 10);
            validation.required(p31q5assurance).key("p31q5assurance");
            validation.maxSize(p31q5assurance, 50);
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
        page.responses.put("p31q1", p31q1);
        page.responses.put("p31q2", p31q2);
        page.responses.put("p31q3", p31q3);
        page.responses.put("p31q4", p31q4);
        page.responses.put("p31q5", p31q5);
        page.insert();
        listing.addResponsePage(page, PAGE_ID);
        redirect(listing.nextPageUrl(PAGE_ID, listing.id));
    }

}
