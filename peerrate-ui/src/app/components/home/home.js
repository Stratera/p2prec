angular.module("app.components.home", [
    "services.component"
])
.config(function (componentProvider){
    componentProvider.register({
        name: "home",
        title: "Home",
        persistent: true,
        static: true,
        url: "^/",
        controller: "HomeController",
        templateUrl: "app/components/home/home.tpl.html",
        access: ""
    });
})
.controller('HomeController', function ($rootScope, $scope, $state, component) {
    this.$state = $state;
    this.components = component.get().filter(function (c) {
        return c.frontpage;
    });
})