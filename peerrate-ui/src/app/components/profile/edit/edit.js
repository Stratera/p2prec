angular.module("app.components.profile.edit", [
    "services.component",
    "services.security",
    "app.directives.profilePic"
])

    .config(function (componentProvider) {
        componentProvider.register({
            name: 'editProfile',
            title: 'Edit Profile',
            controller: 'EditProfileController',
            controllerAs: 'editProfileController',
            templateUrl: 'app/components/profile/edit/edit.tpl.html',
            access: "",
            frontpage: true,
            scope: true,
            reloadOnSearch: true,
            queryParams: []
        })
    })

    .controller("EditProfileController", EditProfileController);

function EditProfileController($scope, $location, $state, $stateParams, $timeout, component, security) {
    var vm = this;
    vm.profileData = Profile.$find(security.user.id).$then(function (response) {
        return response;
    },
    function (err) {
        return require('edit.mock');
    });
    // this.editProfileForm;
    vm.openUploadDialog = function() {

  };


}

EditProfileController.prototype = {
    submitForm: function () {
        var vm = this;
        // REST endpoint edit? user
        var newUser = {};
        newUser = vm.userData;

        vm.userModel.$create(newUser).$then(function (response) {
            console.log(response);
        });
    },

    loadUser: function () {
        // replace default data with actual user
    }


};
