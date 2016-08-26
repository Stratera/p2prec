/**
 * @ngdoc module
 * @name common.restmod.BaseModel
 */
angular.module("services.restmod.BaseModel", [])

/**
 * @ngdoc type
 * @name BaseModel
 * @description
 * A place for global model enhancements
 */
.factory("BaseModel", function (restmod, servicesConfig) {
    var servicePrefix = servicesConfig.prefix;
    return restmod.mixin ({
        $config: {
            style: "DHS",
            urlPrefix: servicePrefix,
            primaryKey: "id"
        },
        $extend: {

            Model: {
                unpack: function (self, _raw) {
                    return _raw.list || _raw;
                }
            },

            Record: {
                $extend: function (_other) {
                    this.$dispatch("before-extend", _other);
                    var value = this.$super(_other);
                    this.$dispatch("after-extend", _other);
                    return value;
                }
            }

        },
        $hooks: {
            'before-request': function (req) {
                if ((new this.$type()).useMock) {
                    req.url = req.url.replace(/https.*dhsrestweb/, "/mock");
                }
            }
        }
    });
});
