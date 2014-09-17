package controllers;

import models.Listing;
import models.Page;
import play.mvc.Controller;

// Not a real Controller - just a template that can serve as a skeleton for page controllers 

public class AAA1TemplatePage extends Controller {

    private static final String PAGE_ID = "CURRENT_PAGE_ID";
    
    // TODO: This method might be the same for all pages, in which case move it to a 'QuestionPage' superclass and extend that
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

    // TODO: This method might be the same for all classes except for validation, in which case move to 'QuestionPage' superclass and just have validator method in extended class
    public static void savePage(Long listingId) {

        Listing listing = Listing.getByListingId(listingId);
        
        // TODO: Validate all fields on this page requiring validation
        
        if(validation.hasErrors()) {
            for(play.data.validation.Error error : validation.errors()) {
                System.out.println(error.message());
            }
            flash.error("Validation failed", validation.errors());
            redirect(String.format("/page/%s/%s", PAGE_ID, listing.id));
        }
        
        int index = listing.pageSequence.indexOf(PAGE_ID);
        //TODO: Save the form data as a Page into the correct page index
        
        redirect(String.format("/page/%s/%s", listing.nextPage(PAGE_ID), listing.id));
    }
}
