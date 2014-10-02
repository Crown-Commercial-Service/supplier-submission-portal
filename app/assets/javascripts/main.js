(function () {
  "use strict"
  var root = this,
      $ = root.jQuery;

  if (typeof GOVUK === 'undefined') { root.GOVUK = {}; }
  if (typeof GOVUK.GDM === 'undefined') { root.GOVUK.GDM = {}; }

  var $buttons = $("label.selection-button input");
  GOVUK.selectionButtons($buttons, {
    "focusedClass" : "selection-button-focused",
    "selectedClass" : "selection-button-selected"
  });

  GOVUK.GDM.wordCounter();
  GOVUK.GDM.listEntry();
}).call(this);
