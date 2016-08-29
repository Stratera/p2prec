'use strict';

var path = require('path');
var gulp = require('gulp');
var conf = require('./conf');

var browserSync = require('browser-sync');

var paths = conf.paths;
var props = conf.configProperties;

var $ = require('gulp-load-plugins')();

var wiredep = require('wiredep').stream;
var _ = require('lodash');

gulp.task('styles-reload', ['styles'], function() {
  return buildStyles()
    .pipe(browserSync.stream());
});

gulp.task('styles', ['compileVendorCSS', 'compileVendorFonts'], function() {
  return buildStyles();
});

gulp.task('compileVendorCSS', function () {
    return gulp.src([
      path.join(paths.src, 'app/vendor/**/*.css'),
      path.join('!' + paths.src, '/app/vendor/**/*.min.css')
    ])
      .pipe(gulp.dest(path.join(paths.tmp, '/serve/app/')));
});

gulp.task('compileVendorFonts', function () {
    return gulp.src([
      path.join(paths.src, 'app/vendor/**/*.eot'),
      path.join(paths.src, 'app/vendor/**/*.ttf'),
      path.join(paths.src, 'app/vendor/**/*.woff'),
      path.join(paths.src, 'app/vendor/**/*.woff2')
    ])
      .pipe(gulp.dest(path.join(paths.tmp, '/serve/app/')));
});

var buildStyles = function() {
  var sassOptions = {
    outputStyle: 'expanded',
    precision: 10
  };

  var injectFiles = gulp.src([
    path.join(paths.src, '/app/**/*.scss'),
    path.join('!' + paths.src, '/app/index.scss')
  ], { read: false });

  var injectOptions = {
    transform: function(filePath) {
      filePath = filePath.replace(conf.paths.src + '/app/', '');
      return '@import "' + filePath + '";';
    },
    starttag: '// injector',
    endtag: '// endinjector',
    addRootSlash: false
  };

  return gulp.src([
    path.join(paths.src, '/app/index.scss')
  ])
    .pipe($.inject(injectFiles, injectOptions))
    .pipe(wiredep(_.extend({}, conf.wiredep)))
    .pipe($.sourcemaps.init())
    .pipe($.sass(sassOptions)).on('error', conf.errorHandler('Sass'))
    .pipe($.autoprefixer()).on('error', conf.errorHandler('Autoprefixer'))
    .pipe($.sourcemaps.write())
    .pipe(gulp.dest(path.join(conf.paths.tmp, '/serve/app/')));
};


