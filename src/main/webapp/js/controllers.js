var smartMirrorControllers = angular.module('smartMirrorControllers', []);

smartMirrorControllers.controller('DashController', ['$scope', '$interval', '$http',
  function ($scope, $interval, $http) {
    $scope.test = 'hello';

    var tick = function() {
      $scope.clock = Date.now();
    }

    var fetchTransports = function() {
      $http.get("/upcoming").then(function(response) {
        $scope.transports = response.data.transports;
      });
    }


    tick();
    fetchTransports();
    $interval(tick, 1000);
    $interval(fetchTransports, 1000 * 60);

  }]);