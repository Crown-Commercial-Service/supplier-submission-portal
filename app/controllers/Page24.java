package controllers;

import java.util.Arrays;
import models.Listing;
import models.Page;
import play.mvc.Controller;

public class Page24 extends Controller {

    private static final Long PAGE_ID = 24l;

    public static void savePage(Long listingId, String[] p24q1, String[] p24q2, String p24q3, String p24q4, String p24q5, String p24q6
            , String p24q7, String p24q8, String p24q9, String p24q10, String p24q11) {

        Listing listing = Listing.getByListingId(listingId);

        // Validate all fields on this page requiring validation
        if (!listing.lot.equals("SCS")) {
            validation.required(p24q1).message("p24q1 : null");
            validation.required(p24q2).message("p24q2 : null");
            validation.required(p24q4).message("p24q4 : null");
            validation.required(p24q5).message("p24q5 : null");
            validation.required(p24q6).message("p24q6 : null");
            validation.required(p24q11).message("p24q11 : null");

            if (!listing.lot.equals("SaaS")) {
                validation.required(p24q7).message("p24q7 : null");
                validation.required(p24q8).message("p24q8 : null");
                validation.required(p24q9).message("p24q9 : null");
                validation.required(p24q10).message("p24q10 : null");
            }
        }
        validation.required(p24q3).message("p24q3 : null");
        if (validation.hasErrors()) {
            flash.error("%s", validation.errors());
            redirect(String.format("/page/%d/%d", PAGE_ID, listing.id));
        }

        // Save the form data as a Page into the correct page index
        Page page = new Page(listingId, PAGE_ID);
        if (p24q1 != null) {
            page.responses.put("p24q1", Arrays.asList(p24q1).toString());
        }
        else {page.responses.put("p24q1", null);
        }
        if (p24q2 != null) {
            page.responses.put("p24q2", Arrays.asList(p24q2).toString());
        }
        else {page.responses.put("p24q2", null);
        }
        page.responses.put("p24q3", p24q3);
        page.responses.put("p24q4", p24q4);
        page.responses.put("p24q5", p24q5);
        page.responses.put("p24q6", p24q6);
        page.responses.put("p24q7", p24q7);
        page.responses.put("p24q8", p24q8);
        page.responses.put("p24q9", p24q9);
        page.responses.put("p24q10", p24q10);
        page.responses.put("p24q11", p24q11);
        page.insert();
        listing.addResponsePage(page, PAGE_ID);
        redirect(listing.nextPageUrl(PAGE_ID, listing.id));
    }

    public static void savePage(Long listingId, String p24q3) {
        savePage(listingId, null, null, p24q3, null, null, null, null, null, null, null, null);
    }

    public static void savePage(Long listingId, String[] p24q1, String[] p24q2, String p24q3, String p24q4, String p24q5, String p24q6, String p24q11) {
        savePage(listingId, p24q1, p24q2, p24q3, p24q4, p24q5, p24q6, null, null, null, null, p24q11);
    }
}
