/**
 * Created by ermahgerd on 19/02/2015.
 */
'use strict';

angular.module('blitzriskServices').factory('GameService', ['$http', '$q',
    function ($http, $q) {
        var hosturl = "http://localhost:8080/BlitzRisk/api/login";
        var token = null;
        var username;
        var password;

        return {
            getTerritoryLayout: function () {
                return $http.get('http://localhost:8080/BlitzRisk/api/territoryLayout');
            }
        }
    }]);


