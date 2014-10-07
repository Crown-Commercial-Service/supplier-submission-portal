package controllers;

import com.google.gson.Gson;
import models.Listing;
import models.Page;
import play.data.Upload;
import play.i18n.Messages;
import uk.gov.gds.dm.DocumentUtils;
import play.data.validation.Error;

import java.util.Map;
import java.util.List;

public class Page8 extends AuthenticatingController {

    private static final Long PAGE_ID = 8l;

    public static void savePage(Long listingId, String[][] p8q1, String p8q2, String p8q3, String p8q4, String p8q5, Upload p8q6, Upload p8q7) {

        Listing listing = Listing.getByListingId(listingId);

        if(p8q1 != null) {
            // TODO: Validate arrays
        } else {
            // validation.required(p8q1); // TODO: pricing matrix
        }
        validation.required(p8q2).key("p8q2");
        validation.maxSize(p8q2, 10);
        validation.required(p8q3).key("p8q3");
        validation.maxSize(p8q3, 10);

        if (!listing.lot.equals("SCS")) {
            validation.required(p8q4).key("p8q4");
            validation.required(p8q5).key("p8q5");
        }

        // Validate documents
        validation.required(p8q6).key("p8q6");
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

            for(Map.Entry<String, List<Error>> entry : validation.errorsMap().entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue().get(0).message();

                flash.put(key, value);
            }
            System.out.println(flash);
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
