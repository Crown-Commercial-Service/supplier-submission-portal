package controllers;

import com.google.appengine.repackaged.com.google.common.base.Strings;
import com.google.gson.Gson;
import models.Listing;
import models.Page;
import play.data.validation.Error;
import uk.gov.gds.dm.ValidationUtils;

import java.util.ArrayList;
import java.util.Map;
import java.util.List;

public class Page21 extends AuthenticatingController {

    private static final Long PAGE_ID = 21l;

    public static void savePage(Long listingId) {

        Listing listing = Listing.getByListingId(listingId);

        // Extract multiple values for list items
        Map<String, String[]> params = request.params.all();
        ArrayList<String> p21q1 = new ArrayList();

        String s;

        for (int i=1; i<11; i++) {
            if(params.containsKey("p21q1val" + i)){
                s = params.get("p21q1val" + i)[0];
                if (!Strings.isNullOrEmpty(s)) {
                    validation.maxSize(s, 100).key("p21q1val" + i).message("Too many characters");
                    validation.isTrue(ValidationUtils.isWordCountLessThan(s, 10)).key("p21q1val" + i).message("Too many words");
                    p21q1.add(s);
                }
            }
        }

        // Validate all fields on this page requiring validation
        // TODO: Verify if... validation.required(p21q1).key("p21q1");

        if(validation.hasErrors()) {
            flash.put("body", params.get("body"));
            for(Map.Entry<String, List<Error>> entry : validation.errorsMap().entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue().get(0).message();

                flash.put(key, value);
            }
            System.out.println(flash);
            redirect(String.format("/page/%d/%d", PAGE_ID, listing.id));
        }

        // Save the form data as a Page into the correct page index
        Gson gson = new Gson();
        Page page = new Page(listingId, PAGE_ID);
        page.responses.put("p21q1", gson.toJson(p21q1));
        page.insert();
        listing.addResponsePage(page, PAGE_ID);
        redirect(listing.nextPageUrl(PAGE_ID, listing.id));
    }
}
