package controllers;

import models.Listing;
import models.Page;
import play.data.validation.Error;
import uk.gov.gds.dm.ValidationUtils;

import java.util.List;
import java.util.Map;

public class Page4 extends AuthenticatingController {

    private static final Long PAGE_ID = 4l;

    public static void savePage(Long listingId, String p4q1, String p4q2) {

        Listing listing = Listing.getByListingId(listingId);

        // Validate all fields on this page requiring validation
        validation.required(p4q1);
        validation.maxSize(p4q1, 100);

        validation.required(p4q2);
        validation.isTrue(ValidationUtils.isWordCountLessThan(p4q2, 50)).key("p4q2").message("Too many words");
        validation.maxSize(p4q2, 500);

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
        page.responses.put("p4q1", p4q1);
        page.responses.put("p4q2", p4q2);
        page.insert();
        listing.title = p4q1;
        listing.addResponsePage(page, PAGE_ID);
        redirect(listing.nextPageUrl(PAGE_ID, listing.id));
    }
}
