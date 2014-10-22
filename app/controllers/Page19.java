package controllers;

import com.google.gson.Gson;
import models.Listing;
import models.Page;
import play.data.validation.Error;
import uk.gov.gds.dm.ValidationUtils;

import java.util.Map;
import java.util.List;

public class Page19 extends AuthenticatingController {

    private static final Long PAGE_ID = 19l;

    public static void savePage(Long listingId, String[] p19q1, String p19q2, String[] p19q3) {

        Listing listing = Listing.getByListingId(listingId);

        if(!listing.supplierId.equals(getSupplierId())) {
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
            System.out.println(flash);
            redirect(String.format("/page/%d/%d", PAGE_ID, listing.id));
        }

        // Save the form data as a Page into the correct page index
        Gson gson = new Gson();
        Page page = new Page(listingId, PAGE_ID);
        page.responses.put("p19q1", gson.toJson(p19q1));
        page.responses.put("p19q2", p19q2);
        page.responses.put("p19q3", gson.toJson(p19q3));
        page.insert();
        listing.addResponsePage(page, PAGE_ID, getEmail());
        redirect(listing.nextPageUrl(PAGE_ID, listing.id));
    }
}
