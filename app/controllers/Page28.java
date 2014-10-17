package controllers;

import models.Listing;
import models.Page;
import play.data.validation.Error;

import java.util.List;
import java.util.Map;

public class Page28 extends AuthenticatingController {
    
    private static final Long PAGE_ID = 28l;

    public static void savePage(Long listingId, String p28q1, String p28q2, String p28q3, String p28q4, String p28q5,
                                String p28q1assurance, String p28q2assurance, String p28q3assurance, String p28q4assurance, String p28q5assurance) {

        Listing listing = Listing.getByListingId(listingId);

        // Validate all fields on this page requiring validation
        validation.required(p28q1).key("p28q1");
        validation.maxSize(p28q1, 10);
        validation.required(p28q2).key("p28q2");
        validation.maxSize(p28q2, 10);
        validation.required(p28q3).key("p28q3");
        validation.maxSize(p28q3, 10);
        validation.required(p28q4).key("p28q4");
        validation.maxSize(p28q4, 10);
        validation.required(p28q5).key("p28q5");
        validation.maxSize(p28q5, 10);

        validation.required(p28q1assurance).key("p28q1");
        validation.maxSize(p28q1assurance, 50);
        validation.required(p28q2assurance).key("p28q2");
        validation.maxSize(p28q2assurance, 50);
        validation.required(p28q3assurance).key("p28q3");
        validation.maxSize(p28q3assurance, 50);
        validation.required(p28q4assurance).key("p28q4");
        validation.maxSize(p28q4assurance, 50);
        validation.required(p28q5assurance).key("p28q5");
        validation.maxSize(p28q5assurance, 50);


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
        page.responses.put("p28q1", p28q1);
        page.responses.put("p28q2", p28q2);
        page.responses.put("p28q3", p28q3);
        page.responses.put("p28q4", p28q4);
        page.responses.put("p28q5", p28q5);
        page.responses.put("p28q1assurance", p28q1assurance);
        page.responses.put("p28q2assurance", p28q2assurance);
        page.responses.put("p28q3assurance", p28q3assurance);
        page.responses.put("p28q4assurance", p28q4assurance);
        page.responses.put("p28q5assurance", p28q5assurance);

        page.insert();
        listing.addResponsePage(page, PAGE_ID);
        redirect(listing.nextPageUrl(PAGE_ID, listing.id));
    }

}
