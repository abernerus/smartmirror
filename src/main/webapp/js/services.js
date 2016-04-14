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
        if(typeof(message.transports) !== "undefined" ) {
          $rootScope.$broadcast('transportsMessage', {
            transports: message.transports
          });
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