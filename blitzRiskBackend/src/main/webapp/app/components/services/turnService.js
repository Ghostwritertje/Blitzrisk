/**
 * Service responsible for backend calls that handle turns and getting information for turns.
 */
'use strict';

angular.module('blitzriskServices').factory('TurnService', ['$http', '$q', 'LoginService',
    function ($http, $q, LoginService) {
        var securityToken = null;
        var currentGame = null;
        var currentGameId = null;
        var territoryLayout = null;

        return {
            getNumberOfReinforcements: function(){
                var defer = $q.defer();
                $http.get('api/numberOfReinforcements', {headers: {'X-Auth-Token': LoginService.getToken()}}).success(function(data){ defer.resolve(data); territoryLayout = data;}).error(function(data, status){defer.reject(status)});
                return defer.promise;
            }
            /*,
            getNumberOfReinforcements: function () {
                var defer = $q.defer();
                if(territoryLayout == null){
                    loadTerritoryLayout().then(function(territoryLayout){defer.resolve(territoryLayout);});
                }else{
                    defer.resolve(territoryLayout);
                }
                return defer.promise;
            }*/
        }
    }]);


