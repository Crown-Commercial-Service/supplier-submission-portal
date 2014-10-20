package controllers;

import models.Listing;
import models.Page;
import play.data.validation.Error;
import uk.gov.gds.dm.ServiceSubmissionJourneyFlows;
import uk.gov.gds.dm.Fixtures;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.List;
import java.util.Map;

public class Service extends AuthenticatingController {

    public static void summaryPage(Long listingId) {
        Listing listing = Listing.getByListingId(listingId);
        List<Long> flow = ServiceSubmissionJourneyFlows.getFlow(listing.lot);
        List<String> optionalQuestions = ServiceSubmissionJourneyFlows.getOptionalQuestions();

        Map<String, String> allAnswers = new HashMap<String, String>();
        for(Page p : listing.completedPages){
            if(p.responses != null){
                allAnswers.putAll(p.responses);
            }
        }

        System.out.println(allAnswers);

        renderArgs.put("content", Fixtures.getContentProperties());
        renderArgs.put("service_id", listingId);
        renderArgs.put("listing", listing);
        renderArgs.put("flow", flow);
        renderArgs.put("maxPossibleNumberOfQuestions", 20);
        renderArgs.put("optionalQuestions", optionalQuestions);
        renderArgs.put("pageIndex", 0);
        renderArgs.put("storedValues", allAnswers);
        render(listingId);
    }

    public static void newService() {
        renderArgs.put("content", Fixtures.getContentProperties());
        render();
    }

    public static void createListing(String lot) {
        validation.required(lot).key("p0q1");
        validation.match(lot, "SaaS|IaaS|PaaS|SCS").key("p0q1");

        if(validation.hasErrors()) {
            for(Map.Entry<String, List<Error>> entry : validation.errorsMap().entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue().get(0).message();

                flash.put(key, value);
            }
            redirect("/addservice");
        }

        Listing listing = new Listing(supplierDetailsFromCookie.get("supplierId"), params.get("lot"));
        listing.insert();

        // TODO: Get next page using page sequence saved in Listing object
        redirect(String.format("/page/%d/%d", listing.firstPage(), listing.id));
    }

    public static void completeListing(Long listingId, String serviceCompleted){

        Listing listing = Listing.getByListingId(listingId);

        validation.required(serviceCompleted);
        if(serviceCompleted != null){
            validation.isTrue(listing.allPagesHaveBeenCompleted()).key("service").message("This service is not complete.");
        }

        if(validation.hasErrors()){
            for(Map.Entry<String, List<Error>> entry : validation.errorsMap().entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue().get(0).message();

                flash.put(key, value);
            }
            redirect(String.format("/service/%d/summary", listingId));
        }

        listing.completeListing(supplierDetailsFromCookie.get("supplierEmail"));

        flash.put("success", "Your service has been marked as completed.");
        redirect("/");
    }

    public static void markListingAsDraft(Long listingId){
        Listing listing = Listing.getByListingId(listingId);
        listing.serviceSubmitted = false;
        listing.save();

        flash.put("success", "Your service has been moved back to draft.");
        redirect("/");
    }

    public static void showDeletePage(Long listingId, String showDeleteMessage){
        Listing listing = Listing.getByListingId(listingId);
        List<Long> flow = ServiceSubmissionJourneyFlows.getFlow(listing.lot);
        List<String> optionalQuestions = ServiceSubmissionJourneyFlows.getOptionalQuestions();

        Map<String, String> allAnswers = new HashMap<String, String>();
        for(Page p : listing.completedPages){
            if(p.responses != null){
                allAnswers.putAll(p.responses);
            }
        }

        System.out.println(allAnswers);

        renderArgs.put("content", Fixtures.getContentProperties());
        renderArgs.put("service_id", listingId);
        renderArgs.put("listing", listing);
        renderArgs.put("flow", flow);
        renderArgs.put("maxPossibleNumberOfQuestions", 20);
        renderArgs.put("optionalQuestions", optionalQuestions);
        renderArgs.put("pageIndex", 0);
        renderArgs.put("storedValues", allAnswers);
        if(showDeleteMessage != null){
            renderArgs.put("confirmDeleteMessage", "Are you sure you want to delete this service?");
        }
        renderTemplate("Service/summaryPage.html", listingId);
    }

    public static void delete(Long listingId){
        Listing listing = Listing.getByListingId(listingId);
        listing.delete();

        redirect("/");
    }
}
