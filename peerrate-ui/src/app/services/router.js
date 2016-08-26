/**
 * @ngdoc module
 * @name common.services.router
 */
angular.module("common.services.router", [
  "ui.router",
  "common.services.security",
  "ngStorage"
])

  .constant('routerEventNames', {
    restoreState: "router.restore.state",
    persistState: "router.persist.state"
  })

  /**
   * @ngdoc provider
   * @name routeProvider
   */
  .provider("route", function ($stateProvider, $urlRouterProvider, $sessionStorageProvider) {

    /**
     * The name of the controller on the scope, so it can be accessed
     * by the template.
     */
    function getControllerScopeName(route) {
      if (route.controllerAs !== false) {
        return route.controllerAs || route.name;
      }
    }

    function createUrlFromRoute(route) {
      var name = route.name;
      var url = route.hasOwnProperty("url") ? route.url : "/" + name.substr(name.lastIndexOf(".") + 1);
      if (route.queryParams) {
        url += "?" + route.queryParams.join("&");
      }
      return url;
    }

    function createViewsFromRoute(route) {
      var routeView = {
        template: route.template,
        templateUrl: route.templateUrl,
        controller: route.controller,
        controllerAs: getControllerScopeName(route)
      };
      var views = {};
      if (route.viewContainer) {
        views[route.viewContainer] = routeView;
      } else {
        views["@"] = routeView;
      }
      return views;
    }

    // Default resolvers injected into views.
    // @ngInject
    var defaultResolvers = {
      currentUser: function (security) {
        return security.user;
      }
    };

    var routes = [];
    var defaultRoute;
    var unauthorizedRoute;

    function setupDefaultRoute() {
      $urlRouterProvider.otherwise(function () {
        // Since URLs are assumed to be a regexp, we need to remove the possible starting ^.
        return defaultRoute.url.replace(/\^/, "");
      });
    }

    /**
     * @ngdoc method
     * @name routeProvider#registerResolver
     * @description
     * Register a resolver that can inject a value into a route's controller.
     *
     * @param {String} name The local variable name of the resolved value.
     * @param {Function} resolverFn A function used to resolve a value.
     * This function can have injectable dependenies and return a promise.
     */
    this.registerResolver = function (name, resolverFn) {
      defaultResolvers[name] = resolverFn;
    };

    /**
     * ngdoc method
     * @name routeProvider#registerResolver
     * @description
     * Register a new route within the app, based on ui-router.
     *
     * @param {Object} route A ui-router state configuration object extended with extra options.
     * @param {String} route.name The name of the route.
     * @param {Object} [route.access] The required access level for the state.
     * @param {String} [route.viewContainer] The containing named ui-view where the route will be placed.
     */
    this.register = function (route) {
      var name = route.name;
      route.url = createUrlFromRoute(route);

      $stateProvider.state(name, angular.extend(angular.copy(route), {
        views: createViewsFromRoute(route),
        resolve: angular.extend(angular.copy(defaultResolvers), route.resolve || {}),
        data: {
          route: route
        }
      }));

      if (route.subroutes) {
        route.subroutes.forEach(function (r) {
          this.register(angular.extend(angular.extend({access: route.access}, r), {name: route.name + "." + r.name}));
        }, this);
      }

      if (route.isDefault) {
        if (defaultRoute) {
          throw Error("Trying to set default for state '" + name + "', but default is already set.");
        }
        defaultRoute = route;
        setupDefaultRoute();
      }
      if (route.isUnauthorized) {
        if (route.access) {
          throw Error("Unauthorized route cannot have access control.  It must remain public.");
        }
        if (unauthorizedRoute) {
          throw Error("Trying to set unauthorized state '" + name + "', but it's already set.");
        }
        unauthorizedRoute = route;
      }
      routes.push(route);
    };

    /**
     * @ngdoc service
     * @name route
     */
    this.$get = function ($state) {

      return {
        defaultRoute: defaultRoute,
        unauthorizedRoute: unauthorizedRoute,
        goToUnauthorized: function () {
          if (unauthorizedRoute) {
            $state.go(unauthorizedRoute.name);
          } else {
            throw Error("Unauthorized route has not been registered.");
          }
        }
      };

    };

  })
  .run(function ($rootScope, $location, $timeout, $state, $stateParams, route, security, routerEventNames, $sessionStorage) {
    var propagateTimer;

    function propagatePersistentState(params, state) {
      if (state.data) {
        var stor = ($rootScope.$storage && $rootScope.$storage[state.data.route.name]) || {};
        var data = $.extend(stor, params);
        $timeout.cancel(propagateTimer);
        propagateTimer = $timeout(function () {
          $rootScope.$broadcast(
            routerEventNames.restoreState,
            {
              state: state.data.route.name,
              data: data
            }
          );
        });
        return true;
      }
    }

    $rootScope.$storage = $sessionStorage;

    // respond to a request to persist state.
    $rootScope.$on(routerEventNames.persistState, function (event, attrs) {
      var name = $state.current.name;
      if (!$rootScope.$storage[name]) {
        $sessionStorage[name] = {};
      }

      Object.keys(attrs).forEach(function (k) {
        event.targetScope.$watch(attrs[k], function (newVal, oldVal) {
          if (oldVal !== newVal) {
            $rootScope.$storage[name][k] = newVal;
          }
        });
      });
    });

    $rootScope.$on('$stateChangeStart', function (event, to, toParams, from, fromParams) {

      // Verify user can access the route, else, re-route them to unauthorized.
      if (!security.isAuthorized(to.data.route.access)) {
        event.preventDefault();
        if (!security.isLoggedIn()) {
          if (!security.isLoginPending()) {
            security.loadUser().then(function () {
              $state.transitionTo(to, toParams);
            });
          } else {
            $state.transitionTo("loading");
          }
          event.preventDefault();
        } else {
          route.goToUnauthorized();
        }
      } else {
        if (angular.equals(fromParams, toParams)) {
          $rootScope.$broadcast("router.queryParamsChanged", $stateParams);
        }
      }

    });

    $rootScope.$on('$stateChangeSuccess', function (event, to, toParams, from, fromParams) {
      document.title = to.data.route.title;
      propagatePersistentState(toParams, to);
    });

    $rootScope.$on("$stateChangeError", function (event, toState, toParams, fromState, fromParams, error) {
      console.error(error);
    });

  });
