/**
 * Created by ermahgerd on 19/02/2015.
 */
'use strict';

angular.module('blitzriskServices').factory('GameService', ['$http', '$q', 'LoginService',
    function ($http, $q, LoginService) {
        var securityToken = null;

        function resolveSecurityToken() {
            securityToken = LoginService.getToken();
        }

        resolveSecurityToken();
        return {
            getTerritoryLayout: function () {
                //    return $http.get('http://localhost:8080/BlitzRisk/api/territoryLayout');
              //  if (securityToken == null) resolveSecurityToken();

                return $http.get('api/territoryLayout', {headers: {'X-Auth-Token': securityToken}});
            },
            getGamesList: function () {
                var deferred = $q.defer();  //maak promise
             //  if (securityToken == null) resolveSecurityToken();

                var username = LoginService.getUserName();

                $http.get('api/user/' + username + '/players', {headers: {'X-Auth-Token': securityToken}})
                    .success(function (data) {
                        deferred.resolve(data);
                    });

                return deferred.promise;

            },
            createNewGame: function () {
              // if (securityToken == null) resolveSecurityToken();

                return $http.get('api/createGame', {headers: {'X-Auth-Token': securityToken}});

            },
            acceptGame: function(gameId) {
          //     if (securityToken == null) resolveSecurityToken();

                return $http.put('api/acceptGame/' + gameId, null, {headers: {'X-Auth-Token': securityToken}});
            }
        }
    }]);


