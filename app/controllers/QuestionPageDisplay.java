package controllers;

import models.Listing;
import models.Page;
import play.mvc.Controller;


public class QuestionPageDisplay extends Controller {

    public static void showPage(Long pageId, Long listingId) {

        //TODO: Get content from yml file and pass in to template
        Listing listing = Listing.getByListingId(listingId);

        notFoundIfNull(listing);
        // TODO: Check listing belongs to authenticated user
        if(!listing.pageSequence.contains(pageId)){
            notFound();
        }

        int index = listing.pageSequence.indexOf(pageId);
        renderArgs.put("lot", listing.lot);
        renderArgs.put("content", Fixtures.getContentProperties());
        renderArgs.put("pageNum", Integer.toString(index+1));
        renderArgs.put("pageTotal", Integer.toString(listing.pageSequence.size()));
        Page page = listing.completedPages.get(index);
        if (page!= null) {
            renderArgs.put("oldValues", page.responses);
        }
        renderTemplate(String.format("QuestionPages/%d.html", pageId));
    }
}
