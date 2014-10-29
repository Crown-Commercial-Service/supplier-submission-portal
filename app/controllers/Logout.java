package controllers;

import uk.gov.gds.dm.CookieUtils;
import uk.gov.gds.dm.URLTools;

public class Logout extends AuthenticatingController {
    public static void doLogout() {
        CookieUtils.clearSSOCookie(supplierDetailsFromCookie);
        redirect(URLTools.getDigitalMarketplaceLogoutURL());
    }
}
