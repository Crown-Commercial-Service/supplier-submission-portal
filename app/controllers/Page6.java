package controllers;

import models.Document;
import models.Listing;
import models.Page;
import play.Logger;
import play.data.Upload;
import play.data.validation.Error;
import play.i18n.Messages;
import uk.gov.gds.dm.DocumentUtils;

import java.util.List;
import java.util.Map;

import static uk.gov.gds.dm.DocumentUtils.storeDocument;

public class Page6 extends AuthenticatingController {

    private static final Long PAGE_ID = 6l;
    private static final String QUESTION_ID = "p6q1";

    public static void savePage(Long listingId, Upload p6q1) {

        Listing listing = Listing.getByListingId(listingId);

        if (listing.serviceSubmitted) {
          redirect(listing.summaryPageUrl());
        }

        validation.required(p6q1).key("p6q1");

        if(p6q1 != null){
            if(!DocumentUtils.validateDocumentFormat(p6q1)){
                validation.addError("p6q1", Messages.getMessage("en", "validation.file.wrongFormat"));
            }
            if(!DocumentUtils.validateDocumentFileSize(p6q1)){
                validation.addError("p6q1", Messages.getMessage("en", "validation.file.tooLarge"));
            }
        }

//        try {
//            Document document = storeDocument(p6q1, getSupplierId(), listingId, QUESTION_ID);
//            document.insert();
//        } catch(Exception e) {
//            Logger.error(e, "Could not upload document to S3. Cause: %s", e.getMessage());
//            validation.addError("p6q1", Messages.getMessage("en", "validation.upload.failed"));
//        }

        if(validation.hasErrors()) {
            flash.put("body", params.get("body"));
            for(Map.Entry<String, List<Error>> entry : validation.errorsMap().entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue().get(0).message();

                flash.put(key, value);
            }
            redirect(String.format("/page/%d/%d", PAGE_ID, listing.id));
        }

        Page page = new Page(listingId, PAGE_ID);
        page.responses.put("p6q1", p6q1.getFileName());
        page.insert();
        listing.addResponsePage(page, PAGE_ID, supplierDetailsFromCookie.get("supplierEmail"));
        redirect(listing.nextPageUrl(PAGE_ID, listing.id));

    }

}
