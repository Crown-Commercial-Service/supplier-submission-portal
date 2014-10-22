package controllers;

import models.Listing;
import models.Page;
import play.Logger;

import java.util.Map;

public class AuthenticatingQuestionPage extends AuthenticatingController {
    protected static void saveResponseToPage(Long page_id, Listing listing, Map<String,String> responses){
        Page page = null;
        try {
            Logger.info("Try to find response page with page id: " + page_id);
            page = listing.getResponsePageByPageId(page_id);
        } catch (Exception e) {
            Logger.error(e.getMessage());
            error();
        }

        if(page == null){
            Logger.info("Page does not exist, creating new page");
            page = new Page(listing.id, page_id);
            saveResponseToNewPage(page, responses);
        } else {
            Logger.info("Page does exist, updating current page");
            saveResponseToExistingPage(page, responses);
        }
        listing.addResponsePage(page, page_id, supplierDetailsFromCookie.get("supplierEmail"));
    }

    private static void saveResponseToNewPage(Page page, Map<String,String> responses){
        page.responses.putAll(responses);
        page.insert();
    }

    private static void saveResponseToExistingPage(Page page, Map<String,String> responses){
        page.responses.putAll(responses);
        page.update();
    }
}
