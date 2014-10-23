package controllers;

import com.google.gson.Gson;
import models.Listing;
import play.data.validation.Error;
import uk.gov.gds.dm.ValidationUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class Page37 extends AuthenticatingQuestionPage {

    private static final Long PAGE_ID = 37l;

    public static void savePage(Long listingId, String p37q1, String p37q1assurance, String[] p37q2, String p37q2assurance, String return_to_summary) {

        Listing listing = Listing.getByListingId(listingId);

        if(!listing.supplierId.equals(getSupplierId())) {
            notFound();
        }
        
        if (listing.serviceSubmitted) {
          redirect(listing.summaryPageUrl());
        }

        // Validate all fields on this page requiring validation
        if (!listing.lot.equals("SaaS")) {
            validation.required(p37q1).key("p37q1");
            validation.maxSize(p37q1, 10);
            validation.required(p37q1assurance).key("p37q1");
            validation.required(p37q2).key("p37q2");
            validation.required(p37q2assurance).key("p37q2");
            validation.isTrue(ValidationUtils.stringArrayValuesAreNotTooLong(p37q2, 50)).key("p37q2").message("Invalid values.");
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
        Gson gson = new Gson();
        pageResponses.put("p37q1", p37q1);
        pageResponses.put("p37q1assurance", p37q1assurance);
        if (p37q2 != null) {
            pageResponses.put("p37q2", gson.toJson(p37q2));
            pageResponses.put("p37q2assurance", p37q2assurance);
        }
        else {
            pageResponses.put("p37q2", null);
            pageResponses.put("p37q2assurance", null);
        }

        saveResponseToPage(PAGE_ID, listing, pageResponses);
        if (return_to_summary.contains("yes")) {
          redirect(listing.summaryPageUrl(PAGE_ID));
        } else {
          redirect(listing.nextPageUrl(PAGE_ID, listing.id));
        }
    }
}
