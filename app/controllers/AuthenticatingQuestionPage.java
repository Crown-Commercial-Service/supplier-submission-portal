package controllers;

import models.Document;
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

    protected static void saveResponseToPage(Long page_id, Listing listing, Map<String,String> responses, Map<String, Document> docs){
        Page page = null;
        try {
            page = listing.getResponsePageByPageId(page_id);
        } catch (Exception e) {
            Logger.error(e.getMessage());
            error();
        }

        if(page == null){
            page = new Page(listing.id, page_id);
            saveResponseToNewPage(page, responses, docs);
        } else {
            saveResponseToExistingPage(page, responses, docs);
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

    private static void saveResponseToNewPage(Page page, Map<String,String> responses, Map<String, Document> docs){
        page.responses.putAll(responses);
        page.submittedDocuments.putAll(docs);
        for(Document doc: docs.values()) {
            doc.insert();
        }
        page.insert();
    }

    private static void saveResponseToExistingPage(Page page, Map<String,String> responses, Map<String, Document> docs){
        page.responses.putAll(responses);
        for(String key: docs.keySet()){
            if (page.submittedDocuments.containsKey(key)) {
                page.submittedDocuments.get(key).delete();
            }
            docs.get(key).insert();
        }
        page.submittedDocuments.putAll(docs);
        page.update();
    }
}
