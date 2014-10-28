package uk.gov.gds.dm;

import play.Play;

import java.net.HttpURLConnection;
import java.net.URL;

public class URLTools {

    private static final String DM_PROD = "https://www.digitalmarketplace.service.gov.uk/";
    private static final String DM_STAGE = "https://stage.digitalmarketplace.service.gov.uk/";
    private static final String DM_QA = "http://qa.digitalmarketplace.service.gov.uk:8080/";

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

    public static String getDigitalMarketplaceLogoutURL(){
        return getDigitalMarketplaceURL() + "logout";
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
        return DM_PROD;
    }

    private static String getStagingDigitalMarketplaceURL(){
        return  DM_STAGE;
    }

    private static String getQADigitalMarketplaceURL(){
        return DM_QA;
    }

    private static boolean isWorkingURL(String url){
        try {
            final URL testURL = new URL(url);
            final HttpURLConnection huc = (HttpURLConnection) testURL.openConnection();

            huc.setRequestMethod("HEAD");
            huc.connect();

            final int code = huc.getResponseCode();
            return code != 404;
        } catch (Exception e){
            return false;
        }
    }
}
