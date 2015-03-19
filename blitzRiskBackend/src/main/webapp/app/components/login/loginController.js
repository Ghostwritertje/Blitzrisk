/**
 * controlling the login page.
 */
'use strict';
angular.module('blitzriskControllers').controller('LoginController', ['$scope', '$rootScope',  '$http', '$location', '$log', 'LoginService',
    function ($scope, $rootScope, $http, $location, $log, LoginService) {
        $scope.username = '';
        $scope.password = '';

        $scope.busy = false;
        $scope.loginError = false;

        $log.info("Hello, world!");

        $scope.go = function (path) {
            $location.path(path);
        };

        $scope.logIn = function login(){
            $scope.busy = true;
            var promise = LoginService.login($scope.username, $scope.password);

            promise.then(function(message) {
                $rootScope.myUsername = $scope.username;
                $rootScope.loggedIn = true;
                $location.path("/overview");
            }, function(reason) {
           //     alert('Failed: ' + reason);
                
                $scope.busy = false;
                $scope.loginError = true;

            });
        };






    }
]);