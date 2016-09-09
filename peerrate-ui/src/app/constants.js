angular
    .module('app.constants', [])
    .constant('servicesConfig', {
        prefix: "ec2-54-244-188-70.us-west-2.compute.amazonaws.com",
        useAuth: "true"
        // build: "@@build@@",
        // version: "@@version@@"
    })
    .constant('appConfig', {
        debug: "@@debug@@"
    })
    .constant('eventNames', {
        sampleEvent: "sampleEvent"
    });
