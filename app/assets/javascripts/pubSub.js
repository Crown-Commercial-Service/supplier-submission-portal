// Pubsub messaging between modules

(function (GOVUK, GDM) {

  "use strict";

  var root = this,     // Use root as a replacement for 'window' (prevents conflicts)
      $ = root.jQuery, // Lets you use $ as a shortcut for jQuery
      events = $({});

  GDM.sub = function(name, callback) { // string, string|object

    var label, list = "";

    if (!name) return GDM;

    events.on(name, callback);

    if ("object" === typeof name) {

      for (label in name) list = list + " " + label;

    } else {

      list = name;

    }

    if (GDM.debug) console.log("  %cListening for %c" + list, "color:#d94; background:#fd9; font-size: 11pt", "color:#fea; background:#d94; font-size: 11pt");

    return GDM;

  };

  GDM.pub = function(name, data) {

    var label, list = "";

    if (!name) return GDM;

    if ("object" === typeof name) {

      for (label in name) list = list + " " + label;

    } else {

      list = name;

    }

    if (GDM.debug) console.log("  %c" + list, "color:#bcf; background:#34d; font-size: 11pt");

    events.trigger(name, data);

    return GDM;

  };

  GOVUK.GDM = GDM;

}).apply(this, [GOVUK||{}, GOVUK.GDM||{}]);
