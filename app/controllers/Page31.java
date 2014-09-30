package controllers;

import models.Listing;
import models.Page;
import play.mvc.Controller;

<<<<<<< HEAD
import play.data.validation.*;
import play.data.validation.Error;

import java.util.ArrayList;
import java.util.Map;
import java.util.List;

public class Page31 extends Controller {
=======
public class Page31 extends AuthenticatingController {
>>>>>>> d339c59... Added cookie authentication to all controllers + check supplier ids match owner

    private static final Long PAGE_ID = 31l;

    public static void savePage(Long listingId, String p31q1, String p31q2, String p31q3, String p31q4, String p31q5) {

        Listing listing = Listing.getByListingId(listingId);

        // TODO: Validate all fields on this page requiring validation
        validation.required(p31q1).key("p31q1");
        validation.required(p31q2).key("p31q2");
        validation.required(p31q3).key("p31q3");
        validation.required(p31q4).key("p31q4");
        if (!listing.lot.equals("SaaS")) {
            validation.required(p31q5).key("p31q5");
        }

        if(validation.hasErrors()) {
            //flash.error("%s", validation.errors());

            for(Map.Entry<String, List<Error>> entry : validation.errorsMap().entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue().get(0).message();

                flash.put(key, value);
            }
            System.out.println(flash);
            redirect(String.format("/page/%d/%d", PAGE_ID, listing.id));
        }

        Page page = new Page(listingId, PAGE_ID);
        page.responses.put("p31q1", p31q1);
        page.responses.put("p31q2", p31q2);
        page.responses.put("p31q3", p31q3);
        page.responses.put("p31q4", p31q4);
        page.responses.put("p31q5", p31q5);
        page.insert();
        listing.addResponsePage(page, PAGE_ID);
        redirect(listing.nextPageUrl(PAGE_ID, listing.id));
    }

}
