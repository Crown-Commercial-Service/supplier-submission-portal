package controllers;

import com.google.gson.Gson;
import models.Listing;
import play.Logger;
import play.data.validation.Error;
import uk.gov.gds.dm.ValidationUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class Page19 extends AuthenticatingQuestionPage {

    private static final Long PAGE_ID = 19l;

    public static void savePage(Long listingId, String[] p19q1, String p19q2, String[] p19q3, String return_to_summary) {

        Listing listing = Listing.getByListingId(listingId);

        if(!listing.supplierId.equals(getSupplierId())) {
            Logger.error("Supplier id of listing did not match the logged in supplier.");
            notFound();
        }
        
        if (listing.serviceSubmitted) {
          redirect(listing.summaryPageUrl());
        }

        // Validate all fields on this page requiring validation
        validation.required(p19q1).key("p19q1");
        validation.isTrue(ValidationUtils.stringArrayValuesAreNotTooLong(p19q1, 30)).key("p19q1").message("Invalid values");

        validation.required(p19q2).key("p19q2");
        validation.maxSize(p19q2, 10);

        validation.required(p19q3).key("p19q3");
        validation.isTrue(ValidationUtils.stringArrayValuesAreNotTooLong(p19q3, 30)).key("p19q3").message("Invalid values");

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
        Gson gson = new Gson();
        pageResponses.put("p19q1", gson.toJson(p19q1));
        pageResponses.put("p19q2", p19q2);
        pageResponses.put("p19q3", gson.toJson(p19q3));
        saveResponseToPage(PAGE_ID, listing, pageResponses);
        if (return_to_summary.contains("yes")) {
          redirect(listing.summaryPageUrl(PAGE_ID));
        } else {
          redirect(listing.nextPageUrl(PAGE_ID, listing.id));
        }
    }
}
