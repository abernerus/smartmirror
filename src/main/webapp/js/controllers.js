var smartMirrorControllers = angular.module('smartMirrorControllers', []);

smartMirrorControllers.controller('DashController', ['$scope', '$interval',
  function ($scope, $interval) {
    $scope.test = 'hello';


    var tick = function() {
      $scope.clock = Date.now();
    }
    tick();
    $interval(tick, 1000);
  }]);