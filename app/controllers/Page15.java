package controllers;

import models.Listing;
import models.Page;
import play.data.validation.Error;

import java.util.Map;
import java.util.List;

public class Page15 extends AuthenticatingController {

    private static final Long PAGE_ID = 15l;

    public static void savePage(Long listingId, String p15q1) {

        Listing listing = Listing.getByListingId(listingId);

        // Validate all fields on this page requiring validation
        validation.required(p15q1);
        validation.maxSize(p15q1, 10);

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

        if (listing.serviceSubmitted) {
          redirect(listing.summaryPageUrl());
        } else {
          // Save the form data as a Page into the correct page index
          Page page = new Page(listingId, PAGE_ID);
          page.responses.put("p15q1", p15q1);
          page.insert();
          listing.addResponsePage(page, PAGE_ID, supplierDetailsFromCookie.get("supplierEmail"));
          redirect(listing.nextPageUrl(PAGE_ID, listing.id));
        }
    }
}
