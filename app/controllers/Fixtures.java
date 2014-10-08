package controllers;

import models.Page;
import play.Logger;
import play.Play;
import play.mvc.Controller;
import play.utils.Properties;

import java.io.InputStream;

public class Fixtures extends Controller {

    private static Properties contentProperties;

    public static void initialise() {
        Logger.info("Init page numbering...");
        Page.initialiseAutoIncrementId();
        Logger.info("Init content...");
        loadQuestionPages();
        Logger.info("Content loaded, ready to go!");
    }
    
    private static void loadQuestionPages() {
        try {
            Logger.info("Loading question page content...");
            contentProperties = new Properties();
            InputStream inputStream = Play.classloader.getResourceAsStream("content.properties");
            contentProperties.load(inputStream);
            Logger.info("Loaded question page content OK.");

        } catch (Exception ex) {
            Logger.error("Something went wrong loading content: \n" + ex.getMessage());
            notFound();
        }
        redirect("/");
    }

    public static Properties getContentProperties(){
        return contentProperties;
    }
}
