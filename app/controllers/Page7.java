package controllers;

import models.Document;
import models.Listing;
import models.Page;
import play.Logger;
import play.data.Upload;
import play.i18n.Messages;
import play.data.validation.Error;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

import static uk.gov.gds.dm.DocumentUtils.*;

public class Page7 extends AuthenticatingQuestionPage {

    private static final Long PAGE_ID = 7l;


    public static void savePage(Long listingId, String p7q1, String p7q2, Upload p7q3) {

        Listing listing = Listing.getByListingId(listingId);

        if (listing.serviceSubmitted) {
          redirect(listing.summaryPageUrl());
        }

        //Validate all fields on this page requiring validation
        if(!listing.lot.equals("SaaS")){
            validation.required(p7q1).key("p7q1");
            validation.maxSize(p7q1, 10);
        }
        validation.required(p7q2).key("p7q2");
        validation.maxSize(p7q2, 10);

        // Validate document
        validation.required(p7q3).key("p7q3");
        if(p7q3 != null){
            if(!validateDocumentFormat(p7q3)){
                validation.addError("p7q3", Messages.getMessage("en", "validation.file.wrongFormat"));
            }
            if(!validateDocumentFileSize(p7q3)){
                validation.addError("p7q3", Messages.getMessage("en", "validation.file.tooLarge"));
            }
        }

//        try {
//            Document document = storeDocument(p7q3, getSupplierId(), listing.id, "p7q3");
//            document.insert();
//        } catch(Exception e) {
//            Logger.error(e, "Could not upload document to S3. Cause: %s", e.getMessage());
//            validation.addError("p7q3", Messages.getMessage("en", "validation.upload.failed"));
//        }

        System.out.println(validation.errorsMap());

        if(validation.hasErrors()) {
            flash.put("body", "p7q1=" + params.get("p7q1") + "&p7q2=" + params.get("p7q2") + "&p7q3=" + params.get("p7q3"));
            for(Map.Entry<String, List<Error>> entry : validation.errorsMap().entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue().get(0).message();

                flash.put(key, value);
            }
            System.out.println(flash);
            if (request.params.get("return_to_summary").equals("yes")) {
              redirect(String.format("/page/%d/%d?return_to_summary=yes", PAGE_ID, listing.id));
            } else {
              redirect(String.format("/page/%d/%d", PAGE_ID, listing.id));
            }
        }

        //Save the form data as a Page into the correct page index
        Map<String, String> pageResponses = new HashMap<String, String>();
        pageResponses.put("p7q1", p7q1);
        pageResponses.put("p7q2", p7q2);
        pageResponses.put("p7q3", p7q3.getFileName());
        saveResponseToPage(PAGE_ID, listing, pageResponses);
        if (request.params.get("return_to_summary").equals("yes")) {
          redirect(listing.summaryPageUrl(PAGE_ID));
        } else {
          redirect(listing.nextPageUrl(PAGE_ID, listing.id));
        }
    }

}
