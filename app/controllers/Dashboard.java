package controllers;

import models.Listing;
import play.mvc.Controller;

import java.util.List;

public class Dashboard extends Controller {

    public static void home() {
        System.out.println(" ALL " + Listing.all());
        List<Listing> listings = Listing.all();
        render(listings);
    }
}
