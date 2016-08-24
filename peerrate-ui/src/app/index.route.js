(function() {
  'use strict';

  angular
    .module('dhsChallenge',['view1'])
    .config(routerConfig);

  /** @ngInject */
  function routerConfig($stateProvider, $urlRouterProvider) {
    $urlRouterProvider.otherwise('/');
    $stateProvider
      .state('home', {
        url: '/',
        templateUrl: 'app/main/main.html',
        controller: 'MainController',
        controllerAs: 'main'
      })
      .state('view1', {
        url: '/view1',
        templateUrl: 'app/view1/view1.html',
        controller: 'View1Controller',
        controllerAs: 'view1Ctrl'
      });
  }
})();
