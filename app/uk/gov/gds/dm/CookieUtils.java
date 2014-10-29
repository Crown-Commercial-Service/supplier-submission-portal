package uk.gov.gds.dm;

import org.apache.commons.lang.StringEscapeUtils;
import play.Play;
import play.mvc.Http;

import java.util.Date;
import java.util.Map;

public class CookieUtils {
    private static final String SSO_COOKIE_NAME = "gdmssosession";
    private static final String DOMAIN = ".digitalmarketplace.service.gov.uk";
    private static final String LOCAL_DOMAIN = "";
    private static final String PATH = "/";
    private static final int COOKIE_TTL = 86400;
    private static final int COOKIE_EXPIRE = 0;
    private static final boolean HTTP_ONLY = true;

    public static void setSSOCookie (String email, String supplierId, String companyName, Date timestamp, String eSourcingId){
        String token = generateEncryptedSSOCookieString(email, supplierId, companyName, Security.getDateFormat().format(timestamp), eSourcingId);

        if(Play.mode == Play.Mode.DEV) {
            Http.Response.current().setCookie(SSO_COOKIE_NAME, token , LOCAL_DOMAIN, PATH, COOKIE_TTL, Security.applicationIsRunningAsSecure(), HTTP_ONLY);
        } else {
            Http.Response.current().setCookie(SSO_COOKIE_NAME, token , DOMAIN, PATH, COOKIE_TTL, Security.applicationIsRunningAsSecure(), HTTP_ONLY);
        }
    }

    public static void clearSSOCookie (String email, String supplierId, String companyName, Date timestamp, String eSourcingId){
        String token = generateEncryptedSSOCookieString(email, supplierId, companyName, Security.getDateFormat().format(timestamp), eSourcingId);

        if(Play.mode == Play.Mode.DEV) {
            Http.Response.current().setCookie(SSO_COOKIE_NAME, token , LOCAL_DOMAIN, PATH, COOKIE_EXPIRE, Security.applicationIsRunningAsSecure(), HTTP_ONLY);
        } else {
            Http.Response.current().setCookie(SSO_COOKIE_NAME, token , DOMAIN, PATH, COOKIE_EXPIRE, Security.applicationIsRunningAsSecure(), HTTP_ONLY);
        }
    }

    public static void updateSSOCookieWithCurrentTimestamp(Map<String, String> currentCookieArray){
        setSSOCookie(currentCookieArray.get("supplierEmail"), currentCookieArray.get("supplierId"), currentCookieArray.get("supplierCompanyName"),
                new Date(), currentCookieArray.get("eSourcingId"));
    }

    public static void clearSSOCookie(){
        clearSSOCookie("deleted@deleted.com", "-1", "deletedSupplier",
                new Date(), "-1");
    }


    public static String generateEncryptedSSOCookieString(String email, String supplierId, String companyName, String timestamp, String eSourcingId){
        String[] unencrypted = { email, supplierId, companyName, timestamp, eSourcingId };
        return StringEscapeUtils.escapeJava(Security.encrypt(unencrypted));
    }

}
