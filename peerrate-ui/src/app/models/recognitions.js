angular.module("models.recognitions", [
    "restmod"
])

/**
 * @property {String} username
 * @property {String} email
 * @property {String} authorities
 */
.factory("Recognitions", function (model) {

    return model("/recognitions").mix({
        
    });

});