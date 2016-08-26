angular
    .module('app.constants', [])
    .constant('servicesConfig', {
        prefix: "@@svrPrefix@@",
        useAuth: "@@useAuth@@",
        build: "@@build@@",
        version: "@@version@@"
    })
    .constant('appConfig', {
        debug: "@@debug@@"
    })
    .constant('eventNames', {
        sampleEvent: "sampleEvent"
    });
