/* Validate forms based on classes
// Requires jQuery.classes.js

Documentation: http://37.26.90.29/foswiki/bin/view/DevelopersGuide/HTMLForForms

*/

(function (GOVUK, GDM) {

  "use strict";

  var root = this,
      $ = root.jQuery,
      patterns = {
        // These are generic validations we can bake in. For anything
        // project-specific it's best to inject patterns from a separate file
        // to keep this module reusable
        email: function($formElement) {

          return (/\S+@\S+\.\S+/).test($formElement.val());
          /*     Anything, then '@', then anything, then '.' then anything
          */

        },
        notBlank: function($formElement) {

          return !patterns.isBlank($formElement);

        },
        isBlank: function($formElement) {

          return $.trim($formElement.val()) === ""; // Native trim not supported on all browsers, using jQuery version

        },
        checked: function($formElement) {

          return $formElement.is(":checked"); // This is the reason we can't just pass $formElement.val() to each of these functions

        },
        matches: function($formElement) {

          return $formElement.val() === $("input[name=" + $formElement.data("validate-matches-target") + "]").val(); // TODOUI: document this

        }
      },
      addPatterns = function(event, newPatterns) {

        if ("object" !== typeof newPatterns) throw("Validation: validation patterns must be functions");

        // This will let us do validations specific to our project while keeping this file clean
        $.extend(patterns, newPatterns);

      },
      init = function() {

        $("body")
          .on("submit", validateForm);

        $("body")
          .on("click", ".validation-summary a", jumpToError);

        GDM.sub(
          {
            "accordionClosed": function(event, accordionTarget) {

              $(accordionTarget)
              .find(".client-side")
                .each(clearErrors);

            },
            "test": function() {

              GDM.validationPatterns = patterns; // Expose the patterns when running tests

            },
            "validationPatternsCreated": addPatterns
          }
        );

      },
      insertSummary = function() {

        if ($(this).find(".validation-summary").length) return;

        $(this)
          .prepend(
            $(
              "<ul>",
              {
                "class": "validation-summary",
                "html": "<h2>There was a problem submitting the form</h2><p>Please try the following:</p><ul></ul>" /* TODOUI: content in JS, eww */
              }
            )
          );

      },
      validateForm = function(event) {

        if (
          $("input, select, textarea", this)
            .each(validateFormElements)              // Check if each element has an error
            .filter(".client-side.validation-error") // Get the ones that do have errors,
            .length > 0                              // (if any)
        ) {

          event.preventDefault(); // Stop the submission of the form

          GDM.pub(
            "errorsInserted",
            $(".validation-summary")
          );

        }

      },
      validateFormElements = function() {

        var $formElement = $(this),
            CSSclasses = $formElement.classes() || [],
            validationType;

        clearErrors.apply($formElement);

        for (var i in CSSclasses) {

          if (!isValidValidation(CSSclasses[i])) continue;

          validationType = CSSclasses[i].replace("validate-", "");

          if (
            !patterns[validationType]($formElement)
          ) {

            addErrorMarkup($formElement, validationType);
            break; // Only report the first error for each field

          }

        }

      },
      clearErrors = function() {

        var $formElement = $(this);

        $formElement
        .filter(".client-side")
          .attr("aria-invalid", "false")
          .removeClass("validation-error");

        if ($formElement.parent(".validation-error-wrapper").length) {

          $("#" + $formElement.attr("id") + "Summary")
          .add("label[for=" + $formElement.attr("name") + "].validation-message")
            .remove();

          $formElement
            .unwrap();

        }

      },
      isValidValidation = function(name) {

        return  "string" === typeof name &&
                0 === name.indexOf("validate-", 0) &&
                "function" === typeof patterns[name.replace("validate-", "")];

      },
      addErrorMarkup = function($formElement, validationType) {

        var message = $formElement.data("validate-" + validationType.toLowerCase() + "-message"),
            summary = $formElement.data("validate-" + validationType.toLowerCase() + "-summary");

        $formElement
          .addClass("validation-error client-side")
          .attr("aria-invalid", "true");

        if (undefined === message) return;

        $formElement
          .before(
            $(
              "<label />",
              {
                "class": "validation-message",
                "text": message,
                "for": $formElement.attr("name")
              }
            )
          )
        .add("label[for=" + $formElement.attr("name") + "]")
          .wrapAll(
            $(
              "<div />",
              {
                "class": "validation-error-wrapper"
              }
            )
          );

        if (undefined === summary) return;

        if (undefined === $formElement.attr("id")) throw "Validation: when specifiying a summary message you must give the input/select/textarea element an ID";

        $formElement
          .parents("form")
            .each(insertSummary)
          .find(".validation-summary ul")
            .append(
              "<li class='client-side' id='" + $formElement.attr("id") + "Summary'><a href='#" + $formElement.attr("id") + "'>" + summary + "</a></li>"
            );

      },
      jumpToError = function() {

        var $field = $($(this).attr("href"));

        // This is some weirdness to smooth out the behviour of focusing between various browsers
        $field.on("mouseup", function (event) {
          event.preventDefault();
        });

        setTimeout(function() {
          $field.trigger("focus").select().off("mouseup");
        }, 10);

        GDM.pub(
          "summaryErrorClicked",
          $field.parent(".validation-error-wrapper")
        );

      };

  GDM.validate = init;

  GOVUK.GDM = GDM;

}).apply(this, [GOVUK||{}, GOVUK.GDM||{}]);
