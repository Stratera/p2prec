(function() {
  'use strict';

  angular.module('view1',[])
    .controller('View1Controller', View1Controller);

  /** @ngInject */
  function View1Controller() {
    var vm = this;
    vm.name = "DHS Challenge";

    showAlert();

    function showAlert() {
      alert("hello" + vm.name);
    }
  }
})();
