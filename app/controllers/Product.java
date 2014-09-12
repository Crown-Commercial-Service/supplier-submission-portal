package controllers;

import play.mvc.Controller;

public class Product extends Controller {
    
    public static void editPage(String id, Integer page) {
        render(id, page);
    }

    public static void summaryPage(String id) {
        render(id);
    }
}
