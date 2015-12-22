var smartMirrorApp = angular.module('smartMirrorApp', [
  'ngRoute',
  'smartMirrorControllers'
]);

smartMirrorApp.config(['$routeProvider',
  function($routeProvider) {
    $routeProvider
      .when('/smartdash', {
        templateUrl: 'views/dashboard.html',
        controller: 'DashController'
      })
      .otherwise({
        redirectTo: '/smartdash'
      });
  }]);