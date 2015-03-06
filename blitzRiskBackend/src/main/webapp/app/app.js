'use strict';
var blitzrisk = angular.module('blitzrisk', [
    'blitzriskControllers',
    'blitzriskServices',
    '$httpProvider',
    function($httpProvider) {
    $httpProvider.defaults.useXDomain = true;
    delete $httpProvider.defaults.headers.common['X-Requested-With'];
}]);
var blitzriskControllers = angular.module('blitzriskControllers',['blitzriskServices']);
var blitzriskServices = angular.module('blitzriskServices', []);



angular.module('blitzriskControllers').controller('HomeController', ['$scope', '$rootScope',  '$http', '$routeParams', '$location', 'LoginService',
    function ($scope, $rootScope, $http, $routeParams, $location, LoginService) {
        $rootScope.loggedIn = false;
        $rootScope.myUsername = '';

        $scope.go = function go(path) {
            $location.path(path);
        };

        $scope.logOut = function logOut() {
            $rootScope.loggedIn = false;
            $rootScope.myUsername = '';
            LoginService.logOut();
            $location.path('/login');
        }
    }]);