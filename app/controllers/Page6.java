package controllers;

import models.Document;
import models.Listing;
import models.Page;
import play.Play;
import play.data.Upload;
import uk.gov.gds.dm.DocumentUtils;
import play.i18n.Messages;
import play.data.validation.Error;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.List;

public class Page6 extends AuthenticatingController {

    private static final Long PAGE_ID = 6l;
    private static final String QUESTION_ID = "p6q1";

    public static void savePage(Long listingId, Upload p6q1) {

        Listing listing = Listing.getByListingId(listingId);

        validation.required(p6q1).key("p6q1");

        if(p6q1 != null){
            if(!DocumentUtils.validateDocumentFormat(p6q1)){
                validation.addError("p6q1", Messages.getMessage("en", "validation.file.wrongFormat"));
            }
            if(!DocumentUtils.validateDocumentFileSize(p6q1)){
                validation.addError("p6q1", Messages.getMessage("en", "validation.file.tooLarge"));
            }
        }

        if(validation.hasErrors()) {

            for(Map.Entry<String, List<Error>> entry : validation.errorsMap().entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue().get(0).message();

                flash.put(key, value);
            }
            redirect(String.format("/page/%d/%d", PAGE_ID, listing.id));
        }

        Page page = new Page(listingId, PAGE_ID);

        Document document = storeDocument(p6q1, listingId);
        document.insert();

        page.insert();
        listing.addResponsePage(page, PAGE_ID);
        redirect(listing.nextPageUrl(PAGE_ID, listing.id));
    }

    private static Document storeDocument(Upload upload, long listingId) {

        String supplierName = getSupplierName();
        String documentName = upload.getFileName();
        try {
            documentName = URLEncoder.encode(documentName, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            // won't happen
        }
        Document document = Document
                .forListing(listingId)
                .withName(documentName)
                .forQuestion(QUESTION_ID)
                .forSupplier(supplierName)
                .fromFile(upload.asFile()).build();

        document.pushDocumentToStorage();

        return document;
    }



}
