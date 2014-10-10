package controllers;

import com.google.gson.Gson;
import models.Listing;
import models.Page;


import play.data.validation.Error;
import uk.gov.gds.dm.ValidationUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class Page1 extends AuthenticatingController {

    private static final Long PAGE_ID = 1l;

    public static void savePage(Long listingId, String[] p1q1) {

        Listing listing = Listing.getByListingId(listingId);

        // Validate all fields on this page requiring validation
        validation.required(p1q1).key("p1q1");
        validation.isTrue(ValidationUtils.stringArrayValuesAreNotTooLong(p1q1, 30)).key("p1q1").message("Invalid values");


        if(validation.hasErrors()) {
            for(Map.Entry<String, List<Error>> entry : validation.errorsMap().entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue().get(0).message();

                flash.put(key, value);
            }

            redirect(String.format("/page/%d/%d", PAGE_ID, listing.id));
        }

        // Save the form data as a Page into the correct page index
        Page page = new Page(listingId, PAGE_ID);

        page.addFieldToPageResponse("p1q1", p1q1);

        page.insert();
        listing.addResponsePage(page, PAGE_ID);
        redirect(listing.nextPageUrl(PAGE_ID, listing.id));
    }
}
