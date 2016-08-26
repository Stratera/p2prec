/**
 * @ngdoc module
 * @name services.component
 */
angular.module("services.component", [
    "services.router"
])
/**
 * @ngdoc provider
 * @name componentProvider
 */
.provider("component", function (routeProvider) {

    var parentRouteName;
    var parentViewName;

    var components = [];
    var componentByName = {};

    /**
     * @name registerParentRoute
     * @description
     * Register a parent containing route for components.  This needs
     * to be called before components are registered.
     *
     * @param {String} routeName
     * @param {viewName} [viewName] If provided, this represents the ui-view
     * where the component will be added to relative to the parent state.
     *
     * @method
     */
    this.registerParentRoute = function (routeName, viewName) {
        parentRouteName = routeName;
        parentViewName = viewName;
    };

    /**
     * @name register
     * @description
     * Register a new component.
     *
     * @param {Object} component A ui-router state based configuration object.
     *
     * @method
     */
    this.register = function (component) {
        components.push(component);
        componentByName[component.name] = component;
        if (!component.url) {
            component.url = "^/" + component.name.replace(".", "/");
        }
        component.parent = parentRouteName;
        component.viewContainer = parentViewName;
        routeProvider.register(component);
    };

    /**
     * @ngdoc service
     * @name component
     */
    this.$get = function ($state) {

        return {

            /**
             * @name get
             * @description
             * Get a component by name, or retrieve all components.
             *
             * @param {String} [name] The name of a component.
             *
             * @method
             */
            get: function (name) {
                return name ? componentByName[name] : components;
            }
        };
    };

});
