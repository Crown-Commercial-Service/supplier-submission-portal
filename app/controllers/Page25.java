package controllers;

import models.Listing;
import models.Page;
import play.data.validation.Error;

import java.util.Map;
import java.util.List;

public class Page25 extends AuthenticatingController {

    private static final Long PAGE_ID = 25l;

    public static void savePage(Long listingId, String p25q1, String p25q2, String p25q2assurance, String p25q3, String p25q3assurance, String p25q4, String p25q4assurance) {

        Listing listing = Listing.getByListingId(listingId);

        if (listing.serviceSubmitted) {
          redirect(listing.summaryPageUrl());
        }

        // Validate all fields on this page requiring validation
        validation.required(p25q1).key("p25q1");
        validation.maxSize(p25q1, 30);

        validation.required(p25q2).key("p25q2");
        validation.maxSize(p25q2, 30);
        validation.required(p25q2assurance).key("p25q2");
        validation.maxSize(p25q2assurance, 50);

        validation.required(p25q3).key("p25q3");
        validation.maxSize(p25q3, 10);
        validation.required(p25q3assurance).key("p25q3");
        validation.maxSize(p25q3assurance, 50);

        validation.required(p25q4).key("p25q4");
        validation.maxSize(p25q4, 10);
        validation.required(p25q4assurance).key("p25q4");
        validation.maxSize(p25q4assurance, 50);

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
        page.responses.put("p25q1", p25q1);
        page.responses.put("p25q2", p25q2);
        page.responses.put("p25q3", p25q3);
        page.responses.put("p25q4", p25q4);
        page.responses.put("p25q2assurance", p25q2assurance);
        page.responses.put("p25q3assurance", p25q3assurance);
        page.responses.put("p25q4assurance", p25q4assurance);
        page.insert();
        listing.addResponsePage(page, PAGE_ID, supplierDetailsFromCookie.get("supplierEmail"));
        redirect(listing.nextPageUrl(PAGE_ID, listing.id));

    }
}
