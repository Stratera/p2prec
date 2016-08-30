(function() {
  'use strict';

  angular
    .module('dhsChallenge')
    .config(config);

  /** @ngInject */
  function config (
    $logProvider,
    $httpProvider,
    $sessionStorageProvider,
    restmodProvider) {

    // Enable log
    $logProvider.debugEnabled(true);

    // configure local storage
    $sessionStorageProvider.setKeyPrefix("");

    // CORS support
    $httpProvider.defaults.useXDomain = true;
    $httpProvider.defaults.withCredentials = true;

    // Configure REST services
    restmodProvider.rebase("BaseModel");

  }

})();
