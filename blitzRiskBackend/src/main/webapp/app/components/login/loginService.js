'use strict';

angular.module('blitzrisk.services').factory('loginService', ['$http',
    function($http){
        var hosturl = "http://localhost:8080/BlitzRisk/api/";
        var token = null;

        return {
            login: function(calback, username, password){
                var req = {
                    method: 'GET',
                    url: hosturl+'token/'+ username,
                    headers: {
                        'password': password
                    }
                };
               return  $http.get(hosturl+'token/'+ username, { headers: {'password': password}});
            },
            getToken: function(){
                return "gunther";
            }
    }}]);