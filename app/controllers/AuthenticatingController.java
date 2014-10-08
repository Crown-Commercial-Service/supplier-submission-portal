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

    @Before
    public static void checkAuthenticationCookie() {
        if(Security.isAuthenticationRequired()){
            Logger.info("Authentication required.");
            Http.Cookie gdmSsoCookie = request.current().cookies.get("gdmssosession");
            Boolean usedRecently = (Cache.get("last-used") != null);

            if(gdmSsoCookie == null){
                Logger.info("SSO Cookie does not exist.");
                redirect(DM_URL + "login");
            } else if (Security.cookieHasExpired(gdmSsoCookie) && !usedRecently){
                Logger.info("SSO Cookie has expired");
                redirect(DM_URL + "login");
            } else {
                supplierDetailsFromCookie.put("supplierId", Security.getCookieSupplierId(gdmSsoCookie));
                supplierDetailsFromCookie.put("supplierEmail", Security.getCookieEmail(gdmSsoCookie));
                supplierDetailsFromCookie.put("supplierCompanyName", Security.getCookieSupplierCompanyName(gdmSsoCookie));

                Cache.set("last-used", new Date(), "60mn");
            }
        } else {
            Logger.info("Authentication not required - using dummy supplier ID.");
            supplierDetailsFromCookie.put("supplierId", "1");
            supplierDetailsFromCookie.put("supplierEmail", "supplier@digital.cabinet-office.gov.uk");
            supplierDetailsFromCookie.put("supplierCompanyName", "SueDo LTD.");
        }
    }
}
