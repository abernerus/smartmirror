var scrumpokerControllers = angular.module('scrumpokerControllers', []);

scrumpokerControllers.controller('DeckController', ['$scope', '$anchorScroll', '$timeout',
  function ($scope, $anchorScroll, $timeout) {
    $scope.test = 'hello';
    $scope.cards = ["0", "1/2", "1", "2", "3", "5", "8", "13", "20", "40", "100", "INFINITY", "UNKNOWN", "COFFEE"];

  }]);

scrumpokerControllers.controller('CardController', ['$scope', '$routeParams',
  function($scope, $routeParams) {

    $scope.cardNumber = $routeParams.cardId;

    $scope.includeWithNumber = function(number) {

        $scope.cardNumber = number;

    }

  }]);

scrumpokerControllers.controller('CardWrapperController', ['$scope', '$routeParams',
    function($scope, $routeParams) {
      $scope.cardNumber = $routeParams.cardId;
    }]);