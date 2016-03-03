var smartMirrorControllers = angular.module('smartMirrorControllers', []);

smartMirrorControllers.controller('DashController', ['$scope', '$interval', '$http', 'VtService',
  function ($scope, $interval, $http, VtService) {
    var tick = function() {
      $scope.clock = Date.now();
    }

    //Oldschool REST Call :)
    var fetchTransports = function() {
      $http.get("/upcoming").then(function(response) {
        $scope.transports = response.data.transports;
        console.log($scope.transports);
      });
    }

    $scope.$on('transportsMessage', function(event, broadcastMessage) {
        $scope.transports = broadcastMessage.transports;
    })

    tick();
    $interval(tick, 1000);
  }]);