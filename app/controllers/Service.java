package controllers;

import models.Listing;
import play.data.validation.Error;

import java.util.List;
import java.util.Map;

public class Service extends AuthenticatingController {

    public static void newService() {
        renderArgs.put("content", Fixtures.getContentProperties());
        render();
    }

    public static void createListing(String lot) {
        validation.required(lot);
        validation.match(lot, "SaaS|IaaS|PaaS|SCS");

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

        listing.serviceSubmitted = true;
        listing.save();

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

    public static void showDeletePage(Long listingId){
        Listing listing = Listing.getByListingId(listingId);
        renderTemplate(String.format("Service/delete.html"), listing);
    }

    public static void delete(Long listingId){
        Listing listing = Listing.getByListingId(listingId);
        listing.delete();

        redirect("/");
    }
}
