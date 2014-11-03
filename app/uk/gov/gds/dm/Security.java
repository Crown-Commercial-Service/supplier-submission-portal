package uk.gov.gds.dm;

import com.elevenware.util.tokenlib.SimpleAesEncryptor;
import org.apache.commons.lang.StringEscapeUtils;
import play.Logger;
import play.Play;
import play.libs.Crypto;
import play.mvc.Http;

import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class Security {

    private static final int EMAIL = 0;
    private static final int SUPPLIER_ID = 1;
    private static final int COMPANY_NAME = 2;
    private static final int CREATED_DATE = 3;
    private static final int ESOURCING_ID = 4;

    static final long COOKIE_DURATION = TimeUnit.DAYS.toMillis(1);

    public static String  getCookieEmail(Http.Cookie cookie){
        return  decrypt(cookie.value)[EMAIL];
    }

    public static String  getCookieSupplierId(Http.Cookie cookie){
        return  decrypt(cookie.value)[SUPPLIER_ID];
    }

    public static String  getCookieSupplierCompanyName(Http.Cookie cookie){
        return  decrypt(cookie.value)[COMPANY_NAME];
    }

    public static String  getCookieDate(Http.Cookie cookie){
        return  decrypt(cookie.value)[CREATED_DATE];
    }

    public static String getESourcingId(Http.Cookie cookie) {
        String[] tokens = decrypt(cookie.value);
        if(tokens.length < 5) {
            return "";
        }
        return tokens[ESOURCING_ID];
    }

    public static String returnEncryptedCookieValueWithCurrentDate(Http.Cookie cookie) {
        String[] cookieArray = decrypt(cookie.value);
        cookieArray[3] = getDateFormat().format(new Date());

        return encrypt(cookieArray);
    }

    public static String[] decrypt(String encryptedString){
        SimpleAesEncryptor simpleAesEncryptor;

        if(!(System.getProperty("ssp.cookie.enc") == null)){
            simpleAesEncryptor = new SimpleAesEncryptor(System.getProperty("ssp.cookie.enc"));
        } else {
            Logger.error("System is using the default encryption key, please change.");
            simpleAesEncryptor = new SimpleAesEncryptor("Bar12345Bar12345");
        }

        return simpleAesEncryptor.decryptArray(StringEscapeUtils.unescapeJava(encryptedString));
    }

    public static String encrypt(String[] stringArray){
        SimpleAesEncryptor simpleAesEncryptor;

        if(!(System.getProperty("ssp.cookie.enc") == null)){
            simpleAesEncryptor = new SimpleAesEncryptor(System.getProperty("ssp.cookie.enc"));
        } else {
            simpleAesEncryptor = new SimpleAesEncryptor("Bar12345Bar12345");
        }

        return simpleAesEncryptor.encryptArray(stringArray);
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

    public static Boolean supplierIsAllowed(String supplierId, String supplierEmail){
        String appName = Play.configuration.getProperty("application.name");
        if(appName.equals("ssp-live")){
            return (liveSupplierIdOk(supplierId) || liveSupplierEmailOk(supplierEmail));
        } else if (appName.equals("ssp-qa")) {
            return qaSupplierEmailOk(supplierEmail);
        } else {
            return true;
        }
    }

    private static Boolean liveSupplierIdOk(String supplierId) {
        return Arrays.asList(new String[] {
            "577184"
        }).contains(supplierId);
    }

    private static Boolean liveSupplierEmailOk(String supplierEmail) {
        return Arrays.asList(new String[] {
            "brendan.short+test@digital.cabinet-office.gov.uk"
        }).contains(supplierEmail);
    }

    private static Boolean qaSupplierEmailOk(String supplierEmail) {
        return Pattern.compile(
            "digital-marketplace-development\\+test-supplier-\\d+@digital.cabinet-office.gov.uk"
        ).matcher(supplierEmail).matches();
    }

    public static boolean applicationIsRunningAsSecure() {
        Http.Request httpRequest = Http.Request.current();
        return httpRequest.secure;
    }
}
