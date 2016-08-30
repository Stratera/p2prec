angular.module('app.unauthorized', [
    'services.router'
])
    .config(function (routeProvider) {
        routeProvider.register({
            name: "unauthorized",
            controller: "UnauthorizedController",
            templateUrl: "app/unauthorized/unauthorized.tpl.html",
            isUnauthorized: true
        });
    })

    .controller('UnauthorizedController', function() {});