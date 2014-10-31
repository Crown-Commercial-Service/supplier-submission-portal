package controllers;

import models.Listing;
import play.data.validation.Error;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class Page33 extends AuthenticatingQuestionPage {

    private static final Long PAGE_ID = 33l;

    public static void savePage(Long listingId, String p33q1, String p33q2, String p33q3, String p33q4, String p33q5,
                                String p33q1assurance, String p33q2assurance, String p33q3assurance, String p33q4assurance, String p33q5assurance,
                                String return_to_summary) {

        Listing listing = Listing.getByListingId(listingId);

        if(!listing.supplierId.equals(getSupplierId())) {
            Logger.error("Supplier id of listing did not match the logged in supplier.");
            notFound();
        }
        
        if (listing.serviceSubmitted) {
          redirect(listing.summaryPageUrl());
        }

        // Validate all fields on this page requiring validation
        validation.required(p33q1).key("p33q1");
        validation.maxSize(p33q1, 10);
        validation.required(p33q1assurance).key("p33q1");
        validation.maxSize(p33q1assurance, 60);
        if (!listing.lot.equals("SCS")) {
            // SaaS, PaaS, IaaS
            validation.required(p33q2).key("p33q2");
            validation.maxSize(p33q2, 10);
            validation.required(p33q3).key("p33q3");
            validation.maxSize(p33q3, 10);
            validation.required(p33q4).key("p33q4");
            validation.maxSize(p33q4, 10);
            validation.required(p33q2assurance).key("p33q2");
            validation.maxSize(p33q2assurance, 60);
            validation.required(p33q3assurance).key("p33q3");
            validation.maxSize(p33q3assurance, 60);
            validation.required(p33q4assurance).key("p33q4");
            validation.maxSize(p33q4assurance, 60);
            if (!listing.lot.equals("SaaS")) {
                // PaaS, IaaS
                validation.required(p33q5).key("p33q5");
                validation.maxSize(p33q5, 10);
                validation.required(p33q5assurance).key("p33q5");
                validation.maxSize(p33q5assurance, 60);
            }
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
        pageResponses.put("p33q1", p33q1);
        pageResponses.put("p33q2", p33q2);
        pageResponses.put("p33q3", p33q3);
        pageResponses.put("p33q4", p33q4);
        pageResponses.put("p33q5", p33q5);
        pageResponses.put("p33q1assurance", p33q1assurance);
        pageResponses.put("p33q2assurance", p33q2assurance);
        pageResponses.put("p33q3assurance", p33q3assurance);
        pageResponses.put("p33q4assurance", p33q4assurance);
        pageResponses.put("p33q5assurance", p33q5assurance);
        saveResponseToPage(PAGE_ID, listing, pageResponses);
        if (return_to_summary.contains("yes")) {
          redirect(listing.summaryPageUrl(PAGE_ID));
        } else {
          redirect(listing.nextPageUrl(PAGE_ID, listing.id));
        }
    }
}
