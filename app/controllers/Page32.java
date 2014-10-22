package controllers;

import models.Listing;
import models.Page;
import play.data.validation.Error;

import java.util.Map;
import java.util.List;

public class Page32 extends AuthenticatingController {

    private static final Long PAGE_ID = 32l;

    public static void savePage(Long listingId, String p32q1, String p32q2, String p32q3, String p32q1assurance, String p32q2assurance, String p32q3assurance) {

        Listing listing = Listing.getByListingId(listingId);

        if (listing.serviceSubmitted) {
          redirect(listing.summaryPageUrl());
        }

        // Validate all fields on this page requiring validation
        validation.required(p32q1).key("p32q1");
        validation.maxSize(p32q1, 10);
        validation.required(p32q2).key("p32q2");
        validation.maxSize(p32q2, 10);
        validation.required(p32q3).key("p32q3");
        validation.maxSize(p32q3, 10);
        validation.required(p32q1assurance).key("p32q1");
        validation.maxSize(p32q1assurance, 50);
        validation.required(p32q2assurance).key("p32q2");
        validation.maxSize(p32q2assurance, 50);
        validation.required(p32q3assurance).key("p32q3");
        validation.maxSize(p32q3assurance, 50);

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
        page.responses.put("p32q1", p32q1);
        page.responses.put("p32q2", p32q2);
        page.responses.put("p32q3", p32q3);
        page.responses.put("p32q1assurance", p32q1assurance);
        page.responses.put("p32q2assurance", p32q2assurance);
        page.responses.put("p32q3assurance", p32q3assurance);
        page.insert();
        listing.addResponsePage(page, PAGE_ID, supplierDetailsFromCookie.get("supplierEmail"));
        redirect(listing.nextPageUrl(PAGE_ID, listing.id));

    }
}
