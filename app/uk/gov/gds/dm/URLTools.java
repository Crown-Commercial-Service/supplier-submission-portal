package uk.gov.gds.dm;

import play.Play;

import java.net.HttpURLConnection;
import java.net.URL;

public class URLTools {

    public static String getDigitalMarketplaceURL(){
        if(Play.mode == Play.Mode.DEV){
            return getLocalDigitalMarketplaceURL();
        } else {
            String appName = Play.configuration.getProperty("application.name");
            if(appName.equals("ssp-qa")){
                return getQADigitalMarketplaceURL();
            } else if (appName.equals("ssp-staging")){
                return getStagingDigitalMarketplaceURL();
            } else {
                return getProductionDigitalMarketplaceURL();
            }
        }
    }

    private static String getLocalDigitalMarketplaceURL(){
        String[] localDigitalMarketplaceURLs = new String[] {"http://localhost.digitalmarketplace.service.gov.uk:8080/", "http://localhost:8080/", "http://localhost:8080/digitalmarketplace/"};

        if(isWorkingURL(localDigitalMarketplaceURLs[0])){
            return localDigitalMarketplaceURLs[0];
        } else if (isWorkingURL(localDigitalMarketplaceURLs[1])) {
            return localDigitalMarketplaceURLs[1];
        } else {
            return localDigitalMarketplaceURLs[2];
        }
    }

    private static String getProductionDigitalMarketplaceURL(){
        String[] localDigitalMarketplaceURLs = new String[] {"https://digitalmarketplace.service.gov.uk/", "https://digitalmarketplace.service.gov.uk/digitalmarketplace/"};

        if(isWorkingURL(localDigitalMarketplaceURLs[0])){
            return localDigitalMarketplaceURLs[0];
        } else {
            return localDigitalMarketplaceURLs[1];
        }
    }

    private static String getStagingDigitalMarketplaceURL(){
        return  "https://stage.digitalmarketplace.service.gov.uk/";
    }

    private static String getQADigitalMarketplaceURL(){
        return "http://qa.digitalmarketplace.service.gov.uk:8080/";
    }

    private static boolean isWorkingURL(String url){
        try {
            final URL testURL = new URL(url);
            final HttpURLConnection huc = (HttpURLConnection) testURL.openConnection();

            huc.setRequestMethod("HEAD");
            huc.connect();

            final int code = huc.getResponseCode();
            System.out.println("Response code: " + code);
            return code != 404;
        } catch (Exception e){
            return false;
        }
    }
}
