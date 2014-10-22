package controllers;

import models.Listing;
import models.Page;
import play.data.validation.Error;

import java.util.List;
import java.util.Map;

public class Page29 extends AuthenticatingController {

    private static final Long PAGE_ID = 29l;

    public static void savePage(Long listingId, String p29q1, String p29q1assurance) {

        Listing listing = Listing.getByListingId(listingId);

        if (listing.serviceSubmitted) {
          redirect(listing.summaryPageUrl());
        }

        // Validate all fields on this page requiring validation
        validation.required(p29q1).key("p29q1");
        validation.maxSize(p29q1, 10);
        validation.required(p29q1assurance).key("p29q1");
        validation.maxSize(p29q1assurance, 50);

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
        page.responses.put("p29q1", p29q1);
        page.responses.put("p29q1assurance", p29q1assurance);
        page.insert();
        listing.addResponsePage(page, PAGE_ID, supplierDetailsFromCookie.get("supplierEmail"));
        redirect(listing.nextPageUrl(PAGE_ID, listing.id));
    }

}
