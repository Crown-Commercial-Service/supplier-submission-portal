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
            System.out.println("Cookie date: " + Security.getCookieDate(gdmSsoCookie));
        }
    }

    private static void populateMapWithSSOCookieData(Http.Cookie cookie){
        supplierDetailsFromCookie.put("supplierId", Security.getCookieSupplierId(cookie));
        supplierDetailsFromCookie.put("supplierEmail", Security.getCookieEmail(cookie));
        supplierDetailsFromCookie.put("supplierCompanyName", Security.getCookieSupplierCompanyName(cookie));
        supplierDetailsFromCookie.put("cookieDate", Security.getCookieDate(cookie));
        supplierDetailsFromCookie.put("eSourcingId", Security.getCookieEsourcingId(cookie));
    }

    private static void populateMapWithFakeCookieData(){
        supplierDetailsFromCookie.put("supplierId", "1");
        supplierDetailsFromCookie.put("supplierEmail", "supplier@digital.cabinet-office.gov.uk");
        supplierDetailsFromCookie.put("supplierCompanyName", "SueDo LTD.");
        supplierDetailsFromCookie.put("cookieDate", new Date().toGMTString());
        supplierDetailsFromCookie.put("eSourcingId", "999999");
    }
}
