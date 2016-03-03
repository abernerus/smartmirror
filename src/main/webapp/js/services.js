var smartMirrorServices = angular.module('smartMirrorServices', []);

smartMirrorServices.factory('VtService', ['$q', '$rootScope', function($q, $rootScope) {
    // We return this object to anything injecting our service
    var Service = {};
    // Keep all pending requests here until they get responses
    var callbacks = {};
    // Create a unique callback ID to map requests to responses
    var currentCallbackId = 0;
    // Create our websocket object with the address to the websocket
    var ws = new WebSocket("ws://localhost:18080/transportsHandler");

    ws.onopen = function(){
        console.log("Socket has been opened!");
    };

    ws.onmessage = function(jsonMessage) {
        message = JSON.parse(jsonMessage.data);
        if(typeof(message.transports) !== "undefined" ) {
          $rootScope.$broadcast('transportsMessage', {
            transports: message.transports
          });
        } else {
          console.log(message);
        }
    };

    return Service;
}])