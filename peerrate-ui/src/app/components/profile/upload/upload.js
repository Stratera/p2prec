/**
 * this module is used to upload your profile picture
 */
angular.module("app.components.profile.upload", [
  "services.component",
  "models.user"
])

  .config(function (componentProvider) {
    componentProvider.register({
      name: 'uploadPicture',
      title: 'Upload Profile Picture',
      controller: 'UploadPictureController',
      controllerAs: 'uploadPictureController',
      templateUrl: 'app/components/profile/upload/upload.tpl.html',
      access: "",
      frontpage: true,
      scope: true,
      reloadOnSearch: true,
      queryParams: []
    })
  })

  .controller("UploadPictureController", UploadPictureController);



function UploadPictureController($scope, $location, $state, $stateParams, $timeout, component) {
  var ctx = this;

  ctx.directive('fileModel', ['$parse', function ($parse) {
    return {
      restrict: 'A',
      link: function(scope, element, attrs) {
        var model = $parse(attrs.fileModel);
        var modelSetter = model.assign;

        element.bind('change', function(){
          scope.$apply(function(){
            modelSetter(scope, element[0].files[0]);
          });
        });
      }
    };
  }]);
}
