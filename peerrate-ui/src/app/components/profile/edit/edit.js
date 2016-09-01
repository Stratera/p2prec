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

    .controller("EditProfileController", EditProfileController);

function EditProfileController($scope, component) {
    var ctx = this;
    // this.editProfileForm;
    ctx.userData = {
        firstName:              "Indira",
        lastName:               "Vaddiparti",
        middleName:             "P",
        department:             "IT",
        city:                   "Alexandria",
        state:                  "VA",
        zip:                     "20841",
        phone:                  "123-456-7890",
        email:                  "ivaddiparti@strateratech.com",
        workAnniversaryDate:    {
            month:  "08",
            day:    "28",
            year:   "2005"
        },
        birthday:    {
            month:  "04",
            day:    "19",
            year:   "1978",
            public: false
        },
        additionalInfo: "I love javascript!!!!"
    };

    // $scope.$watch("this.userData.firstName", function (newVal, oldVal) {
    //     var isValid = newVal >= 1 ? true : false;
    //     ctx.userData.firstName.$setValidity("firstName", isValid);
    // });

    // $scope.$watch('editProfileForm', function () {
    //
    //     this.user.firstName.length >= 1 ? this.editProfileForm : true;
    // });


}

EditProfileController.prototype = {
    submitForm: function () {
        // REST endpoint edit? user
        var newUser = {};
        newUser = this.userData;

        userData.$create(newUser).$then(function (response) {
            console.log(response);
        });
    },

    loadUser: function () {
        // replace default data with actual user
    }


};
