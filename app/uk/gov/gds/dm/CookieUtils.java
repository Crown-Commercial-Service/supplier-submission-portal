package uk.gov.gds.dm;

import org.apache.commons.lang.StringEscapeUtils;
import play.Play;
import play.mvc.Http;

import java.util.Date;

public class CookieUtils {

    private static final String SSO_COOKIE_NAME = "gdmssosession";
    private static final String DOMAIN = ".digitalmarketplace.service.gov.uk";
    private static final String LOCAL_DOMAIN = "";
    private static final String PATH = "";
    private static final int COOKIE_TTL = 86400;
    private static final int COOKIE_EXPIRE = 0;
    private static final boolean HTTP_ONLY = true;

    public static void setSSOCookie (String email, String supplierId, String companyName, String eSourcingId, Date timestamp){
        String token = generateEncryptedSSOCookieString(email, supplierId, companyName, eSourcingId, timestamp);

        if(Play.mode == Play.Mode.DEV) {
            Http.Response.current().setCookie(SSO_COOKIE_NAME, token , LOCAL_DOMAIN, PATH, COOKIE_TTL, Security.applicationIsRunningAsSecure(), HTTP_ONLY);
        } else {
            Http.Response.current().setCookie(SSO_COOKIE_NAME, token , DOMAIN, PATH, COOKIE_TTL, Security.applicationIsRunningAsSecure(), HTTP_ONLY);
        }
    }

    public static void clearSSOCookie (){
        String token = generateEncryptedSSOCookieString("cleared@cookie.com", "-1", "Cleared Cookie LTD", "-1", new Date());

        if(Play.mode == Play.Mode.DEV) {
            Http.Response.current().setCookie(SSO_COOKIE_NAME, StringEscapeUtils.escapeJava(token), LOCAL_DOMAIN, PATH, COOKIE_EXPIRE, Security.applicationIsRunningAsSecure(), HTTP_ONLY);
        } else {
            Http.Response.current().setCookie(SSO_COOKIE_NAME, StringEscapeUtils.escapeJava(token), DOMAIN, PATH, COOKIE_EXPIRE, Security.applicationIsRunningAsSecure(), HTTP_ONLY);
        }
    }

    public static String generateEncryptedSSOCookieString(String email, String supplierId, String companyName, String eSourcingId, Date timestamp){
            String[] unencrypted = { email, supplierId, companyName, timestamp.toGMTString(), eSourcingId };
            return Security.encrypt(unencrypted);
    }

}
