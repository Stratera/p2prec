angular.module("app.components.profile.view", [
  "services.component",
  "models.user",
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
      queryParams: []
    })
  })

  .controller("ViewProfileController", ViewProfileController);

function ViewProfileController($scope, $state) {
  var vm = this;

  vm.viewProfilePage = function() {
    $state.go("editProfile");
  };

  vm.date = new Date();
}

ViewProfileController.prototype = {
  provideRecognition: function(userId) {
    ngDialog.open({ template: 'recognition.tpl.html.html', className: 'ngdialog-theme-default' });
  },

  loadUser: function($http) {
    $http({
      method: 'GET',
      url: '/userprofiles/100000'
    }).then(function successCallback(response) {
      vm.userData = response;
    }, function errorCallback(response) {
      // called asynchronously if an error occurs
      // or server returns response with an error status.
    });
  }

};
