'use strict';

angular.module('blitzriskServices').factory('LoginService', ['$http', '$q',
    function ($http, $q) {
        var hosturl = "http://localhost:8080/BlitzRisk/api/login";
        var token = null;
        var username;
        var password;

        function logMeIn() {
            var deferred = $q.defer();  //maak promise

            $http.get('http://localhost:8080/BlitzRisk/api/login', {headers: {'name': username, 'password': password}})
                .success(function (data) {
                    token = data;
                    deferred.resolve(data);

                }).
                error(function () {
                    deferred.reject('Wrong log in details');
                });
            return deferred.promise;
        }

        return {
            login: function logIn(name, pass) {
                username = name;
                password = pass;

                return logMeIn();
            },
            getToken: function () {
                var deferred = $q.defer();
                if(token!= null){
                    deferred.resolve(token);
                }else {
                    return logMeIn();
                }

            },
            getUsers: function () {
                return $http.get('http://localhost:8080/BlitzRisk/api/users');

            },
            register: function(name, pass, email){
                return $http.post(hosturl + 'user/' + name, {headers: {'password' : pass, 'email': email}});
            }
        }
    }]);

