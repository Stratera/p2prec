angular.module("app.directives.profilePic",[])
.directive('profile', [function() {
  return {
    restrict: 'EA',
    scope: true,
    replace: true,
    templateUrl: 'app/directives/profilepic.html',
    link: function(scope, element, attrs) {
      console.log(scope.value);
    }
  }
}]);
