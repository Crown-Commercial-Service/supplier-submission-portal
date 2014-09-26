package uk.gov.gds.dm;

import com.elevenware.util.tokenlib.SimpleAesEncryptor;
import play.mvc.Http;

/**
 * Created by taosong on 25/09/2014.
 */
public class Security {

    public static String  getSupplierIdFromCookie(Http.Cookie cookie){
        return  decrypt(cookie.value)[1];

    }

    public static String  getSupplierNameFromCookie(Http.Cookie cookie){
        return  decrypt(cookie.value)[0];

    }

    public static String[] decrypt(String encryptedString){
        return new SimpleAesEncryptor("Bar12345Bar12345").decryptArray(encryptedString);
    }
}
