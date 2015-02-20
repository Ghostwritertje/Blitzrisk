/**
 * Created by ermahgerd on 19/02/2015.
 */
'use strict';

angular.module('blitzriskServices').factory('GameService', ['$http',
    function ($http) {

        return {
            getTerritoryLayout: function () {
                return $http.get('http://localhost:8080/BlitzRisk/api/territoryLayout');
            }
        }
    }]);


