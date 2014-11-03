package controllers;

import org.joda.time.DateTime;
import play.Logger;
import play.Play;
import play.mvc.*;

import uk.gov.gds.dm.CookieUtils;
import uk.gov.gds.dm.EmailSupport;
import uk.gov.gds.dm.Security;
import uk.gov.gds.dm.URLTools;

public abstract class AuthenticatingController extends Controller {

    static final String DM_URL = URLTools.getDigitalMarketplaceURL();

    @Catch(value = Throwable.class, priority = 1)
    public static void logThrowable(Throwable throwable) {
        if (Play.mode.isProd())
            EmailSupport.sendMail(String.format("Exception thrown time %s path %s method %s message %s", request.path, request.method, new DateTime(), throwable.getMessage()));
    }

    @Finally
    static void log() {
        Logger.info(String.format("Request: %s method %s, status %s supplier %s", request.path, request.method, response.status, getSupplierName()));
    }

    @Before
    public static void checkAuthenticationCookie() {
        if (Security.isAuthenticationRequired()) {
            doAuthenticationChecks();
        }
    }

    private static void doAuthenticationChecks() {
        try {
            Http.Cookie gdmSsoCookie = request.current().cookies.get("gdmssosession");

            if (gdmSsoCookie == null) {
                Logger.info("SSO Cookie does not exist.");
                redirect(DM_URL + "login");
            } else if (Security.cookieHasExpired(gdmSsoCookie)) {
                Logger.info("SSO Cookie has expired.");
                redirect(DM_URL + "login");
            } else if (!Security.supplierIsAllowed(Security.getCookieSupplierId(gdmSsoCookie), Security.getCookieEmail(gdmSsoCookie))) {
                Logger.info("Supplier id (" + Security.getCookieSupplierId(gdmSsoCookie) + ") was not allowed.");
                redirect(DM_URL + "login");
            } else {
                CookieUtils.updateSSOCookieWithCurrentTimestamp(gdmSsoCookie);
            }
         } catch (Exception e){
            Logger.error("Cookie was encrypted using an invalid encryption key.");
            CookieUtils.clearSSOCookie();
            redirect(URLTools.getDigitalMarketplaceLogoutURL());
         }
    }

    protected static String getSupplierId() {
        if (Security.isAuthenticationRequired()) {
            return Security.getCookieSupplierId(request.current().cookies.get("gdmssosession"));
        } else {
            return "1";
        }
    }

    protected static String getSupplierName() {
        if (Security.isAuthenticationRequired()) {
            return Security.getCookieSupplierCompanyName(request.current().cookies.get("gdmssosession"));
        } else {
            return "SueDo LTD.";
        }
    }

    protected static String getSupplierEmail() {
        if (Security.isAuthenticationRequired()) {
            return Security.getCookieEmail(request.current().cookies.get("gdmssosession"));
        } else {
            return "supplier@digital.cabinet-office.gov.uk";
        }
    }

    protected static String getSupplierEsourcingId() {
        if (Security.isAuthenticationRequired()) {
            return Security.getESourcingId(request.current().cookies.get("gdmssosession"));
        } else {
            return "999999";
        }
    }
}
