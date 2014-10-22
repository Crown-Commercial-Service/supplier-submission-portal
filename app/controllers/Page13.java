package controllers;

import models.Listing;
import models.Page;
import play.data.validation.Error;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class Page13 extends AuthenticatingQuestionPage {

    private static final Long PAGE_ID = 13l;

    public static void savePage(Long listingId, String p13q1, String p13q2, String p13q3) {

        Listing listing = Listing.getByListingId(listingId);

        if (listing.serviceSubmitted) {
          redirect(listing.summaryPageUrl());
        }

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
            if (request.params.get("return_to_summary").equals("yes")) {
              redirect(String.format("/page/%d/%d?return_to_summary=yes", PAGE_ID, listing.id));
            } else {
              redirect(String.format("/page/%d/%d", PAGE_ID, listing.id));
            }
        }

        // Save the form data as a Page into the correct page index
        Map<String, String> pageResponses = new HashMap<String, String>();
        pageResponses.put("p13q1", p13q1);
        pageResponses.put("p13q2", p13q2);
        pageResponses.put("p13q3", p13q3);
        saveResponseToPage(PAGE_ID, listing, pageResponses);
        if (request.params.get("return_to_summary").equals("yes")) {
          redirect(listing.summaryPageUrl(PAGE_ID));
        } else {
          redirect(listing.nextPageUrl(PAGE_ID, listing.id));
        }
     }
}
