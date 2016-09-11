
angular
  .module('dhsChallenge', [

    'ngAnimate',
    'ngCookies',
    'ngTouch',
    'ngSanitize',
    'ngMessages',
    'ngAria',
    'ngDialog',
    'ui.router',
    'ui.layout',
    'ui.grid',
    '720kb.datepicker',

    'restmod',

    'app.layout',
    'app.constants',
    'app.unauthorized',

    'app.components.home',
    'app.components.profile.edit',
    'app.components.profile.view',

    'app.directives.footer',
    'app.directives.profilePic',

    'models.user',

    'services.router',
    'services.security',
    'services.component',
    'services.restmod.BaseModel',
    'services.restmod.PostCollection'

    ]);

