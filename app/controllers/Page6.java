package controllers;

import models.Listing;
import models.Page;
import play.mvc.Controller;
import uk.gov.gds.dm.DocumentUtils;
import play.i18n.Messages;
import play.data.validation.*;
import play.data.validation.Error;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;

public class Page6 extends Controller {

    private static final Long PAGE_ID = 6l;

    public static void savePage(Long listingId, File p6q1) {

        Listing listing = Listing.getByListingId(listingId);

        // Validate document
        //validation.required(p6q1).message("p6q1: null");
        if(p6q1 != null){
            if(!DocumentUtils.validateDocumentFormat(p6q1)){
                validation.addError("p6q1", Messages.getMessage("en", "validation.file.wrongFormat"));
            }
            if(!DocumentUtils.validateDocumentFileSize(p6q1)){
                validation.addError("p6q1", Messages.getMessage("en", "validation.file.tooLarge"));
            }
        } else {
            validation.required(p6q1);
        }

        if(validation.hasErrors()) {
            //flash.error("%s", validation.errors());

            for(Map.Entry<String, List<Error>> entry : validation.errorsMap().entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue().get(0).message();

                flash.put(key, value);
            }
            System.out.println(flash);
            redirect(String.format("/page/%d/%d", PAGE_ID, listing.id));
        }

        Page page = new Page(listingId, PAGE_ID);

        // TODO: Document storage in response

        page.insert();
        listing.addResponsePage(page, PAGE_ID);
        redirect(listing.nextPageUrl(PAGE_ID, listing.id));
    }
}
