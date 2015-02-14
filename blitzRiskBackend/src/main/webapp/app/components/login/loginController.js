'use strict';
angular.module('blitzrisk.controllers').controller('loginCtrl', ['$scope', '$http', '$location', 'loginService', '$q',
    function ($scope, $http, $location, loginService, $q) {


        function logIn(name, password) {
            var deferred = $q.defer();

            $http.get('http://localhost:8080/BlitzRisk/api/login', {headers: {'name': name, 'password': password}})
                .success(function (data) {

                    deferred.resolve(data);
                    //token = data;
                }).
                error(function () {
                    deferred.reject('Wrong log in details');
                });


            return deferred.promise;
        }

        var promise = logIn('Joran', 'joran');

        promise.then(function(message) {
            alert('Success!\nToken:' + message);
        }, function(reason) {
            alert('Failed: ' + reason);
        }, function(update) {
            alert('Got notification: ' + update);
        });

        $scope.go = function (path) {
            $location.path(path);
        }
    }
]);