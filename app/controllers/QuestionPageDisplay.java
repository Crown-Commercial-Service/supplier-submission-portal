package controllers;

import models.Listing;
import models.Page;
import models.QuestionPage;
import play.mvc.Controller;


public class QuestionPageDisplay extends Controller {

    public static void showPage(Long pageId, Long listingId) {

        //TODO: Get content from yml file and pass in to template
        QuestionPage qp = QuestionPage.findByQpId(pageId);
        Listing listing = Listing.getByListingId(listingId);

        notFoundIfNull(listing);
        // TODO: Check listing belongs to authenticated user
        if(!listing.pageSequence.contains(pageId)){
            notFound();
        }

        int index = listing.pageSequence.indexOf(pageId);

        if(qp!=null) {
            renderArgs.put("sectiontitle", qp.title);
            renderArgs.put("content", qp.content);
        } else {
            System.out.println(String.format("Content for page %s NULL", pageId));
        }
        renderArgs.put("pageNum", Integer.toString(index+1));
        renderArgs.put("pageTotal", Integer.toString(listing.pageSequence.size()));
        Page page = listing.completedPages.get(index);
        if (page!= null) {
            renderArgs.put("oldValues", page.responses);
        }
        renderTemplate(String.format("QuestionPages/%d.html", pageId));
    }
}
