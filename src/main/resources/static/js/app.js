var smartMirrorApp = angular.module('smartMirrorApp', [
    'ngRoute',
    'smartMirrorControllers',
    'smartMirrorServices'
]);

smartMirrorApp.config(['$routeProvider',
    function ($routeProvider) {
        $routeProvider
            .when('/smartdash', {
                templateUrl: 'views/dashboard.html',
                controller: 'DashController'
            })
            .when('/smartdash/:reloads', {
                templateUrl: 'views/dashboard.html',
                controller: 'DashController'
            })
            .otherwise({
                redirectTo: '/smartdash'
            });
    }]);

Date.prototype.getWeek = function () {
    var d = new Date();
    // Copy date so don't modify original
    d = new Date(Date.UTC(d.getFullYear(), d.getMonth(), d.getDate()));
    // Set to nearest Thursday: current date + 4 - current day number
    // Make Sunday's day number 7
    d.setUTCDate(d.getUTCDate() + 4 - (d.getUTCDay() || 7));
    // Get first day of year
    var yearStart = new Date(Date.UTC(d.getUTCFullYear(), 0, 1));
    // Calculate full weeks to nearest Thursday
    return Math.ceil((((d - yearStart) / 86400000) + 1) / 7);
}