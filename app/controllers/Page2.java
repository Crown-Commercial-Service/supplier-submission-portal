package controllers;

import com.google.gson.Gson;
import models.Listing;
import play.data.validation.Error;
import uk.gov.gds.dm.ValidationUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Page2 extends AuthenticatingQuestionPage {

    private static final Long PAGE_ID = 2l;

    public static void savePage(Long listingId, String[] p2q1, String return_to_summary) {

        Listing listing = Listing.getByListingId(listingId);

        if (listing.serviceSubmitted) {
          redirect(listing.summaryPageUrl());
        }

        // Validate all fields on this page requiring validation
        validation.required(p2q1);
        validation.isTrue(ValidationUtils.stringArrayValuesAreNotTooLong(p2q1, 50)).key("p2q1").message("Invalid values");

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
        pageResponses.put("p2q1", gson.toJson(p2q1));
        saveResponseToPage(PAGE_ID, listing, pageResponses);
        if (return_to_summary.contains("yes")) {
          redirect(listing.summaryPageUrl(PAGE_ID));
        } else {
          redirect(listing.nextPageUrl(PAGE_ID, listing.id));
        }
  }
}
