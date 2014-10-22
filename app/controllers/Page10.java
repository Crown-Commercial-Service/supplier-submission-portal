package controllers;

import com.google.gson.Gson;
import models.Listing;

import play.data.validation.Error;
import uk.gov.gds.dm.ValidationUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class Page10 extends AuthenticatingQuestionPage {


    private static final Long PAGE_ID = 10l;

    public static void savePage(Long listingId, String[] p10q1, String p10q2, String p10q3, String p10q4, String p10q5) {

        Listing listing = Listing.getByListingId(listingId);

        if (listing.serviceSubmitted) {
          redirect(listing.summaryPageUrl());
        }

        validation.required(p10q1).key("p10q1");
        validation.isTrue(ValidationUtils.stringArrayValuesAreNotTooLong(p10q1, 20)).key("p10q1").message("Invalid values");

        validation.required(p10q2).key("p10q2");
        validation.maxSize(p10q2, 10);

        validation.required(p10q3).key("p10q3");
        validation.isTrue(ValidationUtils.isWordCountLessThan(p10q3, 20)).key("p10q3").message("Too many words");
        validation.maxSize(p10q3, 200);

        validation.required(p10q4).key("p10q4");
        validation.isTrue(ValidationUtils.isWordCountLessThan(p10q4, 20)).key("p10q4").message("Too many words");
        validation.maxSize(p10q4, 200);

        validation.required(p10q5).key("p10q5");
        validation.maxSize(p10q5, 10);

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

        //Save the form data as a Page into the correct page index
        Map<String, String> pageResponses = new HashMap<String, String>();
        Gson gson = new Gson();
        pageResponses.put("p10q1", gson.toJson(p10q1));
        pageResponses.put("p10q2", p10q2);
        pageResponses.put("p10q3", p10q3);
        pageResponses.put("p10q4", p10q4);
        pageResponses.put("p10q5", p10q5);
        saveResponseToPage(PAGE_ID, listing, pageResponses);
        if (request.params.get("return_to_summary").equals("yes")) {
          redirect(listing.summaryPageUrl(PAGE_ID));
        } else {
          redirect(listing.nextPageUrl(PAGE_ID, listing.id));
        }
   }
}
