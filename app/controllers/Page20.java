package controllers;

import com.google.appengine.repackaged.com.google.common.base.Strings;
import com.google.gson.Gson;
import models.Listing;
import play.data.validation.Error;
import uk.gov.gds.dm.ValidationUtils;
import uk.gov.gds.dm.Fixtures;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;


public class Page20 extends AuthenticatingQuestionPage {
    private static final Long PAGE_ID = 20l;

    public static void savePage(Long listingId, String return_to_summary) throws UnsupportedEncodingException {

        Listing listing = Listing.getByListingId(listingId);

        if(!listing.supplierId.equals(getSupplierId())) {
            Logger.error("Supplier id of listing did not match the logged in supplier.");
            notFound();
        }
        
        if (listing.serviceSubmitted) {
          redirect(listing.summaryPageUrl());
        }

        // Extract multiple values for list items
        Map<String, String[]> params = request.params.all();
        ArrayList<String> p20q1 = new ArrayList();

        String s;

        StringBuilder paramString = new StringBuilder();
        for (int i=1; i<11; i++) {
            if(params.containsKey("p20q1val" + i)){
                s = params.get("p20q1val" + i)[0];
                if (!Strings.isNullOrEmpty(s)) {
                    validation.isTrue(ValidationUtils.isWordCountLessThan(s, 10)).key("p20q1").message("Too many words");
                    validation.maxSize(s, 100).key("p20q1");
                    p20q1.add(s);
                    paramString.append("p20q1=").append(URLEncoder.encode(s, "UTF-8")).append("&");
                }
            }
        }

        // Validate all fields on this page requiring validation
        if(validation.hasErrors()) {
            flash.put("body", paramString.toString());
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
        Gson gson = new Gson();
        pageResponses.put("p20q1", gson.toJson(p20q1));
        saveResponseToPage(PAGE_ID, listing, pageResponses);
        if (return_to_summary.contains("yes")) {
          redirect(listing.summaryPageUrl(PAGE_ID));
        } else {
          redirect(listing.nextPageUrl(PAGE_ID, listing.id));
        }
    }
}
