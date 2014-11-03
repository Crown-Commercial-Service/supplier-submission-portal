package controllers;

import com.google.gson.Gson;
import models.Listing;
import play.Logger;
import play.data.validation.Error;
import uk.gov.gds.dm.ValidationUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.List;


public class Page18 extends AuthenticatingQuestionPage {

    private static final Long PAGE_ID = 18l;

    public static void savePage(Long listingId, String[] p18q1, String return_to_summary) {

        Listing listing = Listing.getByListingId(listingId);

        if(!listing.supplierId.equals(getSupplierId())) {
            Logger.error("Supplier id of listing did not match the logged in supplier. Expected: " + listing.supplierId + ", Found: " + getSupplierId());
            notFound();
        }
        
        if (listing.serviceSubmitted) {
            Logger.info("Trying to edit a submitted service; redirect to summary page.");
            redirect(listing.summaryPageUrl());
        }

        // Validate all fields on this page requiring validation
        validation.required(p18q1).key("p18q1");
        validation.isTrue(ValidationUtils.stringArrayValuesAreNotTooLong(p18q1, 50)).key("p18q1").message("Invalid values");

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

        // Save the form data as a Page into the correct page index
        Map<String, String> pageResponses = new HashMap<String, String>();
        Gson gson = new Gson();
        pageResponses.put("p18q1", gson.toJson(p18q1));
        saveResponseToPage(PAGE_ID, listing, pageResponses);
        if (return_to_summary.contains("yes")) {
          redirect(listing.summaryPageUrl(PAGE_ID));
        } else {
          redirect(listing.nextPageUrl(PAGE_ID, listing.id));
        }
    }
}
