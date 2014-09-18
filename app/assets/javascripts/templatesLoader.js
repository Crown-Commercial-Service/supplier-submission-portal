(function(GOVUK, GDM) {

  "use strict";

  var templatesPath = '/public/javascripts/templates/',
      templates = {
        'address.html' : false,
        'assurance.html' : false,
        'boolean.html' : false,
        'checkbox.html' : false,
        'dropdown.html' : false,
        'list.html' : false,
        'percentage.html' : false,
        'price.html' : false,
        'radio.html' : false,
        'radios.html' : false,
        'text.html' : false,
        'textarea.html' : false,
        'upload.html' : false
      };

  GOVUK.GDM = GDM;
  GOVUK.GDM.templates = {};
  GOVUK.GDM.templatesLoader = {
    load : function () {
      var templateFile,
          template;

      for (templateFile in templates) {
        template = templatesPath + templateFile;
        $.ajax(
          template,
          {
            'cache': false
          }
        ).done(function (templateName) {
          return function (data) {
            GOVUK.GDM.templatesLoader.onLoad(templateName, data);
          };
        }(template));
      }
    },
    onLoad : function (loadedTemplate, templateData) {
      var template,
          loadedTemplateFile = loadedTemplate.replace(templatesPath, '');

      templates[loadedTemplateFile] = true;
      GOVUK.GDM.templates[loadedTemplateFile] = templateData;
      for (template in templates) {
        if (!templates[template]) {
          return; // quit early if template has not loaded yet
        }
      }
      GDM.pub('templatesLoaded');
    },
    init : function () {
      this.load();
    }
  };

}).apply(this, [GOVUK||{}, GOVUK.GDM||{}]);
