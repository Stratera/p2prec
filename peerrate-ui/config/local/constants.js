angular
    .module('app.constants', [])
    .constant('servicesConfig', {
        prefix: "https://urlhere",
        useAuth: "",
        build: "@@build@@",
        version: "@@version@@"
    })
    .constant('appConfig', {
        debug: "true"
    })
    .constant('eventNames', {
        sampleEvent: "sampleEvent"
    });
