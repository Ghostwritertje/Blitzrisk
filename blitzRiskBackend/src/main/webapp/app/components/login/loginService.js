'use strict';

angular.module('blitzrisk.services').factory('loginService', ['$http', '$q',
    function ($http, $q) {
        var hosturl = "http://localhost:8080/BlitzRisk/api/login";
        var token = null;
        var username;
        var password;



        return {
            login:  function () {

             /*   var delay = $q.defer();
                $http.get('http://localhost:8080/BlitzRisk/api/login', {headers: {'name': 'Joran', 'password': 'joran'}})
                    .success(function (data) {
                        token = data;
                        delay.resolve();
                    }).
                    error(function () {
                        delay.reject();
                    });
                return delay.promise();*/

            },
            getToken: function () {

                return token;
            },
            getUsers: function () {
                return $http.get('http://localhost:8080/BlitzRisk/api/users');

            }
        }
    }]);

