package controllers;

import models.Listing;
import play.data.validation.Error;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Page30 extends AuthenticatingQuestionPage {

    private static final Long PAGE_ID = 30l;

    public static void savePage(Long listingId, String p30q1, String p30q2, String p30q3, String p30q1assurance, String p30q2assurance, String p30q3assurance, String return_to_summary) {

        Listing listing = Listing.getByListingId(listingId);

        if(!listing.supplierId.equals(getSupplierId())) {
            Logger.error("Supplier id of listing did not match the logged in supplier.");
            notFound();
        }
        
        if (listing.serviceSubmitted) {
          redirect(listing.summaryPageUrl());
        }

        // Validate all fields on this page requiring validation
        // ALL LOTS
        validation.required(p30q1).key("p30q1");
        validation.maxSize(p30q1, 10);
        validation.required(p30q1assurance).key("p30q1");
        validation.maxSize(p30q1assurance, 60);

        if (!listing.lot.equals("SCS")) {
            // SaaS, PaaS, IaaS
            validation.required(p30q2).key("p30q2");
            validation.maxSize(p30q2, 10);
            validation.required(p30q3).key("p30q3");
            validation.maxSize(p30q3, 10);

            validation.required(p30q2assurance).key("p30q2");
            validation.maxSize(p30q2assurance, 60);
            validation.required(p30q3assurance).key("p30q3");
            validation.maxSize(p30q3assurance, 60);
        }

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
        pageResponses.put("p30q1", p30q1);
        pageResponses.put("p30q2", p30q2);
        pageResponses.put("p30q3", p30q3);
        pageResponses.put("p30q1assurance", p30q1assurance);
        pageResponses.put("p30q2assurance", p30q2assurance);
        pageResponses.put("p30q3assurance", p30q3assurance);
        saveResponseToPage(PAGE_ID, listing, pageResponses);
        if (return_to_summary.contains("yes")) {
          redirect(listing.summaryPageUrl(PAGE_ID));
        } else {
          redirect(listing.nextPageUrl(PAGE_ID, listing.id));
        }
    }

}
