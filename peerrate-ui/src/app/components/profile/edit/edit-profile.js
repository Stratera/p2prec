angular.module("app.components.profile.edit", [
    "services.component",
    "models.user"
])

    .config(function (componentProvider) {
        componentProvider.register({
            name: 'editProfile',
            title: 'Edit Profile',
            frontpage: true,
            controller: 'EditProfileController',
            controllerAs: 'editProfileController',
            templateUrl: 'app/components/profile/edit/edit-profile.tpl.html',
            access: "",
            scope: true,
            reloadOnSearch: true,
            queryParams: []
        })
    })

    .controller("EditProfileController", EditProfileController);

function EditProfileController($scope) {

}
