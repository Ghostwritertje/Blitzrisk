'use strict';
angular.module('blitzrisk.controllers').controller('loginCtrl', ['$scope','$http', '$location', 'loginService',
    function ($scope, $http, $location, loginService) {
        /*loginService.login(function(token){$scope.test = token;}, 'gunther', 'claessens');*/
        /*$scope.config = JSON.parse("/assets/config.json");*/
       /* var  req = {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'password': 'claessens'
            }
        };*/
        $http.get('http://localhost:8080/BlitzRisk/api/api/users').success(function(data){$scope.test = data}).error(function(data){$scope.test =  'error'});
        /*$http.get('http://localhost:8080/BlitzRisk/api/users').
            success(function(data, status, headers, config) {
                $scope.test = data[0].name;
            }).
            error(function(data, status, headers, config) {
                $scope.test = data[0].name;
            });*/
/*$scope.test = loginService.login('gunther', 'claessens');*/
            /*var promise =loginService.login('gunther', 'claessens');
            promise.then(
                function(data) {
                    $scope.test = 'test';
                },
                function(data) {
                    $scope.test= data.data;
                });*/

        $scope.go = function(path){
            $location.path(path);
        }
    }
]);