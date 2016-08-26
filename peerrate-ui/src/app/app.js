
angular
  .module('dhsChallenge', [
    
    'ngAnimate',
    'ngCookies',
    'ngTouch',
    'ngSanitize',
    'ngMessages',
    'ngAria',
    'ui.router',
    'ui.bootstrap',
    'ui.layout',
    'ui.grid',

    'restmod',

    'app.layout',
    'app.constants',

    'app.components.home',
    'app.components.edit-profile',
    'app.components.my-profile',

    'models.user',

    'services.router',
    'services.security',
    'services.component',
    'services.restmod.BaseModel',
    'services.restmod.PostCollection'
    
    ]);

