(function() {
  'use strict';

  angular
    .module('dhsChallenge')
    .config(routerConfig);

  /** @ngInject */
  function routerConfig($stateProvider, $urlRouterProvider) {
    $stateProvider
      .state('home', {
        url: '/',
        templateUrl: 'app/main/main.html',
        controller: 'MainController',
        controllerAs: 'main'
      })
      .state('view', {
        url: '/view',
        templateUrl: 'app/view/view.html',
        controller: 'ViewController',
        controllerAs: 'viewCtrl'
      });
    $urlRouterProvider.otherwise('/');
  }
})();
