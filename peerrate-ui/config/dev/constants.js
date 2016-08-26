
'use strict';

angular
    .module('app.constants', [])
    .constant('servicesConfig', {
        prefix: "https://urlhere",
        useAuth: "true",
        build: "@@build@@",
        version: "@@version@@"
    })
    .constant('appConfig', {
        debug: "true"
    })
    .constant('eventNames', {
        sampleEvent: "sampleEvent"
    });
