package controllers;

import models.Listing;
import models.Page;
import play.data.validation.Error;

import java.util.Map;
import java.util.List;

public class Page22 extends AuthenticatingController {

    private static final Long PAGE_ID = 22l;

    public static void savePage(Long listingId, String p22q1, String p22q2, String p22q3, String p22q4, String p22q5) {

        Listing listing = Listing.getByListingId(listingId);

        // Validate all fields on this page requiring validation
        validation.required(p22q1).key("p22q1");
        validation.maxSize(p22q1, 10);

        validation.required(p22q2).key("p22q2");
        validation.maxSize(p22q2, 10);

        validation.required(p22q3).key("p22q3");
        validation.maxSize(p22q3, 30);

        validation.required(p22q4).key("p22q4");
        validation.maxSize(p22q4, 10);

        validation.required(p22q5).key("p22q5");
        validation.maxSize(p22q5, 10);

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
        Page page = new Page(listingId, PAGE_ID);
        page.addFieldToPageResponse("p22q1", p22q1);
        page.addFieldToPageResponse("p22q2", p22q2);
        page.addFieldToPageResponse("p22q3", p22q3);
        page.addFieldToPageResponse("p22q4", p22q4);
        page.addFieldToPageResponse("p22q5", p22q5);
        page.insert();
        listing.addResponsePage(page, PAGE_ID);
        redirect(listing.nextPageUrl(PAGE_ID, listing.id));
    }
}
