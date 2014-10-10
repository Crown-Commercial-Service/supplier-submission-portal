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

public class Page5 extends AuthenticatingController {

    private static final Long PAGE_ID = 5l;

    public static void savePage(Long listingId) {

        Listing listing = Listing.getByListingId(listingId);

        // Extract multiple values for list items
        Map<String, String[]> params = request.params.all();
        ArrayList<String> q1 = new ArrayList();
        ArrayList<String> q2 = new ArrayList();
        String s;

        for (int i=1; i<11; i++) {
            if(params.containsKey("p5q1val" + i)){
                s = params.get("p5q1val" + i)[0];
                if (!Strings.isNullOrEmpty(s)) {
                    validation.maxSize(s, 100).key("p5q1val" + i).message("Too many characters");
                    validation.isTrue(ValidationUtils.isWordCountLessThan(s, 10)).key("p5q1val" + i).message("Too many words");
                    q1.add(s);
                }
            }

            if(params.containsKey("p5q2val" + i)){
                s = params.get("p5q2val" + i)[0];
                if (!Strings.isNullOrEmpty(s)) {
                    validation.maxSize(s, 100).key("p5q2val" + i).message("Too many characters");
                    validation.isTrue(ValidationUtils.isWordCountLessThan(s, 10)).key("p5q2val" + i).message("Too many words");
                    q2.add(s);
                }
            }
        }
        // Validate all fields on this page requiring validation
        validation.isTrue("p5q1", q1.size() > 0).message("validation.required");
        validation.isTrue("p5q2", q2.size() > 0).message("validation.required");

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

        Gson gson = new Gson();
        Page page = new Page(listingId, PAGE_ID);
        page.responses.put("p5q1", gson.toJson(q1));
        page.responses.put("p5q2", gson.toJson(q2));
        page.insert();
        listing.addResponsePage(page, PAGE_ID);
        redirect(listing.nextPageUrl(PAGE_ID, listing.id));
    }
}
