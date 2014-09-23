package models;

import java.util.ArrayList;
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
    public List<Long> pageSequence;
    
    @Embedded
    public List<Page> completedPages;
    
    public Listing(String supplierId, String lot) {
        this.supplierId = supplierId;
        this.lot = lot;
        pageSequence = ServiceSubmissionJourneyFlows.getFlow(lot);
        int size = pageSequence.size();
        completedPages = new ArrayList<Page>(size);
        for (int i=0; i< size; i++) {
            completedPages.add(Page.emptyPage());
        }
        System.out.println("Created Completed: " + completedPages);
    }

    public String nextPageUrl(Long currentPage, Long listingId) {
        Long nextPage = nextPage(currentPage);
        if (nextPage < 0) {
            return String.format("/page/finished/%d", listingId);
        }
        else {
            return String.format("/page/%d/%d", nextPage, listingId);
        }
    }

    public Long firstPage() {
        return pageSequence.get(0);
    }

    public int completedPageCount() {
        int completed = 0;
        for (Page p : completedPages) {
            if (p.pageNumber != 0) {
                completed++;
            }
        }
        return completed;
    }

    public void addResponsePage(Page page, Long pageId) {
        int index = pageSequence.indexOf(pageId);
        Page p = completedPages.get(index);
        if (p != null) {
            p.delete();
            completedPages.remove(index);
        }
        completedPages.add(index, page);
        update();
    }
    
    public boolean isFullyCompleted() {
        return pageSequence.size() == completedPageCount();
    }
    
    public static List<Listing> allBySupplierId(String supplierId) {
        return Model.all(Listing.class).filter("supplierId", supplierId).fetch();
    }

    public static Listing getByListingId(Long listingId) {
        return Listing.getByKey(Listing.class, listingId);
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

    private Long nextPage(Long currentPage) {
        int index = pageSequence.indexOf(currentPage);
        if (index == pageSequence.size()-1) {
            // End of questions
            return -1l;
        } else {
            return pageSequence.get(index+1);
        }
    }

}
