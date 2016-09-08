'use strict';

var path = require('path');
var gulp = require('gulp');
var replace = require('gulp-replace');
var conf = require('./conf');
// var argv = require('yargs').argv;
var $ = require('gulp-load-plugins')();
// var deployedEnv = argv.environment || "local";



var browserSync = require('browser-sync');
var browserSyncSpa = require('browser-sync-spa');

var paths = conf.paths;
var props = conf.configProperties;

var util = require('util');

var proxyMiddleware = require('http-proxy-middleware');

function browserSyncInit(baseDir, browser) {
  browser = browser === undefined ? 'default' : browser;

  var routes = null;
  if(baseDir === conf.paths.src || (util.isArray(baseDir) && baseDir.indexOf(conf.paths.src) !== -1)) {
    routes = {
      '/bower_components': 'bower_components'
    };
  }

  var server = {
    baseDir: baseDir,
    routes: routes
  };

  /*
   * You can add a proxy to your backend by uncommenting the line below.
   * You just have to configure a context which will we redirected and the target url.
   * Example: $http.get('/users') requests will be automatically proxified.
   *
   * For more details and option, https://github.com/chimurai/http-proxy-middleware/blob/v0.9.0/README.md
   */
  // server.middleware = proxyMiddleware('/users', {target: 'http://jsonplaceholder.typicode.com', changeOrigin: true});

  browserSync.instance = browserSync.init({
    startPath: '/#/',
    server: server,
    browser: browser
  });
}

browserSync.use(browserSyncSpa({
  selector: '[ng-app]'// Only needed for angular apps
}));

gulp.task('serve', ['watch'], function () {
  browserSyncInit([path.join(conf.paths.tmp, '/serve'), conf.paths.src]);
});

gulp.task('serve:dist', ['build'], function () {
  browserSyncInit(conf.paths.dist);
});

gulp.task('serve:e2e', ['inject'], function () {
  browserSyncInit([conf.paths.tmp + '/serve', conf.paths.src], []);
});

gulp.task('serve:e2e-dist', ['build'], function () {
  browserSyncInit(conf.paths.dist, []);
});

// var tasks = {
//   localConstants: function() {
//     return gulp.src(props.applyTo)
//       .pipe(replace('@@svrPrefix@@', props.environments[deployedEnv].svrPrefix))
//       .pipe(replace('@@useAuth@@', props.environments[deployedEnv].useAuth))
//       .pipe(replace('@@version@@', props.version))
//       .pipe(replace('@@build@@', props.build))
//       .pipe(replace('@@debug@@', props.environments[deployedEnv].debug))
//       .pipe(gulp.dest(conf.paths.dist));
//   },

//   compileConstants: function() {
//     var last;
//     for (var env in props.environments) {
//       last = gulp.src(props.applyTo)
//         .pipe(replace('@@svrPrefix@@', props.environments[env].svrPrefix))
//         .pipe(replace('@@useAuth@@', props.environments[env].useAuth))
//         .pipe(replace('@@debug@@', props.environments[env].debug))
//         .pipe(gulp.dest(conf.paths.config + "/" + env));
//     }
//     return last;
//   },

// };

// gulp.task('local-constants', tasks.localConstants);
// gulp.task('package-constants', tasks.compileConstants);
