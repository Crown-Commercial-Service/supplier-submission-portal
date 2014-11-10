package controllers;

import models.Listing;
import play.Logger;
import play.data.validation.Error;

import java.util.HashMap;
import java.util.Map;
import java.util.List;


public class Page35 extends AuthenticatingQuestionPage {

    private static final Long PAGE_ID = 35l;

    public static void savePage(Long listingId, String p35q1, String p35q2, String p35q3, String p35q1assurance, String p35q2assurance, String p35q3assurance, String return_to_summary) {

        Listing listing = Listing.getByListingId(listingId);

        notFoundIfNull(listing);

        if(!listing.supplierId.equals(getSupplierId())) {
            Logger.error("Supplier id of listing did not match the logged in supplier. Expected: " + listing.supplierId + ", Found: " + getSupplierId());
            notFound();
        }
        
        if (listing.serviceSubmitted) {
            Logger.info("Trying to edit a submitted service; redirect to summary page.");
            redirect(listing.summaryPageUrl());
        }

        // Validate all fields on this page requiring validation
        validation.required(p35q1).key("p35q1");
        validation.maxSize(p35q1, 10);
        validation.required(p35q2).key("p35q2");
        validation.maxSize(p35q2, 10);

        validation.required(p35q1assurance).key("p35q1");
        validation.maxSize(p35q1assurance, 60);
        validation.required(p35q2assurance).key("p35q2");
        validation.maxSize(p35q2assurance, 60);

        if (!listing.lot.equals("SaaS")) {
            validation.required(p35q3).key("p35q3");
            validation.maxSize(p35q3, 10);
            validation.required(p35q3assurance).key("p35q3");
            validation.maxSize(p35q3assurance, 60);
        }

        if(validation.hasErrors()) {
            flash.put("body", params.get("body"));
            for(Map.Entry<String, List<Error>> entry : validation.errorsMap().entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue().get(0).message();

                flash.put(key, value);
            }
            Logger.info(String.format("Validation errors: %s; reloading page.", validation.errorsMap().toString()));
            if (return_to_summary.contains("yes")) {
              redirect(String.format("/page/%d/%d?return_to_summary=yes", PAGE_ID, listing.id));
            } else {
              redirect(String.format("/page/%d/%d", PAGE_ID, listing.id));
            }
        }

        Map<String, String> pageResponses = new HashMap<String, String>();
        pageResponses.put("p35q1", p35q1);
        pageResponses.put("p35q2", p35q2);
        pageResponses.put("p35q3", p35q3);
        pageResponses.put("p35q1assurance", p35q1assurance);
        pageResponses.put("p35q2assurance", p35q2assurance);
        pageResponses.put("p35q3assurance", p35q3assurance);
        saveResponseToPage(PAGE_ID, listing, pageResponses);
        if (return_to_summary.contains("yes")) {
          redirect(listing.summaryPageUrl(PAGE_ID));
        } else {
          redirect(listing.nextPageUrl(PAGE_ID, listing.id));
        }
    }
}
