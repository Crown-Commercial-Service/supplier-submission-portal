package controllers;

import com.google.gson.Gson;
import models.Listing;
import models.Page;
import play.data.validation.Error;
import uk.gov.gds.dm.ValidationUtils;

import java.util.Map;
import java.util.List;

public class Page32 extends AuthenticatingController {

    private static final Long PAGE_ID = 32l;

    public static void savePage(Long listingId, String[] p32q1, String p32q1assurance) {

        Listing listing = Listing.getByListingId(listingId);

        // Validate all fields on this page requiring validation
        validation.required(p32q1).key("p32q1");
        validation.isTrue(ValidationUtils.stringArrayValuesAreNotTooLong(p32q1, 100)).key("p32q1").message("Invalid values");
        validation.required(p32q1assurance).key("p32q1assurance");
        validation.maxSize(p32q1assurance, 50);

        if(validation.hasErrors()) {

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
        page.responses.put("p32q1", gson.toJson(p32q1));
        page.insert();
        listing.addResponsePage(page, PAGE_ID);
        redirect(listing.nextPageUrl(PAGE_ID, listing.id));
    }
}
