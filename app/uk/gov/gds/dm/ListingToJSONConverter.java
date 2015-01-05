package uk.gov.gds.dm;

import com.google.gson.Gson;
import models.Document;
import models.Listing;
import models.Page;
import play.Logger;

public class ListingToJSONConverter {
    
    public final static String JSON_TRUE = "true";
    public final static String JSON_FALSE = "false";
    
    private final static Gson gson = new Gson();
    
    public static String convertToJson(Listing listing) {
        StringBuilder sb = new StringBuilder("{");
        sb.append("\"id\" : ").append(listing.id).append(",");
        sb.append("\"supplierId\" : ").append(listing.supplierId).append(",");
        sb.append("\"lot\" : ").append(gson.toJson(listing.lot)).append(",");
        sb.append("\"title\" : ").append(gson.toJson(listing.title)).append(",");
        sb.append("\"lastUpdated\" : ").append(gson.toJson(listing.getLastUpdated())).append(",");
        sb.append("\"lastUpdatedByEmail\" : ").append(gson.toJson(listing.lastUpdatedEmail)).append(",");
        sb.append("\"lastCompleted\" : ").append(gson.toJson(listing.getLastCompleted())).append(",");
        sb.append("\"lastCompletedByEmail\" : ").append(gson.toJson(listing.lastCompletedByEmail)).append(",");
        
        for (Page page : listing.completedPages) {
            sb.append(getPageJSON(page));
        }
        // Remove trailing comma
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 1);
        }
        sb.append("}");

        return sb.toString();
    }

    private static String getPageJSON(Page page) {
        StringBuilder sb = new StringBuilder();
        try {
            for (String oldkey : page.responses.keySet()) {

                // Assurance responses are serialised along with the response they relate to
                if (oldkey.endsWith("assurance"))
                    continue;

                String val = page.responses.get(oldkey);
                // NULL values occur for questions on a page that do not apply to the current lot.
                if (val == null)
                    continue;

                String newkey = KeyMapper.KEYS_MAP.get(oldkey);
                String value = "";
                if (newkey != null) {
                    value = val;
                } else {
                    Logger.error("NO NEW KEY FOUND FOR OLD KEY: %s", oldkey);
                }

                if (KeyMapper.ARRAYS_OF_STRING.contains(oldkey) || KeyMapper.NUMBERS.contains(oldkey)) {
                    // String arrays and numbers are already formatted correctly
                } else if (KeyMapper.STRINGS.contains(oldkey)) {
                    value = gson.toJson(val);
                } else if (KeyMapper.BOOLEANS.contains(oldkey)) {
                    value = convertToBoolean(val, page);
                } else if (KeyMapper.ARRAYS_OF_STRING_WITH_ASSURANCE.contains(oldkey)) {
                    String assurance = page.responses.get(oldkey + "assurance");
                    value = "{\"value\":" + val + ", \"assurance\":" + gson.toJson(assurance) + "}";
                } else if (KeyMapper.CONVERT_TO_ARRAY_OF_STRING_WITH_ASSURANCE.contains(oldkey)) {
                    String assurance = page.responses.get(oldkey + "assurance");
                    value = "{\"value\":[" + gson.toJson(val) + "], \"assurance\":" + gson.toJson(assurance) + "}";
                } else if (KeyMapper.STRINGS_WITH_ASSURANCE.contains(oldkey)) {
                    String assurance = page.responses.get(oldkey + "assurance");
                    value = "{\"value\":" + gson.toJson(val) + ", \"assurance\":" + gson.toJson(assurance) + "}";
                } else if (KeyMapper.BOOLEANS_WITH_ASSURANCE.contains(oldkey)) {
                    String assurance = page.responses.get(oldkey + "assurance");
                    value = "{\"value\":" + convertToBoolean(val, page) + ", \"assurance\":" + gson.toJson(assurance) + "}";
                } else if (KeyMapper.NUMBERS_WITH_ASSURANCE.contains(oldkey)) {
                    String assurance = page.responses.get(oldkey + "assurance");
                    value = "{\"value\":" + val + ", \"assurance\":" + gson.toJson(assurance) + "}";
                } else {
                    Logger.error("KEY TYPE NOT FOUND IN MAPPER: %s", oldkey);
                }

                if (value != null && !value.isEmpty()) {
                    //System.out.println(oldkey + " --> " + newkey + " = " + value);
                    sb.append("\"").append(newkey).append("\" : ").append(value).append(",");
                }
            }
            for (Document doc : page.submittedDocuments.values()) {
                String newkey = KeyMapper.KEYS_MAP.get(doc.questionId) + "URL";
                sb.append("\"").append(newkey).append("\" : ").append(gson.toJson(doc.documentUrl)).append(",");
            }
        } catch (Exception ex) {
            Logger.error(ex, "ERROR EXPORTING PAGE TO JSON; ListingId=%d; PageNumber=%d", page.listingId, page.pageNumber);
        }
        return sb.toString();
    }

    private static String convertToBoolean(String val, Page page) {
        if (val.equalsIgnoreCase("yes")) {
            return JSON_TRUE;
        } else if (val.equalsIgnoreCase("no")) {
            return JSON_FALSE;
        } else {
            Logger.error("Could not convert to Boolean from string: '%s' in listing '%d', page '%d'; setting False", val, page.listingId, page.pageNumber);
            return JSON_FALSE;
        }
    }
}
