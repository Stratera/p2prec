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

function ViewProfileController($scope, $state, component) {
  var vm = this;

  vm.viewProfilePage = function() {
    $state.go("editProfile");
  };
}
