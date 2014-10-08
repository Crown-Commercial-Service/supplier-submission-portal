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
        Page.initialiseAutoIncrementId();
        loadQuestionPages();
    }
    
    private static void loadQuestionPages() {
        try {
            contentProperties = new Properties();
            InputStream inputStream = Play.classloader.getResourceAsStream("content.properties");
            contentProperties.load(inputStream);

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
