package controllers;

import models.Listing;
import play.Logger;
import play.data.validation.Error;
import uk.gov.gds.dm.ValidationUtils;
import uk.gov.gds.dm.Fixtures;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Page4 extends AuthenticatingQuestionPage {

    private static final Long PAGE_ID = 4l;

    public static void savePage(Long listingId, String p4q1, String p4q2, String return_to_summary) {

        Listing listing = Listing.getByListingId(listingId);

        if(!listing.supplierId.equals(getSupplierId())) {
            Logger.error("Supplier id of listing did not match the logged in supplier. Expected: " + listing.supplierId + ", Found: " + getSupplierId());
            notFound();
        }
        
        if (listing.serviceSubmitted) {
          redirect(listing.summaryPageUrl());
        }

        // Validate all fields on this page requiring validation
        validation.required(p4q1);
        validation.maxSize(p4q1, 100);

        validation.required(p4q2);
        validation.isTrue(ValidationUtils.isWordCountLessThan(p4q2, 50)).key("p4q2").message("Too many words");
        validation.maxSize(p4q2, 500);

        if(validation.hasErrors()) {
            flash.put("body", params.get("body"));
            for(Map.Entry<String, List<Error>> entry : validation.errorsMap().entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue().get(0).message();

                flash.put(key, Fixtures.getErrorMessage(key, value));
            }

            if (return_to_summary.contains("yes")) {
              redirect(String.format("/page/%d/%d?return_to_summary=yes", PAGE_ID, listing.id));
            } else {
              redirect(String.format("/page/%d/%d", PAGE_ID, listing.id));
            }
        }

        // Save the form data as a Page into the correct page index
        Map<String, String> pageResponses = new HashMap<String, String>();
        pageResponses.put("p4q1", p4q1);
        pageResponses.put("p4q2", p4q2);
        listing.title = p4q1;
        saveResponseToPage(PAGE_ID, listing, pageResponses);

        if (return_to_summary.contains("yes")) {
          redirect(listing.summaryPageUrl(PAGE_ID));
        } else {
          redirect(listing.nextPageUrl(PAGE_ID, listing.id));
        }
    }
}
