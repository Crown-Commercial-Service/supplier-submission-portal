package uk.gov.gds.dm;


import models.Listing;

import java.util.ArrayList;
import java.util.List;

public class ListingUtils {
    public static List<Listing> getCompletedListings(List<Listing> listings) {
        List<Listing> completedListings = new ArrayList<Listing>();

        for(Listing l : listings){
            if(l.serviceSubmitted){
                completedListings.add(l);
            }
        }
        return completedListings;
    }
}
