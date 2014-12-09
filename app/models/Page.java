package models;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import siena.*;
import siena.embed.Embedded;
import siena.embed.EmbeddedMap;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@EmbeddedMap
public class Page extends Model {

    private static long autoIncrementId = 12345;

    private static long nextId() {
        return autoIncrementId++;
    }

    public static void initialiseAutoIncrementId() {
        Page pageWithHighestId =  Model.all(Page.class).order("-id").get();
        if (pageWithHighestId != null) {
            autoIncrementId = pageWithHighestId.id + 1;
        }
    }

    // For GAE :
    // 1. @Id annotated field corresponding to the primary key must be Long type
    // 2. @Id annotated field corresponding to the primary key must be called "id"
    @Id(Generator.NONE)
    public Long id;

    @Column("pageNumber")
    @NotNull
    public Long pageNumber;

    @Column("listingId")
    @NotNull
    public Long listingId;

    @Embedded
    public Map<String,String> responses;

    @Embedded
    public Map<String, Document> submittedDocuments;
    
    public Page(Long listingId, Long pageNumber) {
        this.id = nextId();
        this.listingId = listingId;
        this.pageNumber = pageNumber;
        responses = new HashMap<String,String>();
        submittedDocuments = new HashMap<String, Document>();
    }

    private Page() {

    }

    public static Page emptyPage() {
        return new Page();
    }

    public Map<String, Collection<String>> getUnflattenedResponses () {
        Map<String, Collection<String>> toReturn = new HashMap();
        Gson gson = new Gson();
        for(String key : this.responses.keySet()) {
            String val = this.responses.get(key);
            ArrayList<String> list = new ArrayList();

            if (val == null || val.isEmpty()){
                toReturn.put(key, list);
            } else if (val.startsWith("[\"")) {
                Type collectionType = new TypeToken<Collection<String>>(){}.getType();
                Collection<String> vals = gson.fromJson(val, collectionType);
                String emptyElement = "";
                // Pad with empty element so that 'real' arrays always have
                // a length > 1
                vals.add(emptyElement);
                toReturn.put(key, vals);
            } else {
                list.add(val);
                toReturn.put(key, list);
            }
        }
        return toReturn;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
