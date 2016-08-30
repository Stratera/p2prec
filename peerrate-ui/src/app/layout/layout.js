function LayoutController ($scope, $location, $state, $stateParams, $timeout, component) {
    var vm = this;

    // vm.user = currentUser;

    vm.componentPages = component.get().map(function (c, i) {
      var active = i === 0;
      if (active) {
          vm.activeIndex = i;
      }
      return {
          title: c.title,
          index: i,
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

    vm.updateSelectedPageState = function() {
        var page = this.componentPages.filter(function (page) {
            return $state.includes(page.name);
        })[0];
        if (page) {
            page.savedState = {
                stateName: $state.current.name,
                params: angular.copy($stateParams)
            };
            this.activeIndex = page.index;
            page.hidden = false;
            this.selectedPage = page;
        }
    };

    $scope.$on('$stateChangeSuccess', function () {
        vm.updateSelectedPageState();
    });

}
angular.module("app.layout", [
    "app.constants",
    "services.router",
    "services.component"
])

.controller('LayoutController', LayoutController)

.config(function (routeProvider, componentProvider) {
    routeProvider.register({
      name: "layout",
      templateUrl: "app/layout/layout.tpl.html",
      controller: "LayoutController",
      controllerAs: "layoutController",
      access: "user",
      scope: true
    });

    componentProvider.registerParentRoute("layout", "content");
});
