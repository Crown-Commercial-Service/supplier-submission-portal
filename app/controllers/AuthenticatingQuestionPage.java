package controllers;

import models.Listing;
import models.Page;
import play.Logger;

import java.util.Map;

public class AuthenticatingQuestionPage extends AuthenticatingController {
    protected static void saveResponseToPage(Long page_id, Listing listing, Map<String,String> responses){
        Page page = null;
        try {
            page = listing.getResponsePageByPageId(page_id);
        } catch (Exception e) {
            Logger.error(e.getMessage());
            error();
        }

        if(page == null){
            page = new Page(listing.id, page_id);
            saveResponseToNewPage(page, responses);
        } else {
            saveResponseToExistingPage(page, responses);
        }
        listing.addResponsePage(page, page_id, getEmail());
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
