package controllers;

import play.*;
import play.mvc.*;

import java.io.File;
import java.util.*;

import models.*;

public class Application extends Controller {

    public static void listProducts() {
        System.out.println(" ALL " + Listing.all());
        List<Listing> listings = Listing.all();
        render(listings);
    }

    public static void captureProduct() {
        render();
    }

    public static void submitProduct() {
        Listing l = new Listing(params.get("title"), params.get("description"));
        l.insert();
        redirect("/documents");
    }

    public static void captureDocuments() {
        render();
    }

    public static void submitDocuments(String title, File doc) {
        System.out.println("NAME " + title + " " + doc.getName());
        redirect("/products");
    }
}