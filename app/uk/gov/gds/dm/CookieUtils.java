package uk.gov.gds.dm;

import org.apache.commons.lang.StringEscapeUtils;
import play.Play;
import play.mvc.Http;

import java.util.Date;

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

    public static void updateSSOCookieWithCurrentTimestamp(Http.Cookie currentCookie){
        setSSOCookie(
                Security.getCookieEmail(currentCookie),
                Security.getCookieSupplierId(currentCookie),
                Security.getCookieSupplierCompanyName(currentCookie),
                new Date(),
                Security.getESourcingId(currentCookie)
        );
    }

    public static void clearSSOCookie(){
        clearSSOCookie("deleted@deleted.com", "-1", "deletedSupplier",
                new Date(), "-1");
    }


    public static String generateEncryptedSSOCookieString(String email, String supplierId, String companyName, String timestamp, String eSourcingId){
        String[] unencrypted = { email, supplierId, companyName, timestamp, eSourcingId };
        return StringEscapeUtils.escapeJava(Security.encrypt(unencrypted));
    }

    public static enum ssoCookieProperties {
        COOKIE_DATE ("cookieDate"), ESOURCING_ID("esourcingId"), SUPPLIER_ID("supplierId"),
        SUPPLIER_EMAIL("supplierEmail"), SUPPLIER_COMPANY_NAME("supplierCompanyName");

        private String key;

        ssoCookieProperties (String key){
            this.key = key;
        }

        @Override
        public String toString() {
            return key;
        }
    }
}
