package uk.gov.gds.dm;

import com.elevenware.util.tokenlib.SimpleAesEncryptor;
import play.Logger;
import play.mvc.Http;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Security {

    static final long COOKIE_DURATION = TimeUnit.DAYS.toMillis(1);

    public static String  getSupplierEmailFromCookie(Http.Cookie cookie){
        return  decrypt(cookie.value)[0];
    }

    public static String  getSupplierIdFromCookie(Http.Cookie cookie){
        return  decrypt(cookie.value)[1];
    }

    public static String  getSupplierCompanyNameFromCookie(Http.Cookie cookie){
        return  decrypt(cookie.value)[2];
    }

    public static String  getSetDateFromCookie(Http.Cookie cookie){
        return  decrypt(cookie.value)[3];
    }

    public static String[] decrypt(String encryptedString){
        SimpleAesEncryptor encryptor = new SimpleAesEncryptor("Bar12345Bar12345");

        if(!(System.getProperty("ssp.cookie.enc") == null)){
            encryptor = new SimpleAesEncryptor(System.getProperty("ssp.cookie.enc"));
        }
        return encryptor.decryptArray(encryptedString);
    }

    public static Boolean cookieHasExpired(Http.Cookie cookie){
        Date cookieSetDate;

        try {
            cookieSetDate = new SimpleDateFormat("d MMM yyyy HH:mm:ss z").parse(getSetDateFromCookie(cookie));
        } catch (ParseException pe){
            Logger.error("Incoming cookie date (%s) could not be parsed. Error: %s" , getSetDateFromCookie(cookie), pe.getMessage());
            return true;
        }

        Long timeElapsed = (new Date().getTime() - cookieSetDate.getTime());
        System.out.println(timeElapsed);
        return(timeElapsed > COOKIE_DURATION);
    }
}
