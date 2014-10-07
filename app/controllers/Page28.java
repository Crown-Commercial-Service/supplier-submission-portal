package controllers;

import com.google.gson.Gson;
import models.Listing;
import models.Page;
import play.data.validation.Error;
import uk.gov.gds.dm.ValidationUtils;

import java.util.Map;
import java.util.List;


public class Page28 extends AuthenticatingController {

    private static final Long PAGE_ID = 28l;

    public static void savePage(Long listingId, String[] p28q1, String p28q1assurance) {

        Listing listing = Listing.getByListingId(listingId);

        // Validate all fields on this page requiring validation
        validation.required(p28q1).key("p28q1");
        validation.isTrue(ValidationUtils.stringArrayValuesAreNotTooLong(p28q1, 100)).key("p28q1").message("Invalid values");
        validation.required(p28q1assurance).key("p28q1assurance");
        validation.maxSize(p28q1assurance, 50);

        if(validation.hasErrors()) {

            for(Map.Entry<String, List<Error>> entry : validation.errorsMap().entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue().get(0).message();

                flash.put(key, value);
            }
            System.out.println(flash);
            redirect(String.format("/page/%d/%d", PAGE_ID, listing.id));
        }

        Gson gson = new Gson();
        Page page = new Page(listingId, PAGE_ID);
        page.responses.put("p28q1", gson.toJson(p28q1));
        page.insert();
        listing.addResponsePage(page, PAGE_ID);
        redirect(listing.nextPageUrl(PAGE_ID, listing.id));
    }
}
