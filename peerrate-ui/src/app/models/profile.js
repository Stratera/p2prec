angular.module("models.profile", [
    "restmod"
])

/**
 * @property {String} username
 * @property {String} email
 * @property {String} authorities
 */
.factory("Profile", function (model) {

    return model("/userprofiles").mix({
        
    });

});