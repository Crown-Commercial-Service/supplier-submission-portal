package controllers;

import uk.gov.gds.dm.URLTools;

public class Logout extends AuthenticatingController {
    public static void doLogout() {
        response.removeCookie("gdmssosession");
        redirect(URLTools.getDigitalMarketplaceURL());
    }
}
