package controllers;

import models.Listing;
import play.data.validation.Error;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class Page26 extends AuthenticatingQuestionPage {

    private static final Long PAGE_ID = 26l;

    public static void savePage(Long listingId, String p26q1, String p26q1assurance) {

        Listing listing = Listing.getByListingId(listingId);

        if(!listing.supplierId.equals(getSupplierId())) {
            notFound();
        }
        
        if (listing.serviceSubmitted) {
          redirect(listing.summaryPageUrl());
        }

        // Validate all fields on this page requiring validation
        validation.required(p26q1).key("p26q1");
        validation.maxSize(p26q1, 10);
        validation.required(p26q1assurance).key("p26q1");
        validation.maxSize(p26q1assurance, 50);

        if(validation.hasErrors()) {
            flash.put("body", params.get("body"));
            for(Map.Entry<String, List<Error>> entry : validation.errorsMap().entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue().get(0).message();

                flash.put(key, value);
            }
            System.out.println(flash);
            if (request.params.get("return_to_summary").equals("yes")) {
              redirect(String.format("/page/%d/%d?return_to_summary=yes", PAGE_ID, listing.id));
            } else {
              redirect(String.format("/page/%d/%d", PAGE_ID, listing.id));
            }
        }

        Map<String, String> pageResponses = new HashMap<String, String>();
        pageResponses.put("p26q1", p26q1);
        pageResponses.put("p26q1assurance", p26q1assurance);
        saveResponseToPage(PAGE_ID, listing, pageResponses);
        if (request.params.get("return_to_summary").equals("yes")) {
          redirect(listing.summaryPageUrl(PAGE_ID));
        } else {
          redirect(listing.nextPageUrl(PAGE_ID, listing.id));
        }
    }
}
