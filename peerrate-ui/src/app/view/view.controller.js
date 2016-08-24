(function() {
  'use strict';

  angular.module('dhsChallenge')
    .controller('ViewController', ViewController);

  /** @ngInject */
  function ViewController() {
    var vm = this;
    vm.name = "DHS Challenge";

    showAlert();

    function showAlert() {
      alert("hello" + vm.name);
    }
  }
})();
