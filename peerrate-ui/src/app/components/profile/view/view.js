angular.module("app.components.profile.view", [
  "services.component",
  "services.security",
  "models.user",
  "models.profile",
  "app.directives.profilePic"
])

  .config(function (componentProvider) {
    componentProvider.register({
      name: 'viewProfile',
      title: 'View Profile',
      frontpage: true,
      controller: 'ViewProfileController',
      controllerAs: 'viewProfileController',
      templateUrl: 'app/components/profile/view/view.tpl.html',
      access: "",
      scope: true,
      reloadOnSearch: true,
      queryParams: [
        'p',
      ]
    })
  })

  .controller("ViewProfileController", ViewProfileController);

function ViewProfileController($scope, $state, security, Profile) {
  var vm = this;
  vm.user = security.user;
  vm.profileModel = Profile;
  vm.allProfiles = [];
  vm.myProfile = {};
  vm.profilesInUserDept = []; 
  vm.profilePageData = {};

  vm.viewProfilePage = function() {
    $state.go("editProfile");
  };

  vm.date = new Date();
  vm.recognition= "viewAll";

  $scope.$on('router.restore.state', function (event, to) {

    // get all profiles
    vm.getAllProfiles().$then(function (response) {
      var ctx = vm;
      ctx.allProfiles = response.$response.data;
      response.$response.data.forEach(function (val, idx) {
        // find my profile
        if (val.email === ctx.user.email) {
          ctx.myProfile = angular.copy(val);
        }
      });
    });

    // if a user profile id is provided, view that user's profile
    if (!!to.data.p) {
      vm.userData = vm.profileModel.$find(to.data.p).$then(function (response) {
        return response.$response.data;
      });
    }
  });

}

ViewProfileController.prototype = {

  provideRecognition: function(userId) {
    ngDialog.open({ template: 'recognition.tpl.html.html', className: 'ngdialog-theme-default' });
  },

  getAllProfiles: function() {
    this.profileModel.$collection().$fetch().$then(function (response) {
      return response;
    })
  },

  loadProfileById: function(id) {
    this.state.go('viewProfile', {
      p: id
    });
  }

};
