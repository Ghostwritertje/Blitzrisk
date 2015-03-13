'use strict';
angular.module('blitzriskControllers').controller('RegisterController', ['$scope', '$http', '$location', '$log', 'LoginService',
    function ($scope, $http, $location, $log, LoginService) {
        $scope.username = '';
        $scope.password = '';
        $scope.email = '';

        $scope.registering = false;
        $scope.registerSuccess = false;
        $scope.registerError = false;

        $scope.duplicateUsername = false;
        $scope.duplicateEmail = false;


        $scope.go = function (path) {
            $location.path(path);
        };





        $scope.register = function register(){
            $scope.registering = true;
            $scope.duplicateUsername = false;
            $scope.duplicateEmail = false;
            $log.info("Registering");
            var promise = LoginService.register($scope.username, $scope.password, $scope.email);

            promise.then(function() {
                $scope.registering = false;
                $scope.registerSuccess = true;
            }, function(payload) {
                $log.error("Registererror: " + payload.data);
                $scope.registering = false;
                $scope.registerError = true;

                if(payload.data === 'Duplicate username'){
                    $scope.duplicateUsername = true;
                }else {
                    $scope.duplicateEmail = true;
                }

            });
        };


    }
]);