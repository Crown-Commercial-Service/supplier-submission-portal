package controllers;

import models.Listing;
import models.Page;
import play.i18n.Messages;
import play.mvc.Controller;
import uk.gov.gds.dm.DocumentUtils;
import play.data.validation.*;
import play.data.validation.Error;


import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;

public class Page7 extends Controller {

    private static final Long PAGE_ID = 7l;


    public static void savePage(Long listingId, String p7q1, String p7q2, File p7q3) {

        Listing listing = Listing.getByListingId(listingId);

        //Validate all fields on this page requiring validation
        if(!listing.lot.equals("SaaS")){
            validation.required(p7q1).key("p7q1");
        }
        validation.required(p7q2).key("p7q2");

        // Validate document
        if(p7q3 != null){
            if(!DocumentUtils.validateDocumentFormat(p7q3)){
                validation.addError("p7q3", Messages.getMessage("en", "validation.file.wrongFormat"));
            }
            if(!DocumentUtils.validateDocumentFileSize(p7q3)){
                validation.addError("p7q3", Messages.getMessage("en", "validation.file.tooLarge"));
            }
        } else {
            validation.required(p7q3).key("p7q3");
        }


        System.out.println(validation.errorsMap());

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
