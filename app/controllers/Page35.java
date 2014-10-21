package controllers;

import models.Listing;
import models.Page;
import play.data.validation.Error;
import java.util.Map;
import java.util.List;


public class Page35 extends AuthenticatingController {

    private static final Long PAGE_ID = 35l;

    public static void savePage(Long listingId, String p35q1, String p35q2, String p35q3, String p35q1assurance, String p35q2assurance, String p35q3assurance) {

        Listing listing = Listing.getByListingId(listingId);

        // Validate all fields on this page requiring validation
        validation.required(p35q1).key("p35q1");
        validation.maxSize(p35q1, 10);
        validation.required(p35q2).key("p35q2");
        validation.maxSize(p35q2, 10);

        validation.required(p35q1assurance).key("p35q1");
        validation.maxSize(p35q1assurance, 50);
        validation.required(p35q2assurance).key("p35q2");
        validation.maxSize(p35q2assurance, 50);

        if (!listing.lot.equals("SaaS")) {
            validation.required(p35q3).key("p35q3");
            validation.maxSize(p35q3, 10);
            validation.required(p35q3assurance).key("p35q3");
            validation.maxSize(p35q3assurance, 50);
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

        if (listing.serviceSubmitted) {
          redirect(listing.summaryPageUrl());
        } else {
          Page page = new Page(listingId, PAGE_ID);
          page.responses.put("p35q1", p35q1);
          page.responses.put("p35q2", p35q2);
          page.responses.put("p35q3", p35q3);
          page.responses.put("p35q1assurance", p35q1assurance);
          page.responses.put("p35q2assurance", p35q2assurance);
          page.responses.put("p35q3assurance", p35q3assurance);
          page.insert();
          listing.addResponsePage(page, PAGE_ID, supplierDetailsFromCookie.get("supplierEmail"));
          redirect(listing.nextPageUrl(PAGE_ID, listing.id));
        }
    }

}
