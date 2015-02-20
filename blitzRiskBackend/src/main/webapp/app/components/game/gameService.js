/**
 * Created by ermahgerd on 19/02/2015.
 */
'use strict';

angular.module('blitzriskServices').factory('GameService', ['$http', '$q',
    function ($http, $q) {
        var territoryLayout;

        return {
            initTerritoryLayout: function () {
                var territoryLayoutDeffer = $q.defer();
                $http.get('http://localhost:8080/BlitzRisk/api/territoryLayout').
                    success(function(data){territoryLayout = data; territoryLayoutDeffer.resolve(data);}).
                    error(function (data) {territoryLayoutDeffer.reject(data);});
                return territoryLayoutDeffer.promise;
            },
            getTerritoryLayout: function(){
                return territoryLayout;
            }
        }
    }]);


