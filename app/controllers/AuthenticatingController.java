package controllers;

import play.Logger;
import play.cache.Cache;
import play.mvc.Before;
import play.mvc.Controller;

import play.mvc.Http;
import uk.gov.gds.dm.Security;
import uk.gov.gds.dm.URLTools;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public abstract class AuthenticatingController extends Controller {

    static Map<String, String> supplierDetailsFromCookie = new HashMap<String, String>();
    static final String DM_URL = URLTools.getDigitalMarketplaceURL();
    private static final String SUPPLIER_ID = "supplierId";
    private static final String SUPPLIER_EMAIL = "supplierEmail";
    private static final String SUPPLIER_COMPANY_NAME = "supplierCompanyName";

    @Before
    public static void checkAuthenticationCookie() {
        if(Security.isAuthenticationRequired()){
            Http.Cookie gdmSsoCookie = request.current().cookies.get("gdmssosession");
            Boolean usedRecently = (Cache.get("last-used") != null);

            if(gdmSsoCookie == null){
                Logger.info("SSO Cookie does not exist.");
                redirect(DM_URL + "login");
            } else if (Security.cookieHasExpired(gdmSsoCookie) && !usedRecently){
                Logger.info("SSO Cookie has expired");
                redirect(DM_URL + "login");
            } else {
                supplierDetailsFromCookie.put(SUPPLIER_ID, Security.getCookieSupplierId(gdmSsoCookie));
                supplierDetailsFromCookie.put(SUPPLIER_EMAIL, Security.getCookieEmail(gdmSsoCookie));
                supplierDetailsFromCookie.put(SUPPLIER_COMPANY_NAME, Security.getCookieSupplierCompanyName(gdmSsoCookie));

                Cache.set("last-used", new Date(), "60mn");
            }
        } else {
            supplierDetailsFromCookie.put("supplierId", "1");
            supplierDetailsFromCookie.put("supplierEmail", "supplier@digital.cabinet-office.gov.uk");
            supplierDetailsFromCookie.put("supplierCompanyName", "SueDo LTD.");
        }
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


}
