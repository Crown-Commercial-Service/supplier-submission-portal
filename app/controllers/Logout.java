package controllers;

import play.cache.Cache;
import uk.gov.gds.dm.URLTools;

import java.util.Date;

public class Logout extends AuthenticatingController {
    public static void doLogout() {
        response.removeCookie("gdmssosession");
        Cache.clear();
        redirect(URLTools.getDigitalMarketplaceURL());
    }
}
