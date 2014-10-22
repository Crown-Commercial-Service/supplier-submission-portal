package controllers;
import play.Logger;
import play.mvc.Before;
import play.mvc.Controller;

import play.mvc.Http;
import uk.gov.gds.dm.CookieUtils;
import uk.gov.gds.dm.Security;
import uk.gov.gds.dm.URLTools;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public abstract class AuthenticatingController extends Controller {

    static Map<String, String> supplierDetailsFromCookie = new HashMap<String, String>();
    static final String DM_URL = URLTools.getDigitalMarketplaceURL();
    public static final String COOKIE_DATE = "cookieDate";
    private static final String ESOURCING_ID = "esourcingId";
    private static final String SUPPLIER_ID = "supplierId";
    private static final String SUPPLIER_EMAIL = "supplierEmail";
    private static final String SUPPLIER_COMPANY_NAME = "supplierCompanyName";

    @Before
    public static void checkAuthenticationCookie() {
        if(Security.isAuthenticationRequired()){
            doAuthenticationChecks();
        } else {
            populateMapWithFakeCookieData();
        }
    }

    private static void doAuthenticationChecks() {
        Http.Cookie gdmSsoCookie = request.current().cookies.get("gdmssosession");

        if(gdmSsoCookie == null){
            Logger.info("SSO Cookie does not exist.");
            redirect(DM_URL + "login");
        } else if (Security.cookieHasExpired(gdmSsoCookie)){
            Logger.info("SSO Cookie has expired.");
            redirect(DM_URL + "login");
        } else if (!Security.supplierIdIsAllowed(Security.getCookieSupplierId(gdmSsoCookie))) {
            Logger.info("Supplier id was not allowed.");
            redirect(DM_URL + "login");
        } else {
            populateMapWithSSOCookieData(gdmSsoCookie);
            CookieUtils.updateSSOCookieWithCurrentTimestamp(supplierDetailsFromCookie);
        }
    }

    private static void populateMapWithSSOCookieData(Http.Cookie cookie){
        supplierDetailsFromCookie.put(SUPPLIER_ID, Security.getCookieSupplierId(cookie));
        supplierDetailsFromCookie.put(SUPPLIER_EMAIL, Security.getCookieEmail(cookie));
        supplierDetailsFromCookie.put(SUPPLIER_COMPANY_NAME, Security.getCookieSupplierCompanyName(cookie));
        supplierDetailsFromCookie.put(COOKIE_DATE, Security.getCookieDate(cookie));
        supplierDetailsFromCookie.put(ESOURCING_ID, Security.getESourcingId(cookie));
     }

    private static void populateMapWithFakeCookieData(){
        supplierDetailsFromCookie.put(SUPPLIER_ID, "1");
        supplierDetailsFromCookie.put(SUPPLIER_EMAIL, "supplier@digital.cabinet-office.gov.uk");
        supplierDetailsFromCookie.put(SUPPLIER_COMPANY_NAME, "SueDo LTD.");
        supplierDetailsFromCookie.put(COOKIE_DATE, new Date().toGMTString());
        supplierDetailsFromCookie.put(ESOURCING_ID, "999999");
     }

    protected static String getSupplierId() {
        return supplierDetailsFromCookie.get(SUPPLIER_ID);
    }

    protected static String getSupplierName() {
        return supplierDetailsFromCookie.get(SUPPLIER_COMPANY_NAME);
    }

    protected static String getEmail() {
        return supplierDetailsFromCookie.get(SUPPLIER_EMAIL);
    }

    protected static String getEsourcingId() {
        return supplierDetailsFromCookie.get(ESOURCING_ID);
    }

}
