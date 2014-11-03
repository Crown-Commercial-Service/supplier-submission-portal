package controllers;

import models.Listing;
import play.Logger;
import play.data.validation.Error;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Page34 extends AuthenticatingQuestionPage {

    private static final Long PAGE_ID = 34l;

    public static void savePage(Long listingId, String p34q1, String p34q2, String p34q1assurance, String p34q2assurance, String return_to_summary) {

        Listing listing = Listing.getByListingId(listingId);

        if(!listing.supplierId.equals(getSupplierId())) {
            Logger.error("Supplier id of listing did not match the logged in supplier. Expected: " + listing.supplierId + ", Found: " + getSupplierId());
            notFound();
        }
        
        if (listing.serviceSubmitted) {
          redirect(listing.summaryPageUrl());
        }

        // Validate all fields on this page requiring validation
        validation.required(p34q1).key("p34q1");
        validation.maxSize(p34q1, 10);
        validation.required(p34q2).key("p34q2");
        validation.maxSize(p34q2, 10);
        validation.required(p34q1assurance).key("p34q1");
        validation.maxSize(p34q1assurance, 60);
        validation.required(p34q2assurance).key("p34q2");
        validation.maxSize(p34q2assurance, 60);

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

        Map<String, String> pageResponses = new HashMap<String, String>();
        pageResponses.put("p34q1", p34q1);
        pageResponses.put("p34q2", p34q2);
        pageResponses.put("p34q1assurance", p34q1assurance);
        pageResponses.put("p34q2assurance", p34q2assurance);
        saveResponseToPage(PAGE_ID, listing, pageResponses);
        if (return_to_summary.contains("yes")) {
          redirect(listing.summaryPageUrl(PAGE_ID));
        } else {
          redirect(listing.nextPageUrl(PAGE_ID, listing.id));
        }
    }

}
