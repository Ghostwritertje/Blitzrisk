/**
 * Created by ermahgerd on 19/02/2015.
 */
'use strict';

angular.module('blitzriskServices').factory('GameService', ['$http', 'LoginService',
    function ($http, LoginService) {

        return {
            getTerritoryLayout: function () {
                //    return $http.get('http://localhost:8080/BlitzRisk/api/territoryLayout');
                var token = LoginService.getToken();
                return $http.get('http://localhost:8080/BlitzRisk/api/territoryLayout', {headers: {'X-Auth-Token': token}});
            }
        }
    }]);


