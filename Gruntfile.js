module.exports = function(grunt){

  var JSModules = [];
  var combineJsonFile = __dirname + '/content.json';
  var propertiesFile = __dirname + '/conf/content.properties';

  grunt.registerTask(
    'findJS',
    'Make an array of all JS files that can be used by other tasks',
    function () {

      JSModules.push("public/javascripts/jquery.js");
      grunt.file.recurse(
        "public/javascripts",
        function(abspath, rootdir, subdir, filename) {

          if (
            filename.match(/\.js/) &&
            !filename.match(/application\.js/) &&
            !filename.match(/jquery\.js/) &&
            !filename.match(/main\.js/) &&
            !filename.match(/\.min\.js/) &&
            !filename.match(/\.map/)
          ) JSModules.push(abspath);

        }
      );
      JSModules.push("public/javascripts/main.js");

    }
  );

  grunt.initConfig({

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

    // Compress image assets and move them into public
    imagemin: {
      dynamic: {
        files: [{
          expand: true,
          cwd: "app/assets/images/",
          src: ["*.{png,jpg,gif}"],
          dest: "public/images/"
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
        cwd: 'node_modules/govuk_frontend_toolkit/govuk_frontend_toolkit/javascripts/govuk/',
        src: 'selection-buttons.js',
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
      dev: {
        options: {
          sourceMap: true,
          sourceMapName: "public/javascripts/sourcemap.map",
          mangle: false,
          compress: false,
          beautify: true,
          preserveComments: true
        },
        files: {
          "public/javascripts/application.js": JSModules
        }
      },
      production: {
        options: {
          sourceMap: false,
          mangle: true,
          compress: true,
          preserveComments: false
        },
        files: {
          "public/javascripts/application.js": JSModules
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
        tasks: ['imagemin', 'sass:dev', 'dataUri'],
        options: { nospawn: true }
      },
      js: {
        files: ['app/assets/javascripts/**/*'],
        tasks: ['copy:assets_js', 'uglify:dev']
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
          inputFolder = __dirname + '/conf/digital-marketplace-ssp-content';

      script.combine({
        'outputJsonFile' : outputJsonFile,
        'inputFolder' : inputFolder
      });
      grunt.log.writeln('Files in ' + inputFolder + ' combined to create ' + outputJsonFile);
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

  grunt.registerTask('content', [
    'combine',
    'properties',
    'clean:combine'
  ]);

  grunt.registerTask('dev', [
    'bower',
    'copy',
    'replace',
    'imagemin',
    'sass:dev',
    'dataUri',
    'findJS',
    'uglify:dev',
    'clean:tempCSS'
  ]);

  grunt.registerTask('production', [
    'bower',
    'copy',
    'replace',
    'imagemin',
    'sass:production',
    'dataUri',
    'findJS',
    'uglify:production',
    'clean'
  ]);

  grunt.registerTask('default', [
    'dev',
    'watch'
  ]);

};
