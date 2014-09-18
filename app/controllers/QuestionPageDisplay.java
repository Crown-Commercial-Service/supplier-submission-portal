package controllers;

import models.Listing;
import models.Page;
import play.mvc.Controller;

public class QuestionPageDisplay extends Controller {

    public static void showPage(String pageId, Long listingId) {
        //TODO: Get content from yml file and pass in to template
        Listing listing = Listing.getByListingId(listingId);
        int index = listing.pageSequence.indexOf(pageId);
        renderArgs.put("pageNum", Integer.toString(index+1));
        renderArgs.put("pageTotal", Integer.toString(listing.pageSequence.size()));
        Page page = listing.completedPages.get(index);
        if (page!= null) {
            renderArgs.put("oldValues", page.responses);
        }
        renderTemplate(String.format("QuestionPages/%s.html", pageId));
    }
}
