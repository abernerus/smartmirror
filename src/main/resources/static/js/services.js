var smartMirrorServices = angular.module('smartMirrorServices', []);

smartMirrorServices.factory('VtService', ['$q', '$rootScope', function($q, $rootScope) {
    //console.log("init service");

    var self = this;
    // We return this object to anything injecting our service
    var Service = {};
    // Keep all pending requests here until they get responses
    var callbacks = {};
    // Create a unique callback ID to map requests to responses
    var currentCallbackId = 0;
    // Create our websocket object with the address to the websocket

    Service.refreshSocket = function() {
      //console.log("refresh socket");

      var loc = window.location, new_uri;
      if (loc.protocol === "https:") {
          new_uri = "wss:";
      } else {
          new_uri = "ws:";
      }
      new_uri += "//" + loc.host;
      new_uri += loc.pathname + "/transportsHandler";

      var ws = new WebSocket(new_uri);

      ws.onopen = function(){
          //console.log("Socket has been opened!");
      };

      ws.onmessage = function(jsonMessage) {
        message = JSON.parse(jsonMessage.data);
        console.log(message.type);
        if(message) {
          if(message.type == "TRANSPORTS") {
            $rootScope.$broadcast('transportsMessage', {
              transports: message.content.transports
            });
          } else if(message.type == "TEMPERATURE") {
            $rootScope.$broadcast('temperatureMessage', {
              temperature: message.content.temperature
            });
          } else if(message.type == "TEXT") {
             $rootScope.$broadcast('mirrorMessage', message.content.mirrorMessage);
          } else if(message.type == "WEATHER") {
              console.log(message.content);
             $rootScope.$broadcast('weatherMessage', message.content);
          } else if(message.type == "NOW_PLAYING") {
             $rootScope.$broadcast('nowPlayingMessage', message.content);
          } else if(message.type == "TASKS") {
             $rootScope.$broadcast('tasks', message.content);
          }
        }
      };

      ws.onclose = function(event) {
        $rootScope.$broadcast('socketClose', {
          wsEvent: event
        });
      };
    }

    return Service;
}])