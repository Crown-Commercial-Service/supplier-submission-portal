package controllers;

import models.Listing;
import models.Page;
import play.data.validation.Error;

import java.util.Map;
import java.util.List;

public class Page14 extends AuthenticatingController {

    private static final Long PAGE_ID = 14l;

    public static void savePage(Long listingId, String p14q1, String p14q2, String p14q3) {

        Listing listing = Listing.getByListingId(listingId);

        // Validate all fields on this page requiring validation
        validation.required(p14q1);
        validation.maxSize(p14q1, 10);

        validation.required(p14q2);
        validation.maxSize(p14q2, 100);

        validation.required(p14q3);
        validation.maxSize(p14q3, 100);

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
        page.addFieldToPageResponse("p14q1", p14q1);
        page.addFieldToPageResponse("p14q2", p14q2);
        page.addFieldToPageResponse("p14q3", p14q3);
        page.insert();
        listing.addResponsePage(page, PAGE_ID);
        redirect(listing.nextPageUrl(PAGE_ID, listing.id));
    }
}
