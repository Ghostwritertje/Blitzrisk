'use strict';

angular.module('blitzrisk.services').factory('loginService', ['$http',
    function($http){
        var hosturl = "http://localhost:8080/BlitzRisk/api/login";
        var token= null;
        return {
            login: function(username, password){
               return  $http.post(hosturl,{name:username, password:password}).success(function(data){token = data.data;});
            },
            getToken: function(){
                return token;
            }
    }}]);