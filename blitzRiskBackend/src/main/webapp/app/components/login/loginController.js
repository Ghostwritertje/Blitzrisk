'use strict';
angular.module('blitzriskControllers').controller('LoginController', ['$scope', '$http', '$location', 'LoginService',
    function ($scope, $http, $location, LoginService) {
        $scope.username = '';
        $scope.password = '';



        $scope.go = function (path) {
            $location.path(path);
        };

        $scope.logIn = function login(){
            var promise = LoginService.login($scope.username, $scope.password);

            promise.then(function(message) {
                $location.path("/game");
            }, function(reason) {
                alert('Failed: ' + reason);
            }, function(update) {
                alert('Got notification: ' + update);
            });
        };
    }
]);