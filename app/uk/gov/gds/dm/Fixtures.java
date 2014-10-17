package uk.gov.gds.dm;

import models.Page;
import play.Play;
import play.utils.Properties;

import java.io.InputStream;
import java.io.IOException;

public class Fixtures {

    private static Properties contentProperties;

    private static void loadContentProperties() throws IOException {
        contentProperties = new Properties();
        InputStream inputStream = Play.classloader.getResourceAsStream("content.properties");
        contentProperties.load(inputStream);
    }

    public static Properties getContentProperties() {
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
