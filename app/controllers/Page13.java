package controllers;

import models.Listing;
import models.Page;
import play.data.validation.Error;

import java.util.Map;
import java.util.List;

public class Page13 extends AuthenticatingController {

    private static final Long PAGE_ID = 13l;

    public static void savePage(Long listingId, String p13q1, String p13q2, String p13q3) {

        Listing listing = Listing.getByListingId(listingId);

        // Validate all fields on this page requiring validation
        validation.required(p13q1).key("p13q1");
        validation.maxSize(p13q1, 10);

        validation.required(p13q2).key("p13q2");
        validation.maxSize(p13q2, 10);

        validation.required(p13q3).key("p13q3");
        validation.maxSize(p13q3, 10);

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
        page.addFieldToPageResponse("p13q1", p13q1);
        page.addFieldToPageResponse("p13q2", p13q2);
        page.addFieldToPageResponse("p13q3", p13q3);
        page.insert();
        listing.addResponsePage(page, PAGE_ID);
        redirect(listing.nextPageUrl(PAGE_ID, listing.id));
    }
}
