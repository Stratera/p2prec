angular.module("app.components.profile.edit", [
    "services.component",
    "models.user"
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

    .controller("EditProfileController", EditProfileController)

function EditProfileController($scope, component, user) {
    var ctx = this;
    // this.editProfileForm;
    ctx.userData = {
        firstName:              "",
        lastName:               "",
        middleName:             "",
        department:             "",
        city:                   "",
        state:                  "",
        phone:                  "",
        email:                  "",
        workAnniversaryDate:    {
            month:  "",
            day:    "",
            year:   ""
        },
        birthday:    {
            month:  "",
            day:    "",
            year:   ""
        }
    };

    $scope.$watch("this.user.firstName", function (newVal, oldVal) {
        var isValid = newVal >= 1 ? true : false;
        ctx.user.firstName.$setValidity("firstName", isValid);
    });
    
    // $scope.watch("this.user.firstName", function () {
    //     this.user.firstName.length >= 1 ? this.editProfileForm : true;
    // });


}

EditProfileController.prototype = {
    submitForm: function () {
        // REST endpoint edit? user
        var newUser = {};
        newUser = this.userData;

        user.$create(newUser).$then(function (response) {
            console.log(response);
        });
    },

    loadUser: function () {
        // replace default data with actual user
    }


};
