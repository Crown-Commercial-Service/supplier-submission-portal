(function (GOVUK, GDM) {

  "use strict";

  var root = this,
      $ = root.jQuery,
      completed = false,
      pageURL = window.location.pathname.split('/').slice(1)[0],
      pageURLs = [],
      currentPageIdx,
      init = function() {

        GDM.sub("assetsLoaded", buildForm);

      },
      getTemplate = function (templateType) {
        return GDM.templates[templateType + '.html'];
      },
      fieldFactory = function (field) {

        switch(field.type) {

          case "boolean":
            field.name = field.name || "bool" + Math.random();
            return Mustache.render(
              getTemplate('boolean'),
              field
            );

          case "text":
            return Mustache.render(
              getTemplate('text'),
              field
            );

          case "list":

            field.fields = (function(i) {

              var ordinals = [];

              while (i--) {
                ordinals.push(10 - i);
              }

              return ordinals;

            }(10));

            return Mustache.render(
              getTemplate('list'),
              field
            );

          case "textarea":
            return Mustache.render(
              getTemplate('textarea'),
              field
            );

          case "radio":
            return Mustache.render(
              getTemplate('radio'),
              field
            );

          case "radios":
            return Mustache.render(
              getTemplate('radios'),
              field
            );

          case "checkbox":
            return Mustache.render(
              getTemplate('checkbox'),
              field
            );

          case "upload":
            return Mustache.render(
              getTemplate('upload'),
              field
            );

          case "percentage":
            return Mustache.render(
              getTemplate('percentage'),
              field
            );

          case "price":
            return Mustache.render(
              getTemplate('price'),
              field
            );

          case "address":
            return Mustache.render(
              getTemplate('address'),
              field
            );

          case "dropdown":
            return Mustache.render(
              getTemplate('dropdown'),
              field
            );

          case "assurance":
            return Mustache.render(
              getTemplate('assurance'),
              field
            );

          case "matrix":

            var renderedColumns = {
                  "heads": field.columns.map(function(column) {

                    return "<th>" +
                             "<label>" + column.label + "</label>" +
                             ("undefined" === typeof column.hint ? "" : "<p class='hint'>" + column.hint + "</p>") +
                           "</th>";

                  }),
                  "bodies": field.columns.map(function(column) {

                    return "<td>" +
                             fieldFactory(column.field) +
                           "</td>";

                  })
                };

            return "<table>" +
                     "<thead>" +
                       "<tr>" +
                         renderedColumns.heads.join("") +
                         "<th></th>" +
                       "</tr>" +
                     "</thead>" +
                     "<tbody>" +
                       (function(i) {

                         var rows = [];

                         while(i--) {

                           rows.push(
                            "<tr class='repeatable'>" +
                              renderedColumns.bodies.join("") +
                              "<td>" +
                                "<p>" +
                                  "<a href='#' class='listRemove'>Remove</a>" +
                                "</p>" +
                              "</td>" +
                            "</tr>"
                           );

                         }

                         return rows.join("\n");

                       }(10)) +
                       "<tr colspan='" + (field.columns.length + 1) + "' class='listAdd'>" +
                         "<td>" +
                           "<p>" +
                             "<a href='#'>" + field.addThing + "</a>" +
                           "</p>" +
                         "</td>" +
                       "</tr>" +
                     "</tbody>" +
                   "</table>";

          case "plaintext":
            return Mustache.render(
              "<p>" +
                "{{text}}" +
              "</p>",
              field
            );

        }

      },
      buildForm = function() {

        var pages = [], fieldsets = [], summaries = [], summaryOfQuestions = [],
            mockAnswer = "",
            numberOfPages,
            numberOfQuestions,
            pageIdx,
            pageName,
            page,
            fieldset,
            fieldsetIdx,
            field,
            question,
            $container = $("#questions-container"),
            sectionPages = GDM.questions[$container.data("question-source")],
            getFirstPropertyKey = function (obj) {
              var prop;

              for (prop in obj) {
                return prop;
              }
            },
            getFirstPropertyValue = function (obj) {
              var prop;

              for (prop in obj) {
                return obj[prop];
              }
            };

        for (pageIdx = 0, numberOfPages = sectionPages.length; pageIdx < numberOfPages; pageIdx++) {
          pageName = getFirstPropertyKey(sectionPages[pageIdx]);
          page = getFirstPropertyValue(sectionPages[pageIdx]);

          for (question in page) {

            fieldset = page[question];

            fieldsetIdx = fieldsets.push(
              $("<fieldset><legend>" + question + "</legend></fieldset>")
            );

            mockAnswer = fieldset.mockAnswer || (Math.random() > 0.5 ? "Yes" : "No");
            summaryOfQuestions.push(question + "<span class='toTheRight'>" + mockAnswer + "</span>");

            if ("undefined" !== typeof fieldset.hint) {
              fieldsets[fieldsetIdx - 1]
                .append("<p class='hint'>" + fieldset.hint + "</p>");
            }

            for (field in fieldset.fields) {
              fieldsets[fieldsetIdx - 1].append(
                fieldFactory(fieldset.fields[field])
              );
            }

          }

          numberOfQuestions = Object.keys(page).length;

          summaries.push(
            "<h2>" + pageName + "<a href='#' class='toTheRight'>Change</a></h2>" +
            "<ul>" +
              "<li>" + summaryOfQuestions.join("</li><li>") + "</li>" +
            "</ul>"
          );

          pages.push(
            $(
              "<section>" +
                "<p class='page-count page-" + (pageIdx + 1) + "'>" +
                  "Page " + (pageIdx + 1) + " of " + sectionPages.length +
                "</p>" +
                "<h2>" +
                  pageName +
                "</h2>" +
              "</section>"
            ).append(fieldsets)
          );

          summaryOfQuestions = [];

          fieldsets = [];

        }

        pages.push(
          $("<section class='serviceSummary'><h1>Summary</h1>" + summaries.join("\n") + "</section>")
        );

        sectionPages.push({
          "Summary": {}
        });

        $container.prepend(pages);

        $container.find("section").append("<input type='submit' value='Save and continue' />");

        $container.find("section").not(":first").append("<p class='goBackAPage'><a href='#'>Go back to previous page</a></p>");

        if (false !== $container.data("allow-dashboard")) {

          $container.find("section").append("<p class='returnToDashboard'><a href='/ssp_dashboard_progress_2'>Return to dashboard</a></p>");

        }

        $container
        .find("section:last-child [type=submit]")
          .attr("value", $container.data("exit-message"))
        .next("span")
          .remove();

        if ($container.data("confirm")) {

          $container
          .find("section:last-child [type=submit]")
            .before("<label class='chunk confirm'><input type='checkbox' " + $container.data("confirm-checked") + " /> " + $container.data("confirm") + "</label>");

        }

        if ($container.data("start-page")) {

          jumpToPage($container.data("start-page"));

          if (
            $container.data("start-page") === $container.find("section").length - 1
          ) completed = true;

        }

        $container
          .on("click", "[type=submit]", nextPage)
          .on("click", ".summary a", backToPage)
          .on("click", ".serviceSummary a", backToServiceSummaryPage)
          .on("click", ".listAdd", addItemToList)
          .on("click", ".listRemove", removeItemFromList)
          .on("click", ".goBackAPage", previousPage);

        $(document).keyup(function(e) {

          if (e.keyCode == 27) { // esc

            jumpToPage("last");

          }

        });

        window.onpopstate = function (evt) {
          pageURL = window.location.pathname.split('/').slice(1)[0];
          if (pageURL === '') {
            displayPage(0);
          } else {
            currentPageIdx = getPageIndex(pageURL);
            if (currentPageIdx) {
              displayPage(currentPageIdx);
            }
          }
        };

        getPageURLs(sectionPages);

        if (pageURL !== '') {
          currentPageIdx = getPageIndex(pageURL);
          if (currentPageIdx) {
            displayPage(currentPageIdx);
          }
        }

        GDM.pub("formRendered");

      },
      jumpToPage = function(index) {
        var $container = $("#questions-container"),
            sectionPages = GDM.questions[$container.data("question-source")],
            getFirstPropertyKey = function (obj) {
              var prop;

              for (prop in obj) {
                return prop;
              }
            },
            pageName;

        if (typeof index !== "number") {
          index = {
            "first": 0,
            "last": $("#questions-container section").length - 1
          }[index];
        }

        pageName = getFirstPropertyKey(sectionPages[index]);
        pageURL = pageNameToURL(pageName);
        history.pushState({
          'name' : pageName,
          'url' : pageURL
        }, pageName, pageURL);
        displayPage(index);

        GDM.pub('navigateToPage');

      },
      displayPage = function (index) {
        $("#questions-container")
          .find("section")
          .hide()
          .eq(index)
          .show();
      },
      backToPage = function() {

        jumpToPage(
          $(".summary a").index(this)
        );

      },
      backToServiceSummaryPage = function() {

        jumpToPage(
          $(".serviceSummary a").index(this)
        );

      },
      nextPage = function() {

        var $parent = $(this).parents("section"),
            $pages = $("#questions-container section");

        if ($parent.is(":last-child")) return (
          window.location.href = $parent.parents("form").data("exit")
        );

        if (completed) return jumpToPage(
          $pages.length - 1
        );

        completed = completed || ($parent.next("section").is(":last-child"));

        jumpToPage(
          $pages.index($parent) + 1
        );

      },
      previousPage = function() {

        var $parent = $(this).parents("section"),
            $pages = $("#questions-container section");

        jumpToPage(
          $pages.index($parent) - 1
        );

      },
      addItemToList = function(event) {

        event.preventDefault();

        $(this)
        .parents("fieldset")
        .find(".repeatable")
        .filter(":hidden")
        .eq(0)
          .show();

        doRepeatableControls.apply(this, []);

      },
      removeItemFromList = function(event) {

        event.preventDefault();

        $(this)
        .parents(".repeatable")
          .hide()
        .find("input, select")
          .val("");

        doRepeatableControls.apply(this, []);

      },
      doRepeatableControls = function() {

        var $repeatables = $(this).parents("fieldset").find(".repeatable");

        $repeatables.parents("fieldset").find(".listAdd").toggle(
          $repeatables.filter(":visible").length !== $repeatables.length
        );

        $repeatables
        .find(".listRemove")
          .hide()
        .end()
        .filter(":visible")
        .not(":first-of-type")
        .eq(-1)
        .find(".listRemove")
          .show();

      },
      getPageURLs = function (sectionPages) {
        var pageName,
            getFirstPropertyKey,
            idx, len;

        getFirstPropertyKey = function (obj) {
          var prop;

          for (prop in obj) {
            return prop;
          }
        };

        for (idx = 0, len = sectionPages.length; idx < len; idx++) {
          pageName = getFirstPropertyKey(sectionPages[idx]);
          pageURLs.push(pageNameToURL(pageName));
        }
      },
      pageNameToURL = function (pageName) {
        return encodeURIComponent(pageName.toLowerCase().replace(/\s+/g, '-'));
      },
      getPageIndex = function (pageURL) {
        var index = $.inArray(pageURL, pageURLs);

        if (index !== null) {
          return index;
        }
        return false;
      };

  GDM.formBuilder = init;

  GOVUK.GDM = GDM;

}).apply(this, [GOVUK||{}, GOVUK.GDM||{}]);
