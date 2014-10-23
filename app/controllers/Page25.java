package controllers;

import models.Listing;
import play.data.validation.Error;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class Page25 extends AuthenticatingQuestionPage {

    private static final Long PAGE_ID = 25l;

    public static void savePage(Long listingId, String p25q1, String p25q2, String p25q2assurance, String p25q3, String p25q3assurance, String p25q4, String p25q4assurance, String return_to_summary) {

        Listing listing = Listing.getByListingId(listingId);

        if(!listing.supplierId.equals(getSupplierId())) {
            notFound();
        }
        
        if (listing.serviceSubmitted) {
          redirect(listing.summaryPageUrl());
        }

        // Validate all fields on this page requiring validation
        validation.required(p25q1).key("p25q1");
        validation.maxSize(p25q1, 30);

        validation.required(p25q2).key("p25q2");
        validation.maxSize(p25q2, 30);
        validation.required(p25q2assurance).key("p25q2");
        validation.maxSize(p25q2assurance, 60);

        validation.required(p25q3).key("p25q3");
        validation.maxSize(p25q3, 10);
        validation.required(p25q3assurance).key("p25q3");
        validation.maxSize(p25q3assurance, 60);

        validation.required(p25q4).key("p25q4");
        validation.maxSize(p25q4, 10);
        validation.required(p25q4assurance).key("p25q4");
        validation.maxSize(p25q4assurance, 60);

        if(validation.hasErrors()) {
            flash.put("body", params.get("body"));
            for(Map.Entry<String, List<Error>> entry : validation.errorsMap().entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue().get(0).message();

                flash.put(key, value);
            }
            System.out.println(flash);
            if (return_to_summary.contains("yes")) {
              redirect(String.format("/page/%d/%d?return_to_summary=yes", PAGE_ID, listing.id));
            } else {
              redirect(String.format("/page/%d/%d", PAGE_ID, listing.id));
            }
        }

        // Save the form data as a Page into the correct page index
        Map<String, String> pageResponses = new HashMap<String, String>();
        pageResponses.put("p25q1", p25q1);
        pageResponses.put("p25q2", p25q2);
        pageResponses.put("p25q3", p25q3);
        pageResponses.put("p25q4", p25q4);
        pageResponses.put("p25q2assurance", p25q2assurance);
        pageResponses.put("p25q3assurance", p25q3assurance);
        pageResponses.put("p25q4assurance", p25q4assurance);
        saveResponseToPage(PAGE_ID, listing, pageResponses);
        if (return_to_summary.contains("yes")) {
          redirect(listing.summaryPageUrl(PAGE_ID));
        } else {
          redirect(listing.nextPageUrl(PAGE_ID, listing.id));
        }
    }
}
