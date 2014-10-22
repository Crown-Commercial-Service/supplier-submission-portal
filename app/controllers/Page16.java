package controllers;

import com.google.appengine.repackaged.com.google.common.base.Strings;
import com.google.gson.Gson;
import models.Listing;
import play.data.validation.Error;
import uk.gov.gds.dm.ValidationUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;


public class Page16 extends AuthenticatingQuestionPage {

    private static final Long PAGE_ID = 16l;

    public static void savePage(Long listingId) {

        Listing listing = Listing.getByListingId(listingId);

        if(!listing.supplierId.equals(getSupplierId())) {
            notFound();
        }
        
        if (listing.serviceSubmitted) {
          redirect(listing.summaryPageUrl());
        }

        // Extract multiple values for list items
        Map<String, String[]> params = request.params.all();
        ArrayList<String> p16q1 = new ArrayList();

        String s;

        for (int i=1; i<11; i++) {
            if(params.containsKey("p16q1val" + i)){
                s = params.get("p16q1val" + i)[0];
                if (!Strings.isNullOrEmpty(s)) {
                    validation.maxSize(s, 100).key("p16q1val" + i).message("Too many characters");
                    validation.isTrue(ValidationUtils.isWordCountLessThan(s, 10)).key("p16q1val" + i).message("Too many words");
                    p16q1.add(s);
                }
            }
        }

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

        // Save the form data as a Page into the correct page index
        Map<String, String> pageResponses = new HashMap<String, String>();
        Gson gson = new Gson();
        pageResponses.put("p16q1", gson.toJson(p16q1));
        saveResponseToPage(PAGE_ID, listing, pageResponses);
        if (request.params.get("return_to_summary").equals("yes")) {
          redirect(listing.summaryPageUrl(PAGE_ID));
        } else {
          redirect(listing.nextPageUrl(PAGE_ID, listing.id));
        }
    }
}
