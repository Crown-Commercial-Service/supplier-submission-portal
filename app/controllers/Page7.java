package controllers;

import models.Listing;
import models.Page;
import play.i18n.Messages;
import play.mvc.Controller;
import uk.gov.gds.dm.DocumentUtils;

import java.io.File;

public class Page7 extends Controller {

    private static final Long PAGE_ID = 7l;


    public static void savePage(Long listingId, String p7q1, String p7q2, File p7q3) {

        Listing listing = Listing.getByListingId(listingId);
        
        //Validate all fields on this page requiring validation
        if(!listing.lot.equals("SaaS")){
            validation.required(p7q1).message("p7q1:null");
        }
        validation.required(p7q2).message("p7q2:null");

        // Validate document
        validation.required(p7q3).message("p7q3: null");
        if(p7q3 != null){
            if(!DocumentUtils.validateDocumentFormat(p7q3)){
                validation.addError("p7q3", Messages.getMessage("en", "validation.file.wrongFormat"));
            }
            if(!DocumentUtils.validateDocumentFileSize(p7q3)){
                validation.addError("p7q3", Messages.getMessage("en", "validation.file.tooLarge"));
            }
        }

        if(validation.hasErrors()) {
            flash.error("%s", validation.errors());
            redirect(String.format("/page/%d/%d", PAGE_ID, listing.id));
        }

        //Save the form data as a Page into the correct page index
        Page page = new Page(listingId, PAGE_ID);
        page.responses.put("p7q1", p7q1);
        page.responses.put("p7q2", p7q1);
        // TODO: Document storage response

        page.insert();
        listing.addResponsePage(page, PAGE_ID);
        redirect(listing.nextPageUrl(PAGE_ID, listing.id));
    }

}
