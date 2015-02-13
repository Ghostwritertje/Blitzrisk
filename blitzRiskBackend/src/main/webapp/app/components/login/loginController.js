'use strict';
angular.module('blitzrisk.controllers').controller('loginCtrl', ['$scope','$http', '$location', 'loginService',
    function ($scope, $http, $location, loginService) {

        //loginService.login(function(token){$scope.test = token;}, 'gunther', 'claessens');
        /*$scope.config = JSON.parse("/assets/config.json");*/
       /* var  req = {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'password': 'claessens'
            }
        };*/
            var promise =loginService.login('gunther', 'claessens');
            promise.then(
                function(data) {
                    $scope.test = loginService.getToken();
                },
                function(data) {
                    $scope.test= data;
                });

        $scope.go = function(path){
            $location.path(path);
        }
    }
]);