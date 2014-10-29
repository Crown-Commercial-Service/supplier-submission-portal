module.exports = function(grunt){

  var JSModules = [];
  var combineJsonFile = __dirname + '/content.json';
  var propertiesFile = __dirname + '/conf/content.properties';
  var csvFile = __dirname + '/conf/content.csv';
  var JSFiles = [
    "public/javascripts/jquery.js",
    "public/javascripts/hogan.js",
    "public/javascripts/govuk-template.js",
    "public/javascripts/selection-buttons.js",
    "public/javascripts/wordCounter.js",
    "public/javascripts/listEntry.js",
    "public/javascripts/main.js"
  ];

  grunt.initConfig({

    // Lints SASS
    scsslint: {
      allFiles: [
        'app/assets/sass/**/*.scss',
      ],
      options: {
        config: 'app/assets/sass/.scss-lint.yml',
        colorizeOutput: true
      },
    },

    // Builds SASS
    sass: {
      dev: {
        options: {
          style: "expanded",
          sourcemap: true,
          includePaths: [
            'govuk_modules/govuk_template/assets/stylesheets',
            'govuk_modules/govuk_frontend_toolkit/stylesheets'
          ]
        },
        files: [{
          expand: true,
          cwd: "app/assets/sass",
          src: ["*.scss"],
          dest: "public/stylesheets/.temp", // Put stylesheet in temp place for other tasks to work on
          ext: ".css"
        }]
      },
      production: {
        options: {
          style: "compressed",
          sourceMap: false,
          includePaths: [
            'govuk_modules/govuk_template/assets/stylesheets',
            'govuk_modules/govuk_frontend_toolkit/stylesheets'
          ]
        },
        files: [{
          expand: true,
          cwd: "app/assets/sass",
          src: ["*.scss"],
          dest: "public/stylesheets",
          ext: ".css"
        }]
      }
    },

    // Make data URIs from images
    dataUri: {
      dist: {
        src: [
          "public/stylesheets/.temp/application.css"
        ],
        dest: "public/stylesheets/",
        options: {
          target: ["public/images/**/*"],
          fixDirLevel: true,
          baseDir: "public/images/"
        }
      }
    },

    // Copies templates and assets from external modules and dirs
    copy: {

      govuk_frontend_toolkit: {
        cwd: 'node_modules/govuk_frontend_toolkit/govuk_frontend_toolkit',
        src: '**',
        dest: 'govuk_modules/govuk_frontend_toolkit/',
        expand: true
      },

      govuk_template: {
        cwd: 'node_modules/govuk_template_mustache/assets/',
        src: '**',
        dest: 'govuk_modules/govuk_template/',
        expand: true
      },

      template_css: {
        cwd: 'node_modules/govuk_template_mustache/assets/stylesheets/',
        src: '**',
        dest: 'public/stylesheets/',
        expand: true
      },

      assets_js: {
        cwd: 'app/assets/javascripts/',
        src: '**/*',
        dest: 'public/javascripts/',
        expand: true,
        flatten: true
      },

      toolkit_js: {
        cwd: 'node_modules/govuk_frontend_toolkit/govuk_frontend_toolkit/javascripts/',
        src: ['govuk/selection-buttons.js', 'vendor/polyfills/bind.js'],
        dest: 'public/javascripts/',
        expand: true,
        flatten: true
      },

      template_js: {
        cwd: 'node_modules/govuk_template_mustache/assets/javascripts/',
        src: '**',
        dest: 'public/javascripts/',
        expand: true,
        flatten: true
      },

      toolkit_images: {
        cwd: 'node_modules/govuk_frontend_toolkit/images/',
        src: '**',
        dest: 'public/images/',
        expand: true
      },

      template_images: {
        cwd: 'node_modules/govuk_template_mustache/assets/images/',
        src: '**',
        dest: 'public/images/',
        expand: true
      },

      asset_images: {
        cwd: 'app/assets/images/',
        src: '**',
        dest: 'public/images/',
        expand: true
      }

    },

    uglify: {
      production: {
        options: {
          sourceMap: false,
          mangle: true,
          compress: true,
          preserveComments: false
        },
        files: {
          "public/javascripts/application.js": JSFiles
        }
      }
    },

    // workaround for libsass
    replace: {
      fixSass: {
        src: ['govuk_modules/govuk_template/**/*.scss', 'govuk_modules/govuk_frontend_toolkit/**/*.scss'],
        overwrite: true,
        replacements: [{
          from: /filter:chroma(.*);/g,
          to: 'filter:unquote("chroma$1");'
        }]
      }
    },

    // Remove temporary CSS files
    clean: {
      tempCSS: ['public/stylesheets/.temp'],
      combine: [combineJsonFile],
      production: ['node_modules/*', 'govuk_modules/*', 'bower_components/*']
    },

    // Update whenever CSS/JS/Gruntfile is changed
    watch: {
      css: {
        files: ['app/assets/sass/**/*.scss'],
        tasks: ['sass:dev', 'dataUri'],
        options: { nospawn: true }
      },
      images: {
        files: ['app/assets/images/**/*'],
        tasks: ['copy', 'sass:dev', 'dataUri'],
        options: { nospawn: true }
      },
      js: {
        files: ['app/assets/javascripts/**/*'],
        tasks: ['copy:assets_js']
      },
      self: {
        files: ['Gruntfile.js'],
        tasks: ['dev']
      }
    },

    // Using Bower to install front-end dependencies
    bower: {
      install: {
        dest: 'public/javascripts',
        options: {
          cleanBowerDir: true,
          stripAffix: true
        }
      }
    },

    jasmine: {
      javascripts: {
        src: [
          'node_modules/jquery-browser/lib/jquery.js',
          'public/javascripts/hogan.js',
          'public/javascripts/wordCounter.js',
          'public/javascripts/listEntry.js'
        ],
        options: {
          specs: 'scripts/test/unit/*Spec.js',
          helpers: 'scripts/test/unit/*Helper.js'
        }
      }
    }

  });

  // Automatically loads any grunt-* tasks in your package.json
  require("load-grunt-tasks")(grunt);

  grunt.registerTask(
    'combine',
    'Converts the YAML content in conf/digital-marketplace-ssp-content into content.json',
    function () {
      var script = require(__dirname + '/scripts/combineYAML.js'),
          outputJsonFile = combineJsonFile,
          inputFolder = __dirname + '/conf/digital-marketplace-ssp-content',
          completed = false;

      completed = script.combine({
        'outputJsonFile' : outputJsonFile,
        'inputFolder' : inputFolder
      });
      if (completed) {
        grunt.log.writeln('Files in ' + inputFolder + ' combined to create ' + outputJsonFile);
      } else {
        grunt.fail.fatal("Could not produce content from YAML files");
      }
    }
  );

  grunt.registerTask(
    'properties',
    'Converts the content.json file into conf/content.properties',
    function () {
      var script = require(__dirname + '/scripts/properties.js'),
          inputJsonFile = combineJsonFile,
          outputPropertiesFile = propertiesFile;

      script.properties({
        'inputJsonFile' : inputJsonFile,
        'outputPropertiesFile' : outputPropertiesFile
      });
      grunt.log.writeln(inputJsonFile + ' converted into ' + outputPropertiesFile);
    }
  );

  grunt.registerTask(
    'csv',
    'Converts the content.json file into conf/content.csv',
    function () {
      var script = require(__dirname + '/scripts/csv.js'),
          inputJsonFile = combineJsonFile,
          outputCsvFile = csvFile,
          IaasCsvFile = outputCsvFile.replace(/\.csv$/, '_IaaS.csv'),
          SaasCsvFile = outputCsvFile.replace(/\.csv$/, '_SaaS.csv'),
          PaasCsvFile = outputCsvFile.replace(/\.csv$/, '_PaaS.csv'),
          ScsCsvFile = outputCsvFile.replace(/\.csv$/, '_ScS.csv');

      script.csv({
        'inputJsonFile' : inputJsonFile,
        'outputCsvFile' : outputCsvFile
      });
      grunt.log.writeln(inputJsonFile + ' converted into:');
      grunt.log.writeln('');
      grunt.log.writeln(IaasCsvFile);
      grunt.log.writeln(SaasCsvFile);
      grunt.log.writeln(PaasCsvFile);
      grunt.log.writeln(ScsCsvFile);
    }
  );

  grunt.registerTask('content', [
    'combine',
    'properties',
    'clean:combine'
  ]);

  grunt.registerTask('spreadsheet', [
    'combine',
    'csv',
    'clean:combine'
  ]);

  grunt.registerTask('dev', [
    'bower',
    'copy',
    'replace',
    'sass:dev',
    'dataUri',
    'clean:tempCSS'
  ]);

  grunt.registerTask('production', [
    'bower',
    'copy',
    'replace',
    'sass:production',
    'dataUri',
    'uglify:production',
    'clean'
  ]);

  grunt.registerTask('default', [
    'dev',
    'watch'
  ]);

  grunt.registerTask('test', [
    'jasmine'
  ]);

};
