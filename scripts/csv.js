module.exports = {
  'csv' : function (opts) {
    var YAML = require('js-yaml');
    var fs = require('fs');
    var path = require('path');
    var Mustache = require('mustache');
    var assurancesFile = fs.readFileSync(__dirname + '/assurance.js', { encoding : 'utf-8' });

    var inputJsonFile = opts.inputJsonFile;
    var outputCsvFile = opts.outputCsvFile;
    var formatingOptions = { 'indent' : 2 };
    var assurances = JSON.parse(assurancesFile);

    var contentFile = fs.readFileSync(inputJsonFile, { encoding : 'utf-8' });
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

      result.text = questionName;
      result.dependsOnLots = getLots(questionObject.dependsOnLots);
      if (questionObject.hasOwnProperty('assuranceApproach')) {
        result.assuranceApproach = questionObject.assuranceApproach;
      }
      if (questionObject.hasOwnProperty('assuranceHint')) {
        result.assuranceHint = questionObject.assuranceHint;
      }

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
          bannedProps = [],
          prop,
          getLots;

      for (prop in fieldObject) {
        if (bannedProps.indexOf(prop) === -1) {
          result[prop] = fieldObject[prop];
        }
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
    var getCSVLineFromQuestion = function (pageTitle, questionObj, lot) {
      var requiredFields = ["text","hint"],
          resultingArray = [pageTitle],
          formatValue,
          idx,
          len;

      var getArrayOfValidFields = function (fieldsArr) {
        var prop,
            resultingFields = [];

        for (idx = 0, len = fieldsArr.length; idx < len; idx++) {
          fieldObj = fieldsArr[idx];
          if (typeof fieldObj.label !== 'undefined') {
            resultingFields.push(fieldObj.label);
          } else {
            resultingFields.push('');
          }
        }
        return resultingFields;
      };

      var getFields = function (questionFields) {
        if (questionFields[0].type === 'boolean') {
          return ['Yes', 'No'];
        } else {
          return getArrayOfValidFields(questionFields);
        }
      };

      for (idx = 0, len = requiredFields.length; idx < len; idx++) {
        requiredField = requiredFields[idx];
        if (questionObj.hasOwnProperty(requiredField)) {
          resultingArray.push(questionObj[requiredField]);
        } else {
          resultingArray.push('');
        }
      }
      var fields = getFields(questionObj.fields),
          field;

      for (field in fields) {
        resultingArray.push(fields[field]);
      }
      return resultingArray;
    };

    var addPaddingCells = function (csvArray, totalColumns) {
      var paddingCells,
          cellsToPad,
          isHeaderRow = false,
          idx,
          len;

      for (idx = 0, len = csvArray.length; idx < len; idx++) {
        paddingCells = totalColumns - csvArray[idx].length;
        cellsPadded = 0;
        isHeaderRow = idx === 0;
        while (paddingCells--) {
          if (isHeaderRow) {
            csvArray[idx].push('Answer ' + (cellsPadded + 1));
            cellsPadded++;
          } else {
            csvArray[idx].push('');
          }
        }
      }
      return csvArray;
    };

    var formatCellValues = function (csvArray) {
      var formatCellValue,
          idx,
          len,
          rowIdx,
          rowLen;

      formatCellValue = function (str) {
        return '"' + str + '"';
      };

      for (idx = 0, len = csvArray.length; idx < len; idx++) {
        for (rowIdx = 0, rowLen = csvArray[idx].length; rowIdx < rowLen; rowIdx++) {
          csvArray[idx][rowIdx] = formatCellValue(csvArray[idx][rowIdx]);
        }
      }
      return csvArray;
    };

    var makeCsvFromArray = function (csvArray) {
      var csv = '',
          idx,
          len;

      for (idx = 0, len = csvArray.length; idx < len; idx++) {
        csv += csvArray[idx].join(',');
        csv += "\r\n";
      }
      return csv;
    };

    var makeRowCellsMatchNumberOfCoumns = function (csvArray) {
      var totalColumns = 0;

      for (idx = 0, len = csvArray.length; idx < len; idx++) {
        totalColumns = (csvArray[idx].length > totalColumns) ? csvArray[idx].length : totalColumns;
      }

      csvArray = addPaddingCells(csvArray, totalColumns);
      csvArray = formatCellValues(csvArray);
      return csvArray;
    };

    var getAssuranceRow = function (assuranceApproach, assuranceHint) {
      var idx,
          len,
          assuranceArray = (assuranceHint) ? [assuranceHint] : [''];

      for (idx = 0, len = assurances.length; idx < len; idx++) {
        if (assurances[idx].types.indexOf(assuranceApproach) > -1) {
          return assuranceArray.concat(assurances[idx].answers);
        }
      }
      return false;
    };

    var makeInitialArray = function (csvArray, lot) {
      var pageTitle,
          questionsObj,
          questionIdx,
          assuranceRow,
          assuranceHint = false;

      for (idx = 0, len = pageObjects.length; idx < len; idx++) {
        pageTitle = pageObjects[idx].title;
        for (questionIdx = 0, questionsLen = pageObjects[idx].questions.length; questionIdx < questionsLen; questionIdx++) {
          questionsObj = pageObjects[idx].questions[questionIdx];
          
          if (questionsObj.dependsOnLots.indexOf(lot) > -1) {
            csvRow = getCSVLineFromQuestion(pageTitle, questionsObj);
            if (csvRow) {
              csvArray.push(csvRow);
            }
            if (questionsObj.hasOwnProperty('assuranceApproach')) {
              assuranceHint = (questionsObj.hasOwnProperty('assuranceHint')) ? questionsObj.assuranceHint : false;
              assuranceRow = getAssuranceRow(questionsObj.assuranceApproach, assuranceHint);
              if (assuranceRow) {
                csvArray.push([pageTitle,'Assurance approach'].concat(assuranceRow));
              } else {
                console.warn("No assurance question found for '" + questionsObj.text + "' on '" + pageTitle + "' page when lot '" + lot + "' is selected");
              }
            }
          }
        }
      }
      return csvArray;
    };

    var IaasCsvArray = makeInitialArray([
      ["Page title","Question","Hint"]
    ], 'IaaS');
    var SaasCsvArray = makeInitialArray([
      ["Page title","Question","Hint"]
    ], 'SaaS');
    var PaasCsvArray = makeInitialArray([
      ["Page title","Question","Hint"]
    ], 'PaaS');
    var ScsCsvArray = makeInitialArray([
      ["Page title","Question","Hint"]
    ], 'ScS');

    IaasCsvArray = makeRowCellsMatchNumberOfCoumns(IaasCsvArray);
    SaasCsvArray = makeRowCellsMatchNumberOfCoumns(SaasCsvArray);
    PaasCsvArray = makeRowCellsMatchNumberOfCoumns(PaasCsvArray);
    ScsCsvArray = makeRowCellsMatchNumberOfCoumns(ScsCsvArray);
    var IaasCsvPage = makeCsvFromArray(IaasCsvArray);
    var SaasCsvPage = makeCsvFromArray(SaasCsvArray);
    var PaasCsvPage = makeCsvFromArray(PaasCsvArray);
    var ScsCsvPage = makeCsvFromArray(ScsCsvArray);
    
    fs.writeFileSync(outputCsvFile.replace(/\.csv/, '_IaaS.csv'), IaasCsvPage, { encoding : 'utf-8' });
    fs.writeFileSync(outputCsvFile.replace(/\.csv/, '_SaaS.csv'), SaasCsvPage, { encoding : 'utf-8' });
    fs.writeFileSync(outputCsvFile.replace(/\.csv/, '_PaaS.csv'), PaasCsvPage, { encoding : 'utf-8' });
    fs.writeFileSync(outputCsvFile.replace(/\.csv/, '_SCS.csv'), ScsCsvPage, { encoding : 'utf-8' });
  }
};
