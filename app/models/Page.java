package models;

import siena.*;
import siena.embed.Embedded;

import java.util.List;
import java.util.Map;


@Table("page")
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
    public String listingId;

    @Embedded
    public Map<String,String> responses;
    
    public Page(String listingId, String pageNumber) {
        this.listingId = listingId;
        this.pageNumber = pageNumber;
    }

    public static List<Page> allByListingId(String listingId) {
        return Model.all(Page.class).filter("listingId", listingId).fetch();
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
