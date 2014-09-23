package controllers;

import models.Page;
import models.QuestionPage;
import play.modules.siena.SienaFixtures;
import play.mvc.Controller;
import siena.Model;

public class Fixtures extends Controller {

    public static void initialise() {
        Page.initialiseAutoIncrementId();
        loadQuestionPages();
    }
    
    private static void loadQuestionPages() {
        SienaFixtures.delete(QuestionPage.class);
        Model.all(QuestionPage.class).delete();
        SienaFixtures.loadModels("../content/digital-marketplace-ssp-content/question-page-content/service-type-IaaS.yml");
        for (QuestionPage qp : Model.all(QuestionPage.class).fetch()) {
            System.out.println(qp);
        }
        redirect("/");
    }
}
