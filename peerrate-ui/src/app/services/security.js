/**
 * @ngdoc module
 * @name common.services.security
 */
angular.module("common.services.security", [
    "app.models.user"
])
.factory("security", function ($rootScope, $q, servicesConfig, $http, $window, User) {

    /**
     * @ngdoc service
     * @name security
     *
     * @description
     * Handles application security and mananging the logged in user.
     */
    var security = {

        /**
         * @name user
         *
         * @description
         * The currently logged in user.
         */
        user: null,

        /**
         * @name isLoggedIn
         *
         * @description
         * Specifies if the app assume the user is logged in.
         */
        isLoggedIn: function () {
            return !!this.user;
        },

        /**
         * @name loginPending
         *
         * @description
         * returns true if in the middle of a login attempt
         */
        isLoginPending: function () {
            return !!this.loggingIn;
        },

        /**
         * @name isAuthorized
         * @param {String} authorization The required access level the user needs to be authorized.
         */
        isAuthorized: function (access) {
            return this.isLoggedIn() && this.user.isAuthorized(access);
        },

        /**
         * @name loadUser
         *
         * @description
         * Assuming the user is logged in, try to load the user from the services.
         */
        loadUser: function () {
            var ctx = this;
            var loginPromise, ret = false;
            var prefix = servicesConfig.useAuth ? servicesConfig.prefix : "/mock";
            if (!ctx.isLoginPending()) {
                ret = loginPromise = $http.post(
                    prefix + "/users/authentication",
                    {},
                    { headers: { "Accepts": "application/json" } })
                    .success(function (data, status, headers, config) {
                        if (!data.username) {
                            ctx.login();
                        } else {
                            ctx.user = User.$build(data);
                            ctx.loggingIn = false;
                        }
                    })
                    .catch(function () {
                        ctx.login();
                        throw "Failed Authentication";
                    });
                ctx.loggingIn = true;
            }
            return ret;
        },

        /**
         * @name login
         *
         * @description
         * Attempts to login via automatic SSO.
         */
        login: function () {

        }

    };

    return security;
});
