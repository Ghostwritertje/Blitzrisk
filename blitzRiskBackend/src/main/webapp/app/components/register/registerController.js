'use strict';
angular.module('blitzriskControllers').controller('RegisterController', ['$scope', '$http', '$location', 'LoginService',
    function ($scope, $http, $location, LoginService) {
        $scope.username = '';
        $scope.password = '';
        $scope.email = '';

        $scope.registering = false;
        $scope.registerSuccess = false;
        $scope.registerError = false;


        $scope.go = function (path) {
            $location.path(path);
        };





        $scope.register = function register(){
            $scope.registering = true;
            var promise = LoginService.register($scope.username, $scope.password, $scope.email);

            promise.then(function() {
                $scope.registering = false;
                $scope.registerSuccess = true;
            }, function() {
                $scope.registering = false;
                $scope.registerError = true;

            });
        };


    }
]);