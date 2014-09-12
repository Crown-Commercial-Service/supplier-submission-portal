// Simple functions to scroll the page

(function (GOVUK, GDM) {

  var speed = 0;

  var init = function() {

      GDM.sub(
        "navigateToPage",
        toTop
      );

    },
    toOffset = function(pixelTop) {

      $("html,body")
        .animate(
          {
            scrollTop: pixelTop
          },
          speed
        );

    },
    toElement = function($element) {

      var offset = $element.offset();

      if (offset) toOffset(
        offset.top - parseInt($element.css("margin-top"), 10)
      );

    },
    toElementWithEvent = function(event, $element) {

      toElement($($element));

    },
    toTop = function() {

      toOffset(0);

    };

  GDM.scrollTo = init;
  GOVUK.GDM = GDM;

}).apply(this, [GOVUK||{}, GOVUK.GDM||{}]);
