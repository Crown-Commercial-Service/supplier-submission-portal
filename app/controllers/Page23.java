package controllers;

import models.Listing;
import play.data.validation.Error;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class Page23 extends AuthenticatingQuestionPage {

    private static final Long PAGE_ID = 23l;

    public static void savePage(Long listingId, String p23q1, String p23q2, String p23q3, String p23q1assurance, String p23q2assurance, String p23q3assurance) {

        Listing listing = Listing.getByListingId(listingId);

        if (listing.serviceSubmitted) {
          redirect(listing.summaryPageUrl());
        }

        // Validate all fields on this page requiring validation
        validation.required(p23q1).key("p23q1");
        validation.required(p23q1assurance).key("p23q1");
        validation.maxSize(p23q1, 50);
        validation.maxSize(p23q1assurance, 60);
        if(!listing.lot.equals("SaaS")) {
            validation.required(p23q2).key("p23q2");
            validation.required(p23q2assurance).key("p23q2");
            validation.maxSize(p23q2, 50);
            validation.maxSize(p23q2assurance, 60);
            validation.required(p23q3).key("p23q3");
            validation.required(p23q3assurance).key("p23q3");
            validation.maxSize(p23q3, 50);
            validation.maxSize(p23q3assurance, 60);
        }

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
        pageResponses.put("p23q1", p23q1);
        pageResponses.put("p23q2", p23q2);
        pageResponses.put("p23q3", p23q3);
        pageResponses.put("p23q1assurance", p23q1assurance);
        pageResponses.put("p23q2assurance", p23q2assurance);
        pageResponses.put("p23q3assurance", p23q3assurance);
        saveResponseToPage(PAGE_ID, listing, pageResponses);
        if (request.params.get("return_to_summary").equals("yes")) {
          redirect(listing.summaryPageUrl(PAGE_ID));
        } else {
          redirect(listing.nextPageUrl(PAGE_ID, listing.id));
        }
    }

}
