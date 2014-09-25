package controllers;

import models.Page;
import play.Play;
import play.modules.siena.SienaFixtures;
import play.mvc.Controller;
import play.utils.Properties;
import siena.Model;

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
            InputStream inputStream = Play.classloader.getResourceAsStream("digital-marketplace-ssp-content/content.properties");
            contentProperties.load(inputStream);

        } catch (Exception ex) {
            //
            System.out.println("Something went wrong: \n" + ex.getMessage());
            notFound();
        }
        redirect("/");
    }

    public static Properties getContentProperties(){
        return contentProperties;
    }
}
