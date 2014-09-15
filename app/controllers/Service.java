package controllers;

import models.Listing;
import play.mvc.Controller;
import play.data.validation.Error;
import uk.gov.gds.dm.DevUtils;

public class Service extends Controller {
    
    public static void editPage(String id, Integer page) {
        render(id, page);
    }

    public static void summaryPage(String id) {
        render(id);
    }
    
    public static void newService() {
        render();
    }
    
    public static void createListing(String lot) {
        validation.required(lot);
        validation.match(lot, "SaaS|IaaS|PaaS|SCS");
        if(validation.hasErrors()) {
            for(Error error : validation.errors()) {
                System.out.println(error.message());
            }
            //TODO: Show flash error messages in page (add code to main.html to do this?)
            flash.error("Validation failed", validation.errors());
            redirect("/addservice");
        }

        // TODO: Get supplier ID for logged in user
        Listing l = new Listing(DevUtils.randomSupplierId(), params.get("lot"));
        l.insert();
        // TODO: Get next page using page sequence saved in Listing object
        redirect("/");
    }
}
