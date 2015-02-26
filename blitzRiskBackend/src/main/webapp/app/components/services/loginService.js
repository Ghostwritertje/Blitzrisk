'use strict';

angular.module('blitzriskServices').factory('LoginService', ['$http', '$q',
    function ($http, $q) {
        var hosturl = 'api/';
        var token = null;
        var isLoggedIn = false;
        var username;
        var password;


        function logMeIn() {
            var deferred = $q.defer();  //maak promise

            var passHash = CryptoJS.SHA512(password);
            $http.get(hosturl + 'login', {headers: {'name': username, 'password': passHash}})
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

        return {
            login: function logIn(name, pass) {
                username = name;
                password = pass;

                return logMeIn();
            },
            logOut: function logOut() {
                token = null;
            },
            getToken: function () {
                var deferred = $q.defer();
                if (token != null) {
                    deferred.resolve(token);
                } else {
                    return logMeIn();
                }

            },
            authenticate: function (pass) {
                return angular.equals(pass, password);
            },
            updateUser: function (name, email, pass) {
                var deferred = $q.defer();

                $http.put(hosturl + 'user', {
                    'name': name,
                    'password': pass,
                    'email': email
                }, {headers: {'X-Auth-Token': token}})
                    .success(function () {
                        username = name;
                        if (pass != null) password = pass;

                        var promise = logMeIn();
                        promise.then(function () {
                            deferred.resolve();
                        });


                    });

                return deferred.promise;
            },
            getUserDetails: function () {
                var deferred = $q.defer();

                $http.get(hosturl + "user", {headers: {'X-Auth-Token': token}})
                    .success(function (data) {
                        deferred.resolve({"username": data.name, "email": data.email});
                    }).
                    error(function () {
                        deferred.reject('Token Expired');
                    });
                return deferred.promise;

            },
            getUsers: function () {
                return $http.get('api/users');

            },
            register: function (name, pass, email) {
                var passHash = CryptoJS.SHA512(pass);
                return $http.put(hosturl + 'user/' + name, null, {headers: {'email': email, 'password': passHash}});
            },
            isLoggedIn: function () {
                return isLoggedIn;
            }
        }
    }]);

