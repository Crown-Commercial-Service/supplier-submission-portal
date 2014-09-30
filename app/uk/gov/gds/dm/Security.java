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

    public static String[] decrypt(String encryptedString){
        return new SimpleAesEncryptor("Bar12345Bar12345").decryptArray(encryptedString);
    }
}
