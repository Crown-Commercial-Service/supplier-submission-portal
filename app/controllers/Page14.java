package controllers;

import models.Listing;
import play.Logger;
import play.data.validation.Error;
import uk.gov.gds.dm.Fixtures;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class Page14 extends AuthenticatingQuestionPage {

    private static final Long PAGE_ID = 14l;

    public static void savePage(Long listingId, String p14q1, String p14q2, String p14q3, String return_to_summary) {

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
        validation.required(p14q1);
        validation.maxSize(p14q1, 10);

        validation.required(p14q2);
        validation.maxSize(p14q2, 100).message("Your answer must be less than 100 characters in length.");
        validation.isTrue(!p14q2.startsWith("[")).key("p14q2").message("The character '[' is not allowed here.");

        validation.required(p14q3);
        validation.maxSize(p14q3, 100).message("Your answer must be less than 100 characters in length.");
        validation.isTrue(!p14q3.startsWith("[")).key("p14q3").message("The character '[' is not allowed here.");

        if(validation.hasErrors()) {
            flash.put("body", params.get("body"));
            for(Map.Entry<String, List<Error>> entry : validation.errorsMap().entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue().get(0).message();

                flash.put(key, Fixtures.getErrorMessage(key, value));
            }
            Logger.info(String.format("Validation errors: %s; reloading page.", validation.errorsMap().toString()));
            if (return_to_summary.contains("yes")) {
              redirect(String.format("/page/%d/%d?return_to_summary=yes", PAGE_ID, listing.id));
            } else {
              redirect(String.format("/page/%d/%d", PAGE_ID, listing.id));
            }
        }

        // Save the form data as a Page into the correct page index
        Map<String, String> pageResponses = new HashMap<String, String>();
        pageResponses.put("p14q1", p14q1);
        pageResponses.put("p14q2", p14q2);
        pageResponses.put("p14q3", p14q3);
        saveResponseToPage(PAGE_ID, listing, pageResponses);
        if (return_to_summary.contains("yes")) {
          redirect(listing.summaryPageUrl(PAGE_ID));
        } else {
          redirect(listing.nextPageUrl(PAGE_ID, listing.id));
        }
    }
}
