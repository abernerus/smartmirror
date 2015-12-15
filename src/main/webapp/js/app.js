var scrumpokerApp = angular.module('scrumpokerApp', [
  'ngRoute',
  'scrumpokerControllers'
]);

scrumpokerApp.config(['$routeProvider',
  function($routeProvider) {
    $routeProvider.
      when('/deck', {
        templateUrl: 'views/deck.html',
        controller: 'DeckController'
      }).
      when('/card/:cardId', {
          templateUrl: 'views/cardwrapper.html',
          controller: 'CardWrapperController'
        }).
      otherwise({
        redirectTo: '/deck'
      });
  }]);