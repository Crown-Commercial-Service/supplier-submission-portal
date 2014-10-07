package controllers;

import com.google.gson.Gson;
import models.Listing;
import models.Page;
import play.data.validation.Error;
import uk.gov.gds.dm.ValidationUtils;

import java.util.Map;
import java.util.List;

public class Page33 extends AuthenticatingController {

    private static final Long PAGE_ID = 33l;

    public static void savePage(Long listingId, String p33q1, String[] p33q2, String p33q3, String p33q3assurance, String p33q4, String p33q4assurance) {

        Listing listing = Listing.getByListingId(listingId);

        // Validate all fields on this page requiring validation
        if (!listing.lot.equals("SaaS")) {
            validation.required(p33q1).key("p33q1");
            validation.maxSize(p33q1, 10);
            validation.required(p33q2).key("p33q2");
            validation.isTrue(ValidationUtils.stringArrayValuesAreNotTooLong(p33q2, 20)).key("p33q2").message("Invalid values.");
        }
        validation.required(p33q3).key("p33q3");
        validation.maxSize(p33q3, 10);
        validation.required(p33q3assurance).key("p33q3assurance");
        validation.maxSize(p33q3assurance, 50);

        validation.required(p33q4).key("p33q4");
        validation.maxSize(p33q4, 10);
        validation.required(p33q4assurance).key("p33q4assurance");
        validation.maxSize(p33q4assurance, 50);

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
        page.responses.put("p33q1", p33q1);
        if (p33q2 != null) {
            page.responses.put("p33q2", gson.toJson(p33q2));
        }
        else {
            page.responses.put("p33q2", null);
        }
        page.responses.put("p33q3", p33q3);
        page.responses.put("p33q4", p33q4);
        page.insert();
        listing.addResponsePage(page, PAGE_ID);
        redirect(listing.nextPageUrl(PAGE_ID, listing.id));
    }

}
