/*global module:false*/
'use strict';

module.exports = function (grunt) {
  // load all grunt tasks
  require('matchdep').filterDev('grunt-*').concat(['gruntacular']).forEach(grunt.loadNpmTasks);

  // configurable paths
  var appConfig = {
    components: 'components',
    javascripts: 'app',
    stylesheets: 'stylesheets',
    images: 'assets/images',
    fonts: 'assets/fonts',
    dist: {
      javascripts: '../public/javascripts',
      stylesheets: '../public/stylesheets',
      images: '../public/images',
      fonts: '../public/fonts'
    }
  };

  grunt.initConfig({
    conf: appConfig,
    watch: {
      javascripts: {
        files: [
          '<%= conf.javascripts %>/*.js',
          '<%= conf.javascripts %>/**/*.js'
        ],
        tasks: ['uglify']
      },
      less: {
        files: [
          '<%= conf.stylesheets %>/*.less',
          '<%= conf.stylesheets %>/**/*.less'
        ],
        tasks: ['less']
      },
      livereload: {
        options: {
          nospawn: true
        },
        files: [
          '<%= conf.dist.javascripts %>/*.js',
          '<%= conf.dist.stylesheets %>/*.css',
          '<%= conf.dist.images %>/*.{png,jpg,jpeg}'
        ],
        tasks: ['livereload']
      }
    },
    clean: {
      dist: ['<%= conf.dist %>/*'],
      server: '.tmp'
    },
    jshint: {
      options: {
        jshintrc: '.jshintrc'
      },
      all: [
        'Gruntfile.js',
        '<%= conf.javascripts %>/**/*.js',
        'test/spec/*.js'
      ]
    },
    testacular: {
      unit: {
        configFile: 'testacular.conf.js',
        singleRun: true
      }
    },
    less: {
      options: {
        paths: ['<%= conf.stylesheets %>']
      },
      dev: {
        files: {
          '<%= conf.dist.stylesheets %>/main.min.css': '<%= conf.stylesheets %>/main.less'
        }
      },
      dist: {
        options: {
          yuicompress: true
        },
        files: {
          '<%= conf.dist.stylesheets %>/main.min.css': '<%= conf.stylesheets %>/main.less'
        }
      }
    },
    uglify: {
      dist: {
        files: {
          '<%= conf.dist.javascripts %>/main.min.js': [
            '<%= conf.javascripts %>/**/*.js',
          ]
        }
      }
    },
    concat: {
      dist: {
        files: {
          '<%= conf.dist.javascripts %>/angular.min.js': [
            '<%= conf.components %>/angular/angular.min.js',
            '<%= conf.components %>/angular-resource/angular-resource.min.js'/*,
            '<%= conf.components %>/angular-bootstrap/dist/ui-bootstrap-tpls-0.1.0.min.js'*/
          ],
          '<%= conf.dist.javascripts %>/ie-shim.min.js': [
            '<%= conf.components %>/es5-shim/es5-shim.min.js',
            '<%= conf.components %>/json3/lib/json3.min.js',
          ],
        }
      }
    },
    copy: {
      dist: {
        files: [{
          expand: true,
          flatten: true,
          dot: true,
          cwd: '<%= conf.components %>',
          dest: '<%= conf.dist.javascripts %>',
          src: [
            'jquery/jquery.min.js'
          ]
        }]
      }
    },
    bower: {
      rjsConfig: '<%= conf.javascripts %>/main.js',
      indent: '    '
    }
  });

  grunt.registerTask('run', [
    'clean:dist',
    'uglify',
    'concat',
    'copy',
    'less:dev',
    'livereload-start',
    'watch'
  ]);

  grunt.registerTask('test', [
    'clean:server',
    'testacular'
  ]);

  grunt.registerTask('dist', [
    'clean:dist',
    'jshint',
    'uglify',
    'concat',
    'copy',
    'less:dist'
  ]);

  grunt.registerTask('default', ['dist']);
};
