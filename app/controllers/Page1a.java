package controllers;

import models.Listing;
import models.Page;

import java.util.Arrays;
import play.mvc.Controller;


public class Page1a extends Controller {

    private static final String PAGE_ID = "1a";
    
    public static void showPage(Long listingId) {
        
        Listing listing = Listing.getByListingId(listingId);
        int index = listing.pageSequence.indexOf(PAGE_ID);

        renderArgs.put("pageNum", Integer.toString(index+1));
        renderArgs.put("pageTotal", Integer.toString(listing.pageSequence.size()));
        Page page = listing.completedPages.get(index);
        if (page!= null) {
            renderArgs.put("oldValues", page.responses);
        }
        render();
    }

    public static void savePage(Long listingId, String[] p1a_q1) {
        
        Listing listing = Listing.getByListingId(listingId);
        // Validate all fields on this page requiring validation
        validation.required(p1a_q1);
        if(validation.hasErrors()) {
            for(play.data.validation.Error error : validation.errors()) {
                System.out.println("Validation error: " + error.message());
            }
            flash.error("Validation failed", validation.errors());
            redirect(String.format("/page/%s/%s", PAGE_ID, listing.id));
        }
        // Save the form data as a Page into the correct page index
        int index = listing.pageSequence.indexOf(PAGE_ID);
        Page page = new Page(listingId, PAGE_ID);
        page.responses.put("p1a_q1", Arrays.asList(p1a_q1).toString());
        listing.completedPages.add(index, page);
        listing.update();
        redirect(String.format("/page/%s/%s", listing.nextPage(PAGE_ID), listing.id));
    }
}
