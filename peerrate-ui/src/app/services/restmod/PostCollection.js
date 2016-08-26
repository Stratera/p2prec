/**
 * @ngdoc module
 * @name common.restmod.PostCollection
 */
angular.module("services.restmod.PostCollection", [])

/**
 * @ngdoc type
 * @name PostCollection
 * @description
 * A collection who's fetch method acts as a post.  This is useful if implementing a complex search on a model
 * that requires more than a GET request.
 */
.factory("PostCollection", ["restmod", "RMPackerCache", "inflector", function (restmod, packerCache, inflector) {

    return restmod.mixin (function () {

        this.define("Collection.$fetch", function (payload, params) {
            return this.$action(function () {
                var request = { method: "POST", url: this.$url("fetchMany"), params: this.$params, data: payload };
                if (params) {
                    request.params = request.params ? angular.extend(request.params, params) : params;
                }
                this.$dispatch("before-fetch-many", [request])
                    .$send(request, function (_response) {
                        if (_response.status === 200) {
                            this.$unwrap(_response.data)
                                .$dispatch("after-fetch-many", [_response]);
                        }
                    }, function (_response) {
                        this.$dispatch("after-fetch-many-error", [_response]);
                    }
                );
            });
        });

    });

}]);
