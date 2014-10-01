package uk.gov.gds.dm;

import com.elevenware.util.tokenlib.SimpleAesEncryptor;
import play.mvc.Http;

public class Security {

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

    public static Boolean hasCookieExpired(Http.Cookie cookie){
        //TODO: Do expiration checks
        return false;
    }
}
