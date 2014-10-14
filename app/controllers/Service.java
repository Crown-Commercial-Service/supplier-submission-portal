package controllers;

import models.Listing;
import play.data.validation.Error;

import java.util.List;
import java.util.Map;

public class Service extends AuthenticatingController {

    public static void editPage(String id, Integer page) {
        render(id, page);
    }

    public static void summaryPage(String id) {
        render(id);
    }

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

    public static void submissionComplete(String listingId) {
        // TODO: Add flash message to say "All questions complete"
        redirect(String.format("/service/%d", listingId));
    }
}
