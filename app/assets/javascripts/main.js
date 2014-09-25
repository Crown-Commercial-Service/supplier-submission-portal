(function () {
  "use strict"
  var root = this,
      $ = root.jQuery;

  if (typeof GOVUK === 'undefined') { root.GOVUK = {}; }

  var $buttons = $("label.selection-button input");
  GOVUK.selectionButtons($buttons, {
    "focusedClass" : "selection-button-focused",
    "selectedClass" : "selection-button-selected"
  });

}).call(this);
