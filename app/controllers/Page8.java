package controllers;

import com.google.gson.Gson;
import models.Listing;
import models.Page;
import play.i18n.Messages;
import play.mvc.Controller;
import uk.gov.gds.dm.DocumentUtils;

import java.io.File;

public class Page8 extends Controller {

    private static final Long PAGE_ID = 8l;

    public static void savePage(Long listingId, String[][] p8q1, String p8q2, String p8q3, String p8q4, String p8q5, File p8q6, File p8q7) {

        Listing listing = Listing.getByListingId(listingId);
        
        if(p8q1 != null) {
            // TODO: Validate arrays
        }
        validation.required(p8q2).message("p8q2:null");
        validation.required(p8q3).message("p8q3:null");
        // Files not required here?
        //validation.required(p8q6).message("p8q6:null");
        //validation.required(p8q7).message("p8q7:null");
        if (!listing.lot.equals("SCS")) {
            validation.required(p8q4).message("p8q4:null");
            validation.required(p8q5).message("p8q5:null");
        }

        // Validate documents
        if(p8q6 != null){
            if(!DocumentUtils.validateDocumentFormat(p8q6)){
                validation.addError("p8q6", Messages.getMessage("en", "validation.file.wrongFormat"));
            }
            if(!DocumentUtils.validateDocumentFileSize(p8q6)){
                validation.addError("p8q6", Messages.getMessage("en", "validation.file.tooLarge"));
            }
        }

        if(p8q7 != null){
            if(!DocumentUtils.validateDocumentFormat(p8q7)){
                validation.addError("p8q7", Messages.getMessage("en", "validation.file.wrongFormat"));
            }
            if(!DocumentUtils.validateDocumentFileSize(p8q7)){
                validation.addError("p8q7", Messages.getMessage("en", "validation.file.tooLarge"));
            }
        }

        if(validation.hasErrors()) {
            flash.error("%s", validation.errors());
            redirect(String.format("/page/%d/%d", PAGE_ID, listing.id));
        }

        Gson gson = new Gson();
        Page page = new Page(listingId, PAGE_ID);
        page.responses.put("p8q1", gson.toJson(p8q1));
        page.responses.put("p8q2", p8q2);
        page.responses.put("p8q3", p8q3);
        page.responses.put("p8q4", p8q4);
        page.responses.put("p8q5", p8q5);
        // TODO: Document storage for p8q6 and p8q7
        
        page.insert();
        listing.addResponsePage(page, PAGE_ID);
        redirect(listing.nextPageUrl(PAGE_ID, listing.id));
    }

}
