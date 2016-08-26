angular.module("models.user", [
    "restmod"
])

/**
 * @property {String} username
 * @property {String} email
 * @property {String} authorities
 */
.factory("User", function (model) {
    
    return model("/users").mix({
        
        $hooks: {
            
            "after-extend": function () {
                this.$authoritySet = createAuthoritySet(this.authorities || []);
            }
            
        },
        
        $extend: {
            Record: {
                isAuthorized: function (authorization) {
                    return !authorization || !!this.$authoritySet[authorization];
                }
            }
        }
        
    });

});

function createAuthoritySet (authorities) {
    var authoritySet = {};
    authorities.forEach(function (a) {
        authoritySet[a] = true;
    });
    return authoritySet;
}