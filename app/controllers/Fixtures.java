package controllers;

import models.Page;
import play.Logger;
import play.Play;
import play.mvc.Controller;
import play.utils.Properties;

import java.io.InputStream;
import java.io.IOException;

public class Fixtures extends Controller {

    private static Properties contentProperties;

    private static void loadContentProperties() throws IOException {
        contentProperties = new Properties();
        InputStream inputStream = Play.classloader.getResourceAsStream("content.properties");
        contentProperties.load(inputStream);
    }

    public static Properties getContentProperties(){
        if (null == contentProperties) {
            try {
                Page.initialiseAutoIncrementId();
                loadContentProperties();
            } catch (IOException ex) {
                throw new RuntimeException("Failed to load content: " + ex.getMessage());
            }
        }
        return contentProperties;
    }
}
