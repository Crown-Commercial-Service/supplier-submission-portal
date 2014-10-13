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

    public static void savePage(Long listingId, String p8q1MinPrice, String p8q1MaxPrice, String p8q1Unit, String p8q1Interval,
                                String p8q2, String p8q3, String p8q4, String p8q5, Upload p8q6, Upload p8q7) {

        Listing listing = Listing.getByListingId(listingId);

        Double min = null, max = null;
                
        try {
            min = Double.valueOf(p8q1MinPrice);
        } catch(Exception ex) {
            validation.addError("p8q1", Messages.getMessage("en", "validation.invalid"));
        }
        try {
            if(p8q1MaxPrice != null) {
                max = Double.valueOf(p8q1MaxPrice);
            }
        } catch(Exception ex) {
            validation.addError("p8q1", Messages.getMessage("en", "validation.invalid"));
        }
        if (min != null && max != null) {
            if (min > max) {
                validation.addError("p8q1", Messages.getMessage("en", "validation.maxLessThanMin"));
            }
        }
        validation.required(p8q1MinPrice).key("p8q1").message("validationNoMinPriceSpecified");
        validation.required(p8q1Unit).key("p8q1").message("validationNoUnitSpecified");
        validation.maxSize(p8q1Unit, 25);
        validation.maxSize(p8q1Interval, 25);
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
            flash.put("body", "p8q1MinPrice=" + params.get("p8q1MinPrice") + "&p8q1MaxPrice=" + params.get("p8q1MaxPrice") +
                    "&p8q1Unit=" + params.get("p8q1Unit") + "&p8q1Interval=" + params.get("p8q1Interval") +
                    "&p8q2=" + params.get("p8q2") + "&p8q3=" + 
                    params.get("p8q3") + "&p8q4=" + params.get("p8q4") + "&p8q5=" + params.get("p8q5") + 
                    "&p8q6=" + params.get("p8q6") + "&p8q7=" + params.get("p8q7"));
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
        page.responses.put("p8q1MinPrice", p8q1MinPrice);
        page.responses.put("p8q1MaxPrice", p8q1MaxPrice);
        page.responses.put("p8q1Unit", p8q1Unit);
        page.responses.put("p8q1Interval", p8q1Interval);
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
