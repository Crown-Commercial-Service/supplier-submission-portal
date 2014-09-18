// Load all the things
// - There used to be some Sprockets directives here but now we just
//   pull in everything in the Javascripts directory with Grunt

(function(GOVUK, GDM) {

  "use strict";

  var module,
      templatesLoaded,
      questionsLoaded,
      checkAllLoaded;

  if (
    (GDM.debug = !window.location.href.match(/gov.uk/) && !window.jasmine)
  ) {
    console.log(
      "%cDebug mode %cON",
      "color:#550; background:yellow; font-size: 11pt",
      "color:yellow; background: #550;font-size:11pt"
    );
    console.time("Modules loaded");
  }

  GOVUK.GDM = GDM;
  templatesLoaded = new jQuery.Deferred();
  questionsLoaded = new jQuery.Deferred();

  checkAllLoaded = function () {
    if ((templatesLoaded.state() === 'resolved') && (questionsLoaded.state() === 'resolved')) {
      GOVUK.GDM.pub("assetsLoaded", {});
    }
  };

  GDM.sub("questionsLoaded", function () {
    questionsLoaded.resolve();
  });
  GDM.sub("templatesLoaded", function () {
    templatesLoaded.resolve();
  });

  templatesLoaded.done(checkAllLoaded);
  questionsLoaded.done(checkAllLoaded);

  // Initialise our modules
  for (module in GOVUK.GDM) {

    if (GOVUK.GDM.debug && module !== "debug") {
      console.log(
        "%cLoading module %c" + module,
        "color:#6a6; background:#dfd; font-size: 11pt",
        "color:#dfd; background:green; font-size: 11pt"
      );
    }

    if ("function" === typeof GOVUK.GDM[module].init) {
      // If a module has an init() method then we want that to be called here
      GOVUK.GDM[module].init();
    } else if ("function" === typeof GOVUK.GDM[module]) {
      // If a module doesn't have an interface then call it directly
      GOVUK.GDM[module]();
    }

  }

  if (GOVUK.GDM.debug) console.timeEnd("Modules loaded");

}).apply(this, [GOVUK||{}, GOVUK.GDM||{}]);
