angular.module("services.restmod.BaseModel", [])

.factory("BaseModel", function (restmod, servicesConfig) {
    var servicePrefix = servicesConfig.prefix;
    return restmod.mixin ({
        $config: {
            style: "DHS",
            urlPrefix: servicePrefix,
            primaryKey: "id"
        },
        
    })
})