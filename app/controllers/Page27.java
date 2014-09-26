package controllers;

import models.Listing;
import models.Page;
import play.mvc.Controller;

public class Page27 extends Controller {

    private static final Long PAGE_ID = 27l;

    public static void savePage(Long listingId, String p27q1, String p27q2, String p27q3, String p27q4, String p27q5, String p27q6,
                                String p27q7, String p27q8, String p27q9, String p27q10, String p27q11, String p27q12, String p27q13) {

        Listing listing = Listing.getByListingId(listingId);

        // TODO: Validate all fields on this page requiring validation
        // Q9 is the only one for all lots
//        validation.required(p27q9).message("p27q9:null");
        if (!listing.lot.equals("SCS")) {
            if (!listing.lot.equals("SaaS")) {
                // Q1 for IaaS, PaaS only
//                validation.required(p27q1).message("p27q1:null");
            }
            // Rest are for all lots except SCS
//            validation.required(p27q2).message("p27q2:null");
//            validation.required(p27q3).message("p27q3:null");
//            validation.required(p27q4).message("p27q4:null");
//            validation.required(p27q5).message("p27q5:null");
//            validation.required(p27q6).message("p27q6:null");
//            validation.required(p27q7).message("p27q7:null");
//            validation.required(p27q8).message("p27q8:null");
//            validation.required(p27q10).message("p27q10:null");
//            validation.required(p27q11).message("p27q11:null");
//            validation.required(p27q12).message("p27q12:null");
//            validation.required(p27q13).message("p27q13:null");
        }
        if(validation.hasErrors()) {
            flash.error("%s", validation.errors());
            redirect(String.format("/page/%d/%d", PAGE_ID, listing.id));
        }

        Page page = new Page(listingId, PAGE_ID);
        page.responses.put("p27q1", p27q1);
        page.responses.put("p27q2", p27q2);
        page.responses.put("p27q3", p27q3);
        page.responses.put("p27q4", p27q4);
        page.responses.put("p27q5", p27q5);
        page.responses.put("p27q6", p27q6);
        page.responses.put("p27q7", p27q7);
        page.responses.put("p27q8", p27q8);
        page.responses.put("p27q9", p27q9);
        page.responses.put("p27q10", p27q10);
        page.responses.put("p27q11", p27q11);
        page.responses.put("p27q12", p27q12);
        page.responses.put("p27q13", p27q13);
        page.insert();
        listing.addResponsePage(page, PAGE_ID);
        redirect(listing.nextPageUrl(PAGE_ID, listing.id));
    }

}
