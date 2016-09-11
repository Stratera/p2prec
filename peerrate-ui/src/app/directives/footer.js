angular.module("app.directives.footer",[])
.directive('footerTemplate', [function() {
  return {
    restrict: 'EA',
    scope: true,
    replace: true,
    templateUrl: 'app/directives/footer.html',
    link: function(scope, element, attrs) {
    }
  }
}]);
