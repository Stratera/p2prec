/**
 * @ngdoc module
 * @name common.services.security
 */
angular.module("services.security", [
    "models.user"
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
         * Specifies if the app assumes the user is logged in.
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
            var ret = false;
            var prefix = servicesConfig.prefix;
            if (!ctx.isLoginPending()) {
                ret = $http.get(
                    prefix + "/users/authentication",
                    {},
                    { headers: { "Accepts": "application/json" } })
                    .then(function (data) {

                        if (!data.data.username) {
                            ctx.login();
                        } else {
                            ctx.user = User.$build(data.data);
                            ctx.loggingIn = false;
                        }
                    },
                    function (msg) {
                        if ($window.location.hostname === 'localhost') {
                            var userAccount = {
                                "username" : "jdoe@strateratech.com",
                                "email" : "jdoe@strateratech.com",
                                "firstName" : "John",
                                "lastName" : "Doe",
                                "tenant" : "https://idp.ssocircle.com",
                                "authorities" : [ "PEER_DISCOVER", "PEER_RATE" ]
                            };
                            ctx.user = userAccount;
                            ctx.loggingIn = false;
                        } else {
                        ctx.login();
                        console.log("Failed Authentication");
                        }
                    });
                ctx.loggingIn = true;
            }
            return ret;

        },

        login: function () {
            if ($window.location.hostname !== 'localhost') {
                $window.location.replace(servicesConfig.prefix + "/#/");
            }
        }

    };

    return security;
});
