package controllers;

import models.Listing;
import play.data.validation.Error;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class Page22 extends AuthenticatingQuestionPage {

    private static final Long PAGE_ID = 22l;

    public static void savePage(Long listingId, String p22q1, String p22q2, String p22q3, String p22q4, String p22q5, String return_to_summary) {

        Listing listing = Listing.getByListingId(listingId);

        if(!listing.supplierId.equals(getSupplierId())) {
            Logger.error("Supplier id of listing did not match the logged in supplier.");
            notFound();
        }
        
        if (listing.serviceSubmitted) {
          redirect(listing.summaryPageUrl());
        }

        // Validate all fields on this page requiring validation
        validation.required(p22q1).key("p22q1");
        validation.maxSize(p22q1, 10);

        validation.required(p22q2).key("p22q2");
        validation.maxSize(p22q2, 10);

        validation.required(p22q3).key("p22q3");
        validation.maxSize(p22q3, 30);

        validation.required(p22q4).key("p22q4");
        validation.maxSize(p22q4, 10);

        validation.required(p22q5).key("p22q5");
        validation.maxSize(p22q5, 10);

        if(validation.hasErrors()) {
            flash.put("body", params.get("body"));
            for(Map.Entry<String, List<Error>> entry : validation.errorsMap().entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue().get(0).message();

                flash.put(key, value);
            }
            
            if (return_to_summary.contains("yes")) {
              redirect(String.format("/page/%d/%d?return_to_summary=yes", PAGE_ID, listing.id));
            } else {
              redirect(String.format("/page/%d/%d", PAGE_ID, listing.id));
            }
        }

        // Save the form data as a Page into the correct page index
        Map<String, String> pageResponses = new HashMap<String, String>();
        pageResponses.put("p22q1", p22q1);
        pageResponses.put("p22q2", p22q2);
        pageResponses.put("p22q3", p22q3);
        pageResponses.put("p22q4", p22q4);
        pageResponses.put("p22q5", p22q5);
        saveResponseToPage(PAGE_ID, listing, pageResponses);
        if (return_to_summary.contains("yes")) {
          redirect(listing.summaryPageUrl(PAGE_ID));
        } else {
          redirect(listing.nextPageUrl(PAGE_ID, listing.id));
        }
    }
}
