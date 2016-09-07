angular.module("app.components.profile.edit", [
    "services.component",
    "models.user",
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

function EditProfileController($scope, $location, $state, $stateParams, $timeout, component, user) {
    var vm = this;
    // this.editProfileForm;
    vm.userData = {
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
            year:   2005
        },
        birthday:    {
            month:  "04",
            day:    "19",
            year:   1978,
            public: false
        },
        additionalInfo: "I love javascript!!!!"
    };
    vm.openUploadDialog = function() {

  };
}

EditProfileController.prototype = {
    submitForm: function () {
        var vm = this;
        // REST endpoint edit? user
        var newUser = {};
        newUser = vm.userData;

        user.$create(newUser).$then(function (response) {
            console.log(response);
        });
    },

    loadUser: function () {
        // replace default data with actual user
    }


};
