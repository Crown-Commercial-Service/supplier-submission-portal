module.exports = {
  'properties' : function (opts) {
    var YAML = require('js-yaml');
    var fs = require('fs');
    var path = require('path');
    var Mustache = require('mustache');

    var inputJsonFile = opts.inputJsonFile;
    var outputPropertiesFile = opts.outputPropertiesFile;
    var formatingOptions = { 'indent' : 2 };

    var contentFile = fs.readFileSync(inputJsonFile, { encoding : 'utf-8' })
    var ssp = JSON.parse(contentFile);

    var getFirstPropertyKey = function (obj) {
      var key;

      for (key in obj) {
        return key;
      }
    };

    // do something with the ssp object

    var production = {};
    var productionPage = {};
    var getPageNameFromWrapper = function (pageWrapper) {
      return getFirstPropertyKey(pageWrapper);
    };
    var getPageObjectFromWrapper = function (pageWrapper) {
      return getFirstPropertyValue(pageWrapper);
    };
    var getProductionPageBase = function (pageWrapper, pageIndex) {
      var result = {};

      result.pageNumber = pageIndex;
      result.title = getPageNameFromWrapper(pageWrapper);
      result.questions = [];

      return result;
    };
    var getPageObjectQuestions = function (sspPageObject, pageObject, idx) {
      return sspPageObject[pageObject.title];
    };
    var getProductionQuestion = function (questionName, questionIndex, questionObject) {
      var result = {},
          bannedProps = ['fields'],
          prop,
          getLots;

      getLots = function(lots) {

        var strippedLots;
        var productionLots;

        if ('undefined' === typeof lots) {
          productionLots = 'SaaS,PaaS,IaaS,SCS';
        } else {
          productionLots = lots.replace(/\s+/g, '');
        }

        // Make sure that nothing other than  the correctly capitalized lots are
        // getting into the properties
        strippedLots = productionLots.replace(/SCS|PaaS|SaaS|IaaS/g, '').replace(/,/g, '');

        if (strippedLots.length) {
          console.warn('FAILED: ILLEGAL LOT TYPE\n(lot is case-sensitive)');
          throw 'Illegal lot type (lot is case-sensitive)';
        }

        return productionLots;

      };

      result.questionNumber = questionIndex + 1;
      result.text = questionName;
      result.dependsOnLots = getLots(questionObject.dependsOnLots);

      for (prop in questionObject) {
        if (bannedProps.indexOf(prop) === -1) {
          result[prop] = questionObject[prop];
        }
      }

      result.fields = [];

      return result;
    };
    var getProductionField = function (fieldObject, fieldIndex, question) {
      var result = {},
          bannedProps = ['type'],
          prop,
          getParam,
          getLots;

      getParam = function (label) {
        label = label.toLowerCase();
        label = label.replace(/\s+/g, '-');
        label = label.replace(/[^\w-]+/g, '');

        return label;
      };

      if (result.type === 'assurance') {
        return false;
      }
      result.fieldNumber = fieldIndex + 1;
      for (prop in fieldObject) {
        if (bannedProps.indexOf(prop) === -1) {
          result[prop] = fieldObject[prop];
        }
      }
      if (typeof result.label !== 'undefined') {
        result.param = getParam(result.label);
      } else {
        result.param = getParam(question);
      }

      return result;
    };

    var pageObjects = [];
    var sspPageObject;
    var questionIdx;
    var fieldIdx;
    var questionName;
    for (idx = 0, len = ssp.service.length; idx < len; idx++) {
      sspPageObject = ssp.service[idx];
      pageObjects.push(getProductionPageBase(sspPageObject, idx));
      questionObjects = getPageObjectQuestions(sspPageObject, pageObjects[idx]);
      for (questionName in questionObjects) {
        questionIdx = pageObjects[idx].questions.length;
        pageObjects[idx].questions.push(getProductionQuestion(questionName, questionIdx, questionObjects[questionName]));
        fieldObjects = questionObjects[questionName].fields;
        for (fieldIdx = 0, fieldsLen = fieldObjects.length; fieldIdx < fieldsLen; fieldIdx++) {
          productionField = getProductionField(fieldObjects[fieldIdx], fieldIdx, questionName);
          if (productionField) {
            pageObjects[idx].questions[questionIdx].fields.push(productionField);
          }
        }
      }
    }
    var productionPageEntry = fs.readFileSync(__dirname + '/productionPageEntry.mustache', { encoding : 'utf-8' });
    var addRowsFromPageObject = function (pageObject, rowsArray) {
      var questionIndex,
          fieldIndex;

      productionPageItems += Mustache.render(productionPageEntry, pageObject);
    };
    var productionPageItems = '';
    for (idx = 0, len = pageObjects.length; idx < len; idx++) {
      addRowsFromPageObject(pageObjects[idx], productionPageItems);
    }
    fs.writeFileSync(outputPropertiesFile, productionPageItems, { encoding : 'utf-8' });
  }
};
