var module = angular.module("app.layout", [
    "app.constants",
    "services.router",
    "services.component"
]);

function LayoutController ($scope, $location, $state, $stateParams, $timeout, component, currentUser) {
    var vm = this;

    vm.user = currentUser;
    
    vm.componentPages = component.get().map(function (c, i) {
      var active = i === 0;
      if (active) {
          vm.activeIndex = i;
      }
      return {
          title: c.title,
          index: i,
          iconClass: c.iconClass,
          name: c.name,
          hidden: !c.static,
          persistent: c.persistent,
          savedState: null
      };
    }, this);

    vm.setPageSelected = function (page) {
        if (page.savedState) {
            $state.go(page.savedState.stateName, page.savedState.params);
        } else {
            $state.go(page.name);
        }
    };

    $scope.$on('$stateChangeSuccess', function (event, to) {
        vm.updateSelectedPageState();
    });

}

module.controller('LayoutController', LayoutController);

module.config(function (routeProvider, componentProvider) {
    routeProvider.register({
      name: "layout",
      templateUrl: "app/layout/layout.tpl.html",
      controller: "LayoutController",
    });

    componentProvider.registerParentRoute("layout", "content");
});
