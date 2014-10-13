package controllers;

import com.google.gson.Gson;
import models.Listing;
import models.Page;
import play.data.validation.Error;
import uk.gov.gds.dm.ValidationUtils;

import java.util.Map;
import java.util.List;

public class Page36 extends AuthenticatingController {

    private static final Long PAGE_ID = 36l;

    public static void savePage(Long listingId, String[] p36q1, String p36q1assurance) {

        Listing listing = Listing.getByListingId(listingId);

        // Validate all fields on this page requiring validation
        validation.required(p36q1).key("p36q1");
        validation.isTrue(ValidationUtils.stringArrayValuesAreNotTooLong(p36q1, 100)).key("p36q1").message("Invalid values");
        validation.required(p36q1assurance).key("p36q1");
        validation.maxSize(p36q1assurance, 50);

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

        Page page = new Page(listingId, PAGE_ID);
        Gson gson = new Gson();
        page.responses.put("p36q1", gson.toJson(p36q1));
        page.responses.put("p36q1assurance", p36q1assurance);
        page.insert();
        listing.addResponsePage(page, PAGE_ID);
        redirect(listing.nextPageUrl(PAGE_ID, listing.id));
    }
}
