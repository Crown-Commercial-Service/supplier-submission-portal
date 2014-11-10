package controllers;

import models.Listing;
import play.Logger;
import play.data.validation.Error;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Page28 extends AuthenticatingQuestionPage {

    private static final Long PAGE_ID = 28l;

    public static void savePage(Long listingId, String p28q1, String p28q2, String p28q3, String p28q4, String p28q5,
                                String p28q1assurance, String p28q2assurance, String p28q3assurance, String p28q4assurance, String p28q5assurance,
                                String return_to_summary) {

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
        validation.required(p28q1).key("p28q1");
        validation.maxSize(p28q1, 10);
        validation.required(p28q2).key("p28q2");
        validation.maxSize(p28q2, 10);
        validation.required(p28q3).key("p28q3");
        validation.maxSize(p28q3, 10);
        validation.required(p28q4).key("p28q4");
        validation.maxSize(p28q4, 10);
        validation.required(p28q5).key("p28q5");
        validation.maxSize(p28q5, 10);

        validation.required(p28q1assurance).key("p28q1");
        validation.maxSize(p28q1assurance, 60);
        validation.required(p28q2assurance).key("p28q2");
        validation.maxSize(p28q2assurance, 60);
        validation.required(p28q3assurance).key("p28q3");
        validation.maxSize(p28q3assurance, 60);
        validation.required(p28q4assurance).key("p28q4");
        validation.maxSize(p28q4assurance, 60);
        validation.required(p28q5assurance).key("p28q5");
        validation.maxSize(p28q5assurance, 60);


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
        pageResponses.put("p28q1", p28q1);
        pageResponses.put("p28q2", p28q2);
        pageResponses.put("p28q3", p28q3);
        pageResponses.put("p28q4", p28q4);
        pageResponses.put("p28q5", p28q5);
        pageResponses.put("p28q1assurance", p28q1assurance);
        pageResponses.put("p28q2assurance", p28q2assurance);
        pageResponses.put("p28q3assurance", p28q3assurance);
        pageResponses.put("p28q4assurance", p28q4assurance);
        pageResponses.put("p28q5assurance", p28q5assurance);
        saveResponseToPage(PAGE_ID, listing, pageResponses);
        if (return_to_summary.contains("yes")) {
          redirect(listing.summaryPageUrl(PAGE_ID));
        } else {
          redirect(listing.nextPageUrl(PAGE_ID, listing.id));
        }
    }
}
