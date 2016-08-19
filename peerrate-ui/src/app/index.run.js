(function() {
  'use strict';

  angular
    .module('dhsChallenge')
    .run(runBlock);

  /** @ngInject */
  function runBlock($log) {

    $log.debug('runBlock end');
  }

})();
