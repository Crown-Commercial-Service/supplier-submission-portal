package controllers;

import models.Document;
import models.Listing;
import play.Logger;
import play.data.Upload;
import play.data.validation.Error;
import play.i18n.Messages;
import uk.gov.gds.dm.DocumentUtils;
import uk.gov.gds.dm.Fixtures;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static uk.gov.gds.dm.DocumentUtils.storeDocument;

public class Page6 extends AuthenticatingQuestionPage {

    private static final Long PAGE_ID = 6l;
    private static final String QUESTION_ID = "p6q1";

    public static void savePage(Long listingId, Upload p6q1, String p6q1_uploaded, String return_to_summary) {

        Listing listing = Listing.getByListingId(listingId);

        if(!listing.supplierId.equals(getSupplierId())) {
            notFound();
        }
        
        if (listing.serviceSubmitted) {
          redirect(listing.summaryPageUrl());
        }
        
        if(p6q1_uploaded == null || p6q1_uploaded.isEmpty()) {
            validation.required(p6q1).key("p6q1");
        }
        if(p6q1 != null){
            if(!DocumentUtils.validateDocumentFormat(p6q1)){
                validation.addError("p6q1", Messages.getMessage("en", "validation.file.wrongFormat"));
            }
            if(!DocumentUtils.validateDocumentFileSize(p6q1)){
                validation.addError("p6q1", Messages.getMessage("en", "validation.file.tooLarge"));
            }
            try {
                Document document = storeDocument(p6q1, getSupplierId(), listingId, QUESTION_ID);
                document.insert();
            } catch(Exception e) {
                Logger.error(e, "Could not upload document to S3. Cause: %s", e.getMessage());
                validation.addError("p6q1", Messages.getMessage("en", "validation.upload.failed"));
            }
        }

        if(validation.hasErrors()) {
            flash.put("body", params.get("body"));
            for(Map.Entry<String, List<Error>> entry : validation.errorsMap().entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue().get(0).message();

                flash.put(key, Fixtures.getErrorMessage(key, value));
            }
            if (return_to_summary.contains("yes")) {
              redirect(String.format("/page/%d/%d?return_to_summary=yes", PAGE_ID, listing.id));
            } else {
              redirect(String.format("/page/%d/%d", PAGE_ID, listing.id));
            }
        }

        if(p6q1 != null) {
            Map<String, String> pageResponses = new HashMap<>();
            pageResponses.put("p6q1", p6q1.getFileName());
            saveResponseToPage(PAGE_ID, listing, pageResponses);
        }
        if (return_to_summary.contains("yes")) {
          redirect(listing.summaryPageUrl(PAGE_ID));
        } else {
          redirect(listing.nextPageUrl(PAGE_ID, listing.id));
        }
    }
}
