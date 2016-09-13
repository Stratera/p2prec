angular.module("app.components.home", [
    "services.component",
    "services.security",
    "models.profile",
    "models.recognitions"
])
.config(function (componentProvider){
    componentProvider.register({
        name: "home",
        title: "Home",
        persistent: true,
        url: "^/",
        controller: "HomeController",
        controllerAs: "homeController",
        templateUrl: "app/components/home/home.tpl.html",
        access: "",
        scope: true,
        queryParams: []
    });
})
.controller('HomeController', HomeController);

function HomeController($rootScope, $scope, $state, component, security, Profile, Recognitions) {
    var vm = this;

    vm.user = security.user;
    vm.recognitionsModel = Recognitions;
    vm.profileModel = Profile;
    vm.allProfiles = [];
    vm.allProfilesNamesAndId = [];
    vm.myProfile = {};
    vm.profilesInUserDept = [];
    vm.profilePageData = {};
    vm.pointCategories = [{id: 1, name: 'TEAMWORK'}, {id: 2, name:'IMPROVEMENT'}, {id: 3, name: 'DELIVERY'}, {id: 3, name: 'EXPERIMENT'}];
    vm.comment = "";
    vm.selectedPersonName = "";
    vm.selectedCategory = "";

    // get all profiles
    vm.profileModel.$collection().$fetch().$then(function (response) {
      var ctx = vm;
      ctx.allProfiles = response.$response.data;

      ctx.allProfiles.forEach(function (val, idx) {
        // find my profile
        if (val.email === ctx.user.email) {
          ctx.myProfile = angular.copy(val);
        }

        ctx.allProfilesNamesAndId[idx] = {
            name: val.fullName,
            id: val.id
        };
      });
    });

};

HomeController.prototype = {
    submitForm : function () {
        var ctx = this;
        ctx.allProfiles.forEach(function (val, idx) {
            if (selectedPersonName === val.id) {
                var date = new Date();

                ctx.recognitionsModel.$build(angular.copy({
                    "category": ctx.selectedCategory,
                    "messageText": ctx.comment,
                    "name": ctx.selectedPersonName,
                    "recipientDepartmentId": val.department.id,
                    "recipientUserProfileId": val.id,
                    "sendingUserProfileId": ctx.myProfile,
                    "submitTs": date
                })).$save();

            }
        });
    }
};
