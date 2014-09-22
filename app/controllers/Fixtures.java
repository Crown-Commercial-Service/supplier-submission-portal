package controllers;

import models.QuestionPage;
import play.modules.siena.SienaFixtures;
import play.mvc.Controller;
import siena.Model;

public class Fixtures extends Controller {

    public static void loadQuestionPages() {
        SienaFixtures.delete(QuestionPage.class);
        Model.all(QuestionPage.class).delete();
        SienaFixtures.loadModels("../content/digital-marketplace-ssp-content/service/testService-type-IaaS.yml");
        for (QuestionPage qp : Model.all(QuestionPage.class).fetch()) {
            System.out.println(qp);
        }
        redirect("/");
    }
}
