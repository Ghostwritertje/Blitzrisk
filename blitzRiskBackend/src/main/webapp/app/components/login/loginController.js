'use strict';
angular.module('blitzriskControllers').controller('LoginController', ['$scope', '$http', '$location', 'LoginService',
    function ($scope, $http, $location, LoginService) {
        $scope.username = '';
        $scope.password = '';

        $scope.newUsername = '';
        $scope.newPassword = '';
        $scope.newEmail = '';


        $scope.go = function (path) {
            $location.path(path);
        };

        $scope.logIn = function login(){
            alert('Username: ' + $scope.username);
            var promise = LoginService.login($scope.username, $scope.password);

            promise.then(function(message) {
                $location.path("/game");
            }, function(reason) {
           //     alert('Failed: ' + reason);
            }, function(update) {
                //TODO: Proper errormessage
                alert('Got notification: ' + update);
            });
        };



        $scope.register = function register(){
            var promise = LoginService.register($scope.newUsername, $scope.newPassword, $scope.newEmail);

            promise.then(function() {
                $location.path("/login");
            }, function() {
                alert('Could not register');
            });
        };


    }
]);