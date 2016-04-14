var smartMirrorControllers = angular.module('smartMirrorControllers', []);

smartMirrorControllers.controller('DashController', ['$scope', '$interval', '$http', 'VtService', '$location', '$routeParams', '$window',
  function ($scope, $interval, $http, VtService, $location, $routeParams, $window) {
    var tick = function() {
      $scope.clock = Date.now();
    }
    if(typeof($routeParams.reloads) === "undefined") {
      $scope.reloads = 0;
    } else {
      $scope.reloads = parseInt($routeParams.reloads);
    }
    VtService.refreshSocket();

    //Oldschool REST Call :)
    var fetchTransports = function() {
      $http.get("/upcoming").then(function(response) {
        $scope.transports = response.data.transports;
      });
    }

    $scope.$on('transportsMessage', function(event, broadcastMessage) {
        $scope.transports = broadcastMessage.transports;
    });

    $scope.$on('socketClose', function(event, wsEvent) {
        setTimeout(function() {
          $scope.reloads = $scope.reloads + 1;
          VtService.refreshSocket();
        }, 5000);
    });

    tick();
    $interval(tick, 1000);
  }]);