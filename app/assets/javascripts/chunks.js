(function (GOVUK, GDM) {

  "use strict";

  var attach = function() {

      GOVUK.selectionButtons(
        $("label input[type='radio'], label input[type='checkbox']"),
        {
          'selectedClass' : 'optionSelected',
          'focusedClass' : 'optionFocused'
        }
      );

    };

  GDM.chunks = function () {

      GDM.sub("formRendered", attach);

  };

  GOVUK.GDM = GDM;

}).apply(this, [GOVUK||{}, GOVUK.GDM||{}]);
