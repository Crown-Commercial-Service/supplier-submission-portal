package models;

import java.util.List;

import siena.*;
import siena.embed.Embedded;
import uk.gov.gds.dm.ServiceSubmissionJourneyFlows;

import static siena.Json.*;


@Table("listing")
public class Listing extends Model {
    
    // For GAE :
    // 1. @Id annotated field corresponding to the primary key must be Long type
    // 2. @Id annotated field corresponding to the primary key must be called "id"
    @Id(Generator.AUTO_INCREMENT)
    public Long id;

    @Column("supplierId")
    @NotNull
    public String supplierId;

    @Column("lot")
    @Max(4)
    @NotNull
    public String lot;

    @Column("title")
    @Max(200)
    @NotNull
    public String title;

    @Column("description")
    @Max(200)
    @NotNull
    public String description;

    @Embedded
    public List<String> pageSequence;
    
    @Embedded
    public List<Page> completedPages;
    
    public Listing(String supplierId, String lot) {
        this.supplierId = supplierId;
        this.lot = lot;
        pageSequence = ServiceSubmissionJourneyFlows.getFlow(lot);
    }

//    public static List<Listing> all() {
//        return Model.all(Listing.class).fetch();
//    }

    public static List<Listing> allBySupplierId(String supplierId) {
        return Model.all(Listing.class).filter("supplierId", supplierId).fetch();
    }

    @Override
    public String toString() {
        return "Listing{" +
                "id=" + id +
                ", supplierId='" + supplierId + "'" +
                ", lot='" + lot + "'" +
                ", title='" + title + "'" +
                ", description='" + description + "'" +
                ", pageSequence='" + pageSequence + "'" +
                '}';
    }
}
