package controllers;

import play.Logger;
import play.mvc.Before;
import play.mvc.Controller;

import uk.gov.gds.dm.Security;

import java.util.HashMap;
import java.util.Map;


public abstract class AuthenticatingController extends Controller {

    static Map<String, String> supplierDetailsFromCookie = new HashMap<String, String>();
    static final String DM_URL = "https://digitalmarketplace.service.gov.uk";

    @Before
    public static void checkAuthenticationCookie() {

        if(request.current().cookies.get("gdmssosession")!=null){
            supplierDetailsFromCookie.put("supplierId", Security.getSupplierIdFromCookie(request.cookies.get("gdmssosession")));
            supplierDetailsFromCookie.put("supplierEmail", Security.getSupplierEmailFromCookie(request.cookies.get("gdmssosession")));
            supplierDetailsFromCookie.put("supplierCompanyName", Security.getSupplierCompanyNameFromCookie(request.cookies.get("gdmssosession")));
        }
        else{
            Logger.info("SSO Cookie does not exist / has expired");
            redirect(DM_URL + "/login");
        }
    }
}
