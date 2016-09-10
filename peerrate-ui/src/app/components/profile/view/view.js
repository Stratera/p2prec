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

function ViewProfileController($scope, $state, ngDialog) {
  var vm = this;

  vm.viewProfilePage = function() {
    $state.go("editProfile");
  };

  vm.date = new Date();

  vm.userData = {
    firstName:              "Indira",
    lastName:               "Vaddiparti",
    middleName:             "P",
    department:             "IT",
    city:                   "Alexandria",
    state:                  "VA",
    zip:                     "20841",
    phone:                  "123-456-7890",
    email:                  "ivaddiparti@strateratech.com",
    workAnniversaryDate:    {
      month:  "08",
      day:    "28",
      year:   "2005"
    },
    birthday:    {
      month:  "04",
      day:    "19",
      year:   "1978",
      public: false
    },
    additionalInfo: "I love javascript!!!!"
  };
  vm.recognition= "viewAll"
}

ViewProfileController.prototype = {
  provideRecognition: function(userId) {
    ngDialog.open({ template: 'recognition.tpl.html.html', className: 'ngdialog-theme-default' });
  }

};
