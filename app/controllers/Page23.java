package controllers;

import models.Listing;
import models.Page;
import play.data.validation.Error;
import java.util.Map;
import java.util.List;

public class Page23 extends AuthenticatingController {

    private static final Long PAGE_ID = 23l;

    public static void savePage(Long listingId, String p23q1, String p23q2, String p23q3, String p23q1assurance, String p23q2assurance, String p23q3assurance) {

        Listing listing = Listing.getByListingId(listingId);

        // Validate all fields on this page requiring validation
        validation.required(p23q1).key("p23q1");
        validation.required(p23q1assurance).key("p23q1");
        validation.maxSize(p23q1, 50);
        validation.maxSize(p23q1assurance, 50);
        if(!listing.lot.equals("SaaS")) {
            validation.required(p23q2).key("p23q2");
            validation.required(p23q2assurance).key("p23q2");
            validation.maxSize(p23q2, 50);
            validation.maxSize(p23q2assurance, 50);
            validation.required(p23q3).key("p23q3");
            validation.required(p23q3assurance).key("p23q3");
            validation.maxSize(p23q3, 50);
            validation.maxSize(p23q3assurance, 50);
        }

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
        Page page = new Page(listingId, PAGE_ID);
        page.responses.put("p23q1", p23q1);
        page.responses.put("p23q2", p23q2);
        page.responses.put("p23q3", p23q3);
        page.responses.put("p23q1assurance", p23q1assurance);
        page.responses.put("p23q2assurance", p23q2assurance);
        page.responses.put("p23q3assurance", p23q3assurance);
        page.insert();
        listing.addResponsePage(page, PAGE_ID, supplierDetailsFromCookie.get("supplierEmail"));
        redirect(listing.nextPageUrl(PAGE_ID, listing.id));
    }

}
