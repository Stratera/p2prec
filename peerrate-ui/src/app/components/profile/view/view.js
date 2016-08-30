angular.module("app.components.profile.view", [
  "services.component",
  "models.user"
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

function ViewProfileController($scope, $state, component, $http) {
  var vm = this;

  vm.viewProfilePage = function() {
    $state.go("editProfile");
  };

  vm.date = new Date();

  // $http.get('/auth.py').then(function(response) {
  //   $scope.user = response.data;
  // });
  //
  // $scope.saveMessage = function(message) {
  //   $scope.status = 'Saving...';
  //
  //   $http.post('/add-msg.py', message ).then(function(response) {
  //     $scope.status = '';
  //   }).catch(function() {
  //     $scope.status = 'Failed...';
  //   });
  // };
}
