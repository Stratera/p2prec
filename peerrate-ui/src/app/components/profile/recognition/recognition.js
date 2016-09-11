/**
 * Created by Indira on 9/7/2016.
 */

angular.module("app.components.recognition.recognition", [
  "services.component",
  "models.user"
]).controller('RecognitionController',RecognitionController);


function RecognitionController($scope, ngDialog) {
  $scope.clickToOpen = function () {
    ngDialog.open({ template: 'recognition.tpl.html.html', className: 'ngdialog-theme-default' });
  };
}
