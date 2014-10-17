package uk.gov.gds.dm;

import com.elevenware.util.tokenlib.SimpleAesEncryptor;
import play.Logger;
import play.Play;
import play.mvc.Http;

import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Security {

    static final long COOKIE_DURATION = TimeUnit.DAYS.toMillis(1);

    public static String  getCookieEmail(Http.Cookie cookie){
        return  decrypt(cookie.value)[0];
    }

    public static String  getCookieSupplierId(Http.Cookie cookie){
        return  decrypt(cookie.value)[1];
    }

    public static String  getCookieSupplierCompanyName(Http.Cookie cookie){
        return  decrypt(cookie.value)[2];
    }

    public static String  getCookieDate(Http.Cookie cookie){
        return  decrypt(cookie.value)[3];
    }

    public static String returnEncryptedCookieValueWithCurrentDate(Http.Cookie cookie) {
        String[] cookieArray = decrypt(cookie.value);
        cookieArray[3] = getDateFormat().format(new Date());

        return encrypt(cookieArray);
    }

    public static String[] decrypt(String encryptedString){
        SimpleAesEncryptor encryptor = new SimpleAesEncryptor("Bar12345Bar12345");

        if(!(System.getProperty("ssp.cookie.enc") == null)){
            encryptor = new SimpleAesEncryptor(System.getProperty("ssp.cookie.enc"));
        }
        return encryptor.decryptArray(encryptedString);
    }

    public static String encrypt(String[] stringArray){
        SimpleAesEncryptor encryptor = new SimpleAesEncryptor("Bar12345Bar12345");

        if(!(System.getProperty("ssp.cookie.enc") == null)){
            encryptor = new SimpleAesEncryptor(System.getProperty("ssp.cookie.enc"));
        }
        return encryptor.encryptArray(stringArray);
    }

    public static Boolean cookieHasExpired(Http.Cookie cookie){
        Date cookieSetDate;

        try {
            cookieSetDate = getDateFormat().parse(getCookieDate(cookie));
        } catch (ParseException pe){
            Logger.error("Incoming cookie date (%s) could not be parsed. Error: %s" , getCookieDate(cookie), pe.getMessage());
            return true;
        }

        Long timeElapsed = (new Date().getTime() - cookieSetDate.getTime());
        System.out.println("Time elapsed: " + timeElapsed + "  COOKIE DURATION: " + COOKIE_DURATION);
        return(timeElapsed > COOKIE_DURATION);
    }

    public static SimpleDateFormat getDateFormat(){
        return new SimpleDateFormat("d MMM yyyy HH:mm:ss z");
    }

    public static Boolean isAuthenticationRequired(){
        if(Play.mode == Play.Mode.DEV){
            return false;
        }

        String appName = Play.configuration.getProperty("application.name");

        if(appName.equals("ssp-preview") || appName.equals("ssp-staging")){
            return false;
        } else {
            return true;
        }
    }

    public static Boolean supplierIdIsAllowed(String supplierId){
        String appName = Play.configuration.getProperty("application.name");
        if(appName.equals("ssp-live")){
            if(supplierId.equals("577184")){
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }
}
