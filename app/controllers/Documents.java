package controllers;

import play.mvc.*;
import java.io.File;

public class Documents extends Controller{

    public static void captureDocuments() {
        render();
    }

    public static void submitDocuments(String title, File doc) {
        System.out.println("DOCUMENT NAME " + title + " " + doc.getName());
        redirect("/");
    }
}
