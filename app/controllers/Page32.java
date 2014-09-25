package controllers;

import java.util.Arrays;
import models.Listing;
import models.Page;
import play.mvc.Controller;

public class Page32 extends Controller {

    private static final Long PAGE_ID = 32l;

    public static void savePage(Long listingId, String[] p32q1) {

        Listing listing = Listing.getByListingId(listingId);

        validation.required(p32q1).message("p32q1:null");
        if(validation.hasErrors()) {
            flash.error("%s", validation.errors());
            redirect(String.format("/page/%d/%d", PAGE_ID, listing.id));
        }
        
        Page page = new Page(listingId, PAGE_ID);
        page.responses.put("p32q1", Arrays.asList(p32q1).toString());
        page.insert();
        listing.addResponsePage(page, PAGE_ID);
        redirect(listing.nextPageUrl(PAGE_ID, listing.id));
    }
}
