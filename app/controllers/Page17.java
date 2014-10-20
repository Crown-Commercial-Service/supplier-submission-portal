package controllers;

import models.Listing;
import models.Page;
import play.data.validation.Error;

import java.util.Map;
import java.util.List;

public class Page17 extends AuthenticatingController {

    private static final Long PAGE_ID = 17l;

    public static void savePage(Long listingId, String p17q1, String p17q2) {

        Listing listing = Listing.getByListingId(listingId);

        // Validate all fields on this page requiring validation
        validation.required(p17q1).key("p17q1");
        validation.maxSize(p17q1, 10);

        if(p17q1 != null && p17q1.toLowerCase().equals("yes")){
            validation.required(p17q2).key("p17q2");
            validation.maxSize(p17q2, 100);
        }

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
        page.responses.put("p17q1", p17q1);
        page.responses.put("p17q2", p17q2);
        page.insert();
        listing.addResponsePage(page, PAGE_ID, supplierDetailsFromCookie.get("supplierEmail"));
        redirect(listing.nextPageUrl(PAGE_ID, listing.id));
    }
}
