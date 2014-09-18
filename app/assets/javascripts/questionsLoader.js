(function (GOVUK, GDM) {

  "use strict";

  GDM.questionsLoader = {
    load : function () {

      $.ajax(
        'public/javascripts/ssp.json',
        {
          'cache': false
        }
      ).done(function (data) {

        GDM.questions = data;

        GDM.pub("questionsLoaded"); // Tell the rest of the app we're successful

      });

    },
    init : function () {
      this.load();
    }
  };

  GOVUK.GDM = GDM;

}).apply(this, [GOVUK||{}, GOVUK.GDM||{}]);
