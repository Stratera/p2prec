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

    // .controller("EditFormController" , function ($rootScope, $scope) {
    //     var vm = this;


    //     $scope.$watch("vm.editProfileForm.firstName", function (newVal, oldVal) {
    //         if (newVal >= 1) {
    //             vm.firstName.$setValidity("firstName entered", true);
    //         } else {
    //             vm.firstName.$setValidity("firstName entered", false);
    //         }
    //     });
    // })

function EditProfileController($scope) {
    var ctx = this;
    // this.editProfileForm;
    this.user = {
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
    },


};
