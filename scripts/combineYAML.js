module.exports = {
  'combine' : function (opts) {
    var YAML = require('js-yaml');
    var fs = require('fs');
    var path = require('path');
    var beautify = require('js-beautify').js_beautify;

    var inputFolder = opts.inputFolder;
    var outputJsonFile = opts.outputJsonFile;
    var formatingOptions = { 'indent' : 2 };
    var manifestFileName = 'index.yml';

    var getManifests = function (path) {
      var children = fs.readdirSync(path),
          results = [];

      children.forEach(function (child) {
        var isManifest = child.match(/([^\.]+.yml)$/);

        if (isManifest) {
          results.push(child);
        }
      });

      return results;
    };

    var getFirstProperty = function (obj) {
      var prop;
      for (prop in obj) {
        return prop;
      }
    };

    var getChildFiles = function (path) {
      var children = fs.readdirSync(path),
          results = [];

      children.forEach(function (child) {
        var yamlFile = child.match(/([^\.]+.yml)$/);

        if ((yamlFile !== null) && (yamlFile[1] !== manifestFileName)) {
          results.push(child);
        }
      });

      return results;
    };

    var getFilesFromManifest = function (manifestFileName) {
      var manifestFile = fs.readFileSync(manifestFileName, { encoding : 'utf-8' }),
          manifestArray = YAML.safeLoad(manifestFile),
          files = [],
          idx,
          len,
          pageNumber;

      for (idx = 0, len = manifestArray.length; idx < len; idx++) {
        pageNumber = getFirstProperty(manifestArray[idx]);
        files.push(manifestArray[idx][pageNumber].file);
      }

      return files;
    };

    var ssp = {};
    var sections = getManifests(inputFolder);
    var idx;
    var len;
    var sectionFileName;
    var sectionName;
    var sectionPages;

    var getPages = function (manifestFileName) {
      var pageFile,
          pageObj,
          idx,
          len,
          pages = [];

      pageFiles = getFilesFromManifest(inputFolder + '/' + manifestFileName);

      for (idx = 0, len = pageFiles.length; idx < len; idx++) {
        pageFile = fs.readFileSync(inputFolder + '/' + pageFiles[idx], { encoding : 'utf-8' });
        pageObj = YAML.safeLoad(pageFile);
        pages.push(pageObj);
      }

      return pages;
    };

    // get sections
    for (idx = 0, len = sections.length; idx < len; idx++) {
      sectionFileName = sections[idx];
      sectionName = sectionFileName.substring(0, sectionFileName.length - 4); // Get rid of .yml, .YML, etc
      ssp[sectionName] = getPages(sectionFileName);
    }

    var jsonString = JSON.stringify(ssp);
    fs.writeFileSync(outputJsonFile, beautify(jsonString, formatingOptions), { encoding : 'utf-8' });
  }
};
