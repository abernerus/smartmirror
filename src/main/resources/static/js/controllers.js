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
        console.log(broadcastMessage.transports);
        $scope.transports = broadcastMessage.transports;
    });

    $scope.$on('temperatureMessage', function(event, broadcastMessage) {
        $scope.temperature = broadcastMessage.temperature;
    });

    $scope.$on('mirrorMessage', function(event, broadcastMessage) {
      $scope.mirrorMessage = broadcastMessage;
    });

    $scope.$on('weatherMessage', function(event, weatherMessage) {

      $scope.weathers = [];
      for(i = 0; i < 3; i++) {
        var weather = weatherMessage[i];
        console.log(weather);
        weather.toDateTimeString = getDateTimeString(weather.toDateTime)
        weather.fromDateTimeString = getDateTimeString(weather.fromDateTime)
        $scope.weathers.push(weather);
      }
      console.log($scope.weathers)
      //$scope.weather = broadcastMessage.weatherMessage;
    });

    $scope.$on('nowPlayingMessage', function(event, broadcastMessage) {
      $scope.nowPlaying = broadcastMessage;
      console.log($scope.nowPlaying);
    });

    $scope.$on('tasks', function(event, broadcastMessage) {
          $scope.tasksHeader = 'Att Göra';
          $scope.tasks = broadcastMessage;
          console.log($scope.tasks);
    });

    $scope.$on('socketClose', function(event, wsEvent) {
        setTimeout(function() {
          $scope.reloads = $scope.reloads + 1;
          VtService.refreshSocket();
        }, 5000);
    });

    function getDateTimeString(dateTime) {
      var hour = dateTime.hour + "";
      if(dateTime.hour < 10) {
        hour = "0" + dateTime.hour;
      }

      var min = dateTime.minute + "";
      if(dateTime.minute < 10) {
        min = "0" + dateTime.minute;
      }

      return hour + ":" + min;
    }

    tick();
    $interval(tick, 1000);
  }]);
  /*
  Application name	Smartmirror
  API key	252dc026a57846110647f5cfecee77c3
  Shared secret	2a49073dff46e1c71cc5856d6fa33c4e
  Registered to	wimpbeef
  */