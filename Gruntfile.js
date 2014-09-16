module.exports = function(grunt){

  var JSModules = [];

  grunt.file.recurse(
    "./public/javascripts",
    function(abspath, rootdir, subdir, filename) {

      if (
        filename.match(/\.js/) &&
        !filename.match(/application\.js/) &&
        !filename.match(/\.min\.js/) &&
        !filename.match(/\.map/)
      ) JSModules.push(abspath);

    }
  );

  grunt.initConfig({

    // Builds Sass
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
          dest: "public/stylesheets",
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

    // Compress image assets and move them into public
    dataUri: {
      dist: {
        src: [
          "public/stylesheets/application.css"
        ],
        dest: "public/stylesheets/application.min.css", // To fix, work out how to get around [src cannot equal dest]
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
        cwd: 'node_modules/govuk_frontend_toolkit/govuk_frontend_toolkit/',
        src: '**',
        dest: 'govuk_modules/govuk_frontend_toolkit/',
        expand: true
      },

      assets_js: {
        cwd: 'app/assets/javascripts/',
        src: '**',
        dest: 'public/javascripts/',
        expand: true
      },

      toolkit_js: {
        cwd: 'node_modules/govuk_frontend_toolkit/javascripts/govuk/',
        src: 'selection-buttons.js',
        dest: 'public/javascripts/',
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
          "public/assets/javascripts/application.js": JSModules
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

    // Watches styles and specs for changes
    watch: {
      css: {
        files: ['app/assets/sass/**/*.scss'],
        tasks: ['sass'],
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
    }

  });

// Automatically loads any grunt-* tasks in your package.json
  require("load-grunt-tasks")(grunt);

  grunt.registerTask('dev', [
    'copy',
    'bower',
    'replace',
    'sass:dev',
    'uglify:dev'
  ]);

  grunt.registerTask('default', [
    'dev',
    'watch'
  ]);

};
