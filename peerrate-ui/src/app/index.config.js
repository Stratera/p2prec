(function() {
  'use strict';

  angular
    .module('dhsChallenge')
    .config(config);

  /** @ngInject */
  function config($logProvider) {
    // Enable log
    $logProvider.debugEnabled(true);
  }

})();
