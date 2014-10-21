package models;

import siena.*;
import siena.embed.Embedded;
import siena.embed.EmbeddedMap;

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

    public Page(Long listingId, Long pageNumber) {
        this.id = nextId();
        this.listingId = listingId;
        this.pageNumber = pageNumber;
        responses = new HashMap<String,String>();
    }

    private Page() {

    }

    public static Page emptyPage() {
        return new Page();
    }

    // TODO: Method(s) to store responses into the responses object

    @Override
    public String toString() {
        return "Page{" +
                "id=" + id +
                ", listingId='" + listingId + "'" +
                ", pageNumber='" + pageNumber + "'" +
                ", responses='" + responses + "'" +
                '}';
    }
}
