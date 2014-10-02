package controllers;

public class Logout extends AuthenticatingController {
    public static void doLogout() {
        response.removeCookie("gdmssosession");
        redirect("https://digitalmarketplace.service.gov.uk");
    }
}
