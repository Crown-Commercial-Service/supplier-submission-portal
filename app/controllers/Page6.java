package controllers;

import models.Listing;
import models.Page;
import play.mvc.Controller;
import uk.gov.gds.dm.DocumentUtils;
import play.i18n.Messages;

import java.io.File;

public class Page6 extends Controller {

    private static final Long PAGE_ID = 6l;

    public static void savePage(Long listingId, File p6q1) {

        Listing listing = Listing.getByListingId(listingId);

        // Validate document
        //validation.required(p6q1).message("p6q1: null");
        if(p6q1 != null){
            if(!DocumentUtils.validateDocumentFormat(p6q1)){
                validation.addError("pg6q1", Messages.getMessage("en", "validation.file.wrongFormat"));
            }
            if(!DocumentUtils.validateDocumentFileSize(p6q1)){
                validation.addError("pg6q1", Messages.getMessage("en", "validation.file.tooLarge"));
            }
        }
        
        if(validation.hasErrors()) {
            flash.error("%s", validation.errors());
            redirect(String.format("/page/%d/%d", PAGE_ID, listing.id));
        }

        Page page = new Page(listingId, PAGE_ID);
        
        // TODO: Document storage in response

        page.insert();
        listing.addResponsePage(page, PAGE_ID);
        redirect(listing.nextPageUrl(PAGE_ID, listing.id));
    }
}
