angular.module("app.directives.profilePicIcons",[])
.directive('profilePicIcons', [function() {
  return {
    restrict: 'EA',
    scope: true,
    replace: true,
    templateUrl: 'app/directives/profilePicIcons.tpl.html',
    link: function(scope, element, attrs) {
    }
  }
}]);
