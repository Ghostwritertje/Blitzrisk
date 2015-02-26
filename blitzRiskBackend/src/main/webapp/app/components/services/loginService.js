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

            $http.get(hosturl + 'login', {headers: {'name': username, 'password': password}})
                .success(function (data) {
                    token = data;
                    isLoggedIn = true;

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
            logOut: function logOut() {
                token = null;
            },
            getToken: function () {
               return token;

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
            getUserName: function (){
                return username;
            },
            register: function (name, pass, email) {
                return $http.put(hosturl + 'user/' + name, null, {headers: {'email': email, 'password': pass}});
            },
            isLoggedIn: function () {
                return isLoggedIn;
            }
        }
    }]);

