package controllers;

import models.Listing;
import models.Page;
import play.data.validation.Error;

import java.util.List;
import java.util.Map;


public class Page34 extends AuthenticatingController {

    private static final Long PAGE_ID = 34l;

    public static void savePage(Long listingId, String p34q1, String p34q2, String p34q1assurance, String p34q2assurance) {

        Listing listing = Listing.getByListingId(listingId);

        if (listing.serviceSubmitted) {
          redirect(listing.summaryPageUrl());
        }

        // Validate all fields on this page requiring validation
        validation.required(p34q1).key("p34q1");
        validation.maxSize(p34q1, 10);
        validation.required(p34q2).key("p34q2");
        validation.maxSize(p34q2, 10);
        validation.required(p34q1assurance).key("p34q1");
        validation.maxSize(p34q1assurance, 50);
        validation.required(p34q2assurance).key("p34q2");
        validation.maxSize(p34q2assurance, 50);

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
        page.responses.put("p34q1", p34q1);
        page.responses.put("p34q2", p34q2);
        page.responses.put("p34q1assurance", p34q1assurance);
        page.responses.put("p34q2assurance", p34q2assurance);
        page.insert();
        listing.addResponsePage(page, PAGE_ID, supplierDetailsFromCookie.get("supplierEmail"));
        redirect(listing.nextPageUrl(PAGE_ID, listing.id));
    }

}
