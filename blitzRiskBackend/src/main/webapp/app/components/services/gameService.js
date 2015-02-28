/**
 * Created by ermahgerd on 19/02/2015.
 */
'use strict';

angular.module('blitzriskServices').factory('GameService', ['$http', '$q', 'LoginService',
    function ($http, $q, LoginService) {
        var securityToken = null;
        var currentGame = null;
        var currentGameId = null;
        var territoryLayout = null;

        function loadTerritoryLayout(){
            return $http.get('api/territoryLayout', {headers: {'X-Auth-Token': LoginService.getToken()}}).success(function(data){territoryLayout = data;});
        }
        loadTerritoryLayout();

        return {
            getTerritoryLayout: function () {
                var defer = $q.defer();
                if(territoryLayout == null){
                    loadTerritoryLayout().then(function(territoryLayout){defer.resolve(territoryLayout);});
                    //alert("t1");
                }else{
                    defer.resolve(territoryLayout);
                    //alert("t2");
                    //alert(territoryLayout[2].territoryKey);
                }
                return defer.promise;

                //return $http.get('api/territoryLayout', {headers: {'X-Auth-Token': LoginService.getToken()}}).success(function(data){territoryLayout = data.data});
            },
            getGamesList: function () {
                var deferred = $q.defer();  //maak promise

                var username = LoginService.getUserName();

              /*  $http.get('api/user/' + username + '/players', {headers: {'X-Auth-Token': LoginService.getToken()}})
                    .success(function (data) {
                        deferred.resolve(data);
                    });
*/
                $http.get('api/user/' + username + '/games', {headers: {'X-Auth-Token': LoginService.getToken()}})
                    .success(function (data) {
                        deferred.resolve(data);
                    });
                return deferred.promise;

            },
            createNewGame: function () {

                return $http.get('api/createGame', {headers: {'X-Auth-Token': LoginService.getToken()}});

            },
            acceptGame: function (playerId) {

                return $http.put('api/acceptGame/' + playerId, null, {headers: {'X-Auth-Token': LoginService.getToken()}});
            },
            setCurrentGame: function (gameId) {
                /*var deferred = $q.defer();
                 $http.get('api/game/' + gameId, {headers: {'X-Auth-Token': securityToken}}).success(function (data){
                 currentGame = data;
                 deferred.resolve();
                 });
                 return deferred.promise;*/
                currentGameId = gameId;
            },
            getCurrentGame: function () {
                return $http.get('api/game/' + currentGameId, {headers: {'X-Auth-Token': LoginService.getToken()}})
            },
            invitePlayerToGame: function (username) {
                return $http.post('api/game/' + currentGameId + '/invite/' + username, null, {headers: {'X-Auth-Token': LoginService.getToken()}});

            },
            invitRandomPlayerToGame : function() {
                return $http.post('api/game/' + currentGameId + '/invite-random', null, {headers: {'X-Auth-Token': LoginService.getToken()}});
            }
        }
    }]);


