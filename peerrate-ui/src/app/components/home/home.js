angular.module("app.components.home", [
    "services.component"
])
.config(function (componentProvider){
    componentProvider.register({
        name: "home",
        title: "Home",
        persistent: true,
        frontpage: true,
        static: true,
        url: "^/",
        controller: "HomeController",
        templateUrl: "app/components/home/home.tpl.html",
        access: ""
    });
})
.controller('HomeController', function ($rootScope, $scope, $state, component) {
    var vm = this;
    vm.$state = $state;
    vm.components = component.get().filter(function (c) {
        return c.frontpage;
    });
});
