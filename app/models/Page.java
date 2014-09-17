package models; 

import siena.*;
import siena.embed.Embedded;
import siena.embed.EmbeddedMap;

import java.util.HashMap;
import java.util.Map;

@EmbeddedMap
public class Page extends Model {

    // For GAE :
    // 1. @Id annotated field corresponding to the primary key must be Long type
    // 2. @Id annotated field corresponding to the primary key must be called "id"
    @Id(Generator.AUTO_INCREMENT)
    public Long id;

    @Column("pageNumber")
    @NotNull
    public String pageNumber;

    @Column("listingId")
    @NotNull
    public Long listingId;

    @Embedded
    public Map<String,String> responses;

    public Page(Long listingId, String pageNumber) {
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
