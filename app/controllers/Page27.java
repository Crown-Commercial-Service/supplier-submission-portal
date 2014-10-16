package controllers;

import models.Listing;
import models.Page;
import play.data.validation.Error;
import java.util.Map;
import java.util.List;

public class Page27 extends AuthenticatingController {
    
    private static final Long PAGE_ID = 27l;

    public static void savePage(Long listingId, String p27q1, String p27q2, String p27q1assurance, String p27q2assurance) {

        Listing listing = Listing.getByListingId(listingId);

        // Validate all fields on this page requiring validation
        if (!listing.lot.equals("SCS")) {
            // SaaS, PaaS, IaaS
            validation.required(p27q2).key("p27q2");
            validation.maxSize(p27q2, 10);
            validation.required(p27q2assurance).key("p27q2");
            validation.maxSize(p27q2assurance, 50);

            if (!listing.lot.equals("SaaS")) {
                // Q1 for IaaS, PaaS only
                validation.required(p27q1).key("p27q1");
                validation.maxSize(p27q1, 10);
                validation.required(p27q1assurance).key("p27q1");
                validation.maxSize(p27q1assurance, 50);
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
            redirect(String.format("/page/%d/%d", PAGE_ID, listing.id));
        }

        Page page = new Page(listingId, PAGE_ID);
        page.responses.put("p27q1", p27q1);
        page.responses.put("p27q2", p27q2);
        page.responses.put("p27q1assurance", p27q1assurance);
        page.responses.put("p27q2assurance", p27q2assurance);
        page.insert();
        listing.addResponsePage(page, PAGE_ID, supplierDetailsFromCookie.get("supplierEmail"));
        redirect(listing.nextPageUrl(PAGE_ID, listing.id));
    }

}
