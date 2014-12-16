package models;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import com.google.gson.Gson;
import play.Logger;
import siena.*;
import siena.embed.Embedded;
import uk.gov.gds.dm.ServiceSubmissionJourneyFlows;

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

    @NotNull
    public boolean serviceSubmitted;

    @NotNull
    public Date lastUpdated;

    @NotNull
    public String lastUpdatedEmail;

    public String lastCompletedByEmail = "";
    public Date lastCompleted;

    @Embedded
    public List<Long> pageSequence;

    @Embedded
    public List<Page> completedPages;

    public Listing(String supplierId, String lot) {
        this.supplierId = supplierId;
        this.lot = lot;
        pageSequence = ServiceSubmissionJourneyFlows.getFlow(lot);
        completedPages = new ArrayList<Page>();
        this.serviceSubmitted = false;
        this.lastUpdated = new Date();
    }

    public Page getResponsePageByPageId(Long page_id) throws Exception {
        Page returnPage = null;
        int counter = 0;
        for(Page p : completedPages){
            if(p.pageNumber.equals(page_id)){
                returnPage = p;
                counter++;
            }
        }

        if(counter > 1){
            Logger.error("There are duplicate pages in listing id [1]: %s; removing page: %s", id, returnPage.toString());
            completedPages.remove(returnPage);
            update();
            return getResponsePageByPageId(page_id);
        }

        return returnPage;
    }

    public String nextPageUrl(Long currentPage, Long listingId) {
        Long nextPage = nextPage(currentPage);
        if (nextPage < 0) {
            return String.format("/service/%d/summary", listingId);
        }
        else {
            return String.format("/page/%d/%d", nextPage, listingId);
        }
    }

    public String prevPageUrl(Long currentPage, Long listingId) {
        Long prevPage = prevPage(currentPage);
        if (prevPage < 0) {
            return null;
        }
        else {
            return String.format("/page/%d/%d", prevPage, listingId);
        }
    }

    public String summaryPageUrl() {
      return String.format("/service/%d/summary", this.id);
    }

    public String summaryPageUrl(Long pageID) {
      String pageIDAsString = pageID.toString();
      String summaryPageUrl = this.summaryPageUrl();
      return summaryPageUrl + "#page" + pageIDAsString;
    }

    public static List<Listing> allBySupplierId(String supplierId) {
        return Model.all(Listing.class).filter("supplierId", supplierId).fetch();
    }

    public static Listing getByListingId(Long listingId) {
        return Listing.getByKey(Listing.class, listingId);
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

    public int getPageSequenceSize(){
        return pageSequence.size();
    }

    public void addResponsePage(Page page, Long pageId, String updateByEmail) {
        try {
            completedPages.remove(getResponsePageByPageId(pageId));
            completedPages.add(page);
            updateListing(updateByEmail);
        } catch (Exception e){
            Logger.error(e.getMessage());
        }

    }

    public void completeListing(String completedByEmail){
        this.serviceSubmitted = true;
        this.lastCompleted= new Date();
        this.lastCompletedByEmail = completedByEmail;
        update();
    }

    public boolean allPagesHaveBeenCompleted() {
        return pageSequence.size() <= completedPageCount();
    }

    public String getLastUpdated(){
        Date d = this.lastUpdated;

        // 10:20am, 8 July 2014
        SimpleDateFormat sdf = new SimpleDateFormat("KK:mmaa, dd MMM yyyy");
        sdf.setTimeZone(TimeZone.getTimeZone("Europe/London"));
        String formattedDateString = sdf.format(d);
        return formattedDateString.replace("AM", "am").replace("PM","pm");
    }

    public String getLastCompleted(){
        if (this.lastCompleted == null) 
            return "";
        
        Date d = this.lastCompleted;

        // 10:20am, 8 July 2014
        SimpleDateFormat sdf = new SimpleDateFormat("KK:mmaa, dd MMM yyyy");
        sdf.setTimeZone(TimeZone.getTimeZone("Europe/London"));
        String formattedDateString = sdf.format(d);
        return formattedDateString.replace("AM", "am").replace("PM","pm");
    }

    public Long getFirstIncompletePage() {

        ArrayList<Long> completedPageNumbers = new ArrayList<Long>();
        ArrayList<Page> toRemove = new ArrayList<Page>();
        for (Page page : completedPages) {
            if (completedPageNumbers.contains(page.pageNumber)) {
                Logger.error("There are duplicate pages in listing id [2]: %s; removing page: %s", id, page.toString());
                toRemove.add(page);
            } else {
                completedPageNumbers.add(page.pageNumber);
            }
        }
        if(!toRemove.isEmpty()) {
            for (Page pageToRemove : toRemove) {
                completedPages.remove(pageToRemove);
            }
            update();
        }
        Collections.sort(completedPageNumbers);
        int i = 0;
        while (i < completedPageNumbers.size() && completedPageNumbers.get(i).equals(pageSequence.get(i))) {
            i++;
        }
        if (i < pageSequence.size()) return pageSequence.get(i);
        else return 0L;
    }

    private void updateListing(String updatedByEmail){
        this.lastUpdated = new Date();
        this.lastUpdatedEmail = updatedByEmail;
        update();
    }

    public Long firstPage() {
        return pageSequence.get(0);
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

    private Long prevPage(Long currentPage) {
        int index = pageSequence.indexOf(currentPage);
        if (index < 1) {
            // Start of questions
            return -1l;
        } else {
            return pageSequence.get(index-1);
        }
    }

    @Override
    public String toString() {
        try {
            Gson gson = new Gson();
            return gson.toJson(this);
        } catch (Exception ex) {
            Logger.error(ex, "Error converting listing to JSON");
            return "{\"ERROR\":\"" + ex.getMessage() + "\"}";
        }
    }
}
