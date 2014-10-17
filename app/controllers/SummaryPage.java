package controllers;

public class SummaryPage extends AuthenticatingController {
    public static void showPage(Long listingId){
        renderArgs.put("listingId", listingId);
        renderTemplate(String.format("Service/summaryPage.html"));
    }
}
