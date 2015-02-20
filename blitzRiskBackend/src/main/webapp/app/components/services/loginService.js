'use strict';

angular.module('blitzriskServices').factory('LoginService', ['$http', '$q',
    function ($http, $q) {
        var hosturl = "http://localhost:8080/BlitzRisk/api/";
        var token = null;
        var isLoggedIn = false;
        var username;
        var password;

        function logMeIn() {
            var deferred = $q.defer();  //maak promise
            var passwordHash = hashPassword(password);
            $http.get(hosturl + 'login', {headers: {'name': username, 'password': passwordHash}})
                .success(function (data) {
                    token = data;
                    deferred.resolve(data);
                    isLoggedIn = true;

                }).
                error(function () {
                    deferred.reject('Wrong log in details');
                });
            return deferred.promise;
        }

        function hashPassword(password){
            return CryptoJS.SHA256(password).toString()
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
                var passwordHash = hashPassword(pass);
                return $http.put(hosturl + 'user/' + name, null, {headers: {'email': email,'password' : passwordHash }});
            },
            isLoggedIn: function(){
                return isLoggedIn;
            }
        }
    }]);

