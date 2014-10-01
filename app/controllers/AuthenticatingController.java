package controllers;

import play.Logger;
import play.mvc.Before;
import play.mvc.Controller;

import play.mvc.Http;
import uk.gov.gds.dm.Security;

import java.util.HashMap;
import java.util.Map;


public abstract class AuthenticatingController extends Controller {

    static Map<String, String> supplierDetailsFromCookie = new HashMap<String, String>();
    static final String DM_URL = "https://digitalmarketplace.service.gov.uk";

    @Before
    public static void checkAuthenticationCookie() {
        Http.Cookie gdmSsoCookie = request.current().cookies.get("gdmssosession");

        // TODO : Need to flesh out cookie expiration method
        if(gdmSsoCookie == null){
            Logger.info("SSO Cookie does not exist.");
            redirect(DM_URL + "/login");
        } else if (Security.hasCookieExpired(gdmSsoCookie)){
            Logger.info("SSO Cookie has expired");
            redirect(DM_URL + "/login");
        } else {
            supplierDetailsFromCookie.put("supplierId", Security.getSupplierIdFromCookie(gdmSsoCookie));
            supplierDetailsFromCookie.put("supplierEmail", Security.getSupplierEmailFromCookie(gdmSsoCookie));
            supplierDetailsFromCookie.put("supplierCompanyName", Security.getSupplierCompanyNameFromCookie(gdmSsoCookie));
        }
    }
}
