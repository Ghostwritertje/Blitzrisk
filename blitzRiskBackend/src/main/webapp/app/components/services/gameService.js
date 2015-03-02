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

        return {
            loadTerritoryLayout: function(){
                var defer = $q.defer();
                $http.get('api/territoryLayout', {headers: {'X-Auth-Token': LoginService.getToken()}}).success(function(data){ defer.resolve(data); territoryLayout = data;}).error(function(data, status){defer.reject(status)});
                return defer.promise;
            },
            getTerritoryLayout: function () {
                var defer = $q.defer();
                if(territoryLayout == null){
                    loadTerritoryLayout().then(function(territoryLayout){defer.resolve(territoryLayout);});
                }else{
                    defer.resolve(territoryLayout);
                }
                return defer.promise;
            },
            getGamesList: function () {
                var deferred = $q.defer();  //maak promise

                var username = LoginService.getUserName();

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
                currentGameId = gameId;
            },
            getCurrentGame: function () {
                return $http.get('api/game/' + currentGameId, {headers: {'X-Auth-Token': LoginService.getToken()}})
            },
            getCurrentGameId: function () {
                return currentGameId;
            },
            invitePlayerToGame: function (gameId, username) {
                return $http.post('api/game/' + gameId + '/invite/' + username, null, {headers: {'X-Auth-Token': LoginService.getToken()}});

            },
            invitRandomPlayerToGame : function(gameId) {
                return $http.post('api/game/' + gameId + '/invite-random', null, {headers: {'X-Auth-Token': LoginService.getToken()}});
            },
            getCurrentPlayer: function () {
                var deferred = $q.defer();

                $http.get('api/game/' + currentGameId, {headers: {'X-Auth-Token': LoginService.getToken()}})
                    .success(function (data) {
                        var temp = {"playerTurn":0,"started":true,"players":[{"id":3,"color":1,"invitationStatus":"ACCEPTED","username":"Joran"},{"id":2,"color":0,"invitationStatus":"ACCEPTED","username":"Gunther"}],"turns":[],"territories":[{"numberOfUnits":1,"key":34,"playerId":3,"id":21},{"numberOfUnits":1,"key":38,"playerId":3,"id":39},{"numberOfUnits":1,"key":24,"playerId":3,"id":19},{"numberOfUnits":1,"key":41,"playerId":3,"id":37},{"numberOfUnits":1,"key":6,"playerId":2,"id":36},{"numberOfUnits":1,"key":10,"playerId":2,"id":4},{"numberOfUnits":1,"key":3,"playerId":3,"id":9},{"numberOfUnits":1,"key":23,"playerId":2,"id":34},{"numberOfUnits":1,"key":15,"playerId":3,"id":33},{"numberOfUnits":1,"key":17,"playerId":2,"id":40},{"numberOfUnits":1,"key":26,"playerId":2,"id":12},{"numberOfUnits":1,"key":2,"playerId":3,"id":11},{"numberOfUnits":1,"key":33,"playerId":2,"id":18},{"numberOfUnits":1,"key":16,"playerId":2,"id":24},{"numberOfUnits":1,"key":7,"playerId":2,"id":6},{"numberOfUnits":1,"key":32,"playerId":3,"id":15},{"numberOfUnits":1,"key":35,"playerId":2,"id":2},{"numberOfUnits":1,"key":36,"playerId":3,"id":25},{"numberOfUnits":1,"key":5,"playerId":2,"id":16},{"numberOfUnits":1,"key":14,"playerId":3,"id":17},{"numberOfUnits":1,"key":12,"playerId":3,"id":13},{"numberOfUnits":1,"key":28,"playerId":2,"id":10},{"numberOfUnits":1,"key":8,"playerId":3,"id":41},{"numberOfUnits":1,"key":9,"playerId":2,"id":8},{"numberOfUnits":1,"key":39,"playerId":3,"id":29},{"numberOfUnits":1,"key":27,"playerId":3,"id":5},{"numberOfUnits":1,"key":42,"playerId":2,"id":32},{"numberOfUnits":1,"key":40,"playerId":3,"id":23},{"numberOfUnits":1,"key":4,"playerId":3,"id":35},{"numberOfUnits":1,"key":21,"playerId":2,"id":42},{"numberOfUnits":1,"key":19,"playerId":3,"id":27},{"numberOfUnits":1,"key":18,"playerId":3,"id":31},{"numberOfUnits":1,"key":29,"playerId":2,"id":30},{"numberOfUnits":1,"key":20,"playerId":2,"id":14},{"numberOfUnits":1,"key":22,"playerId":2,"id":28},{"numberOfUnits":1,"key":31,"playerId":2,"id":38},{"numberOfUnits":1,"key":25,"playerId":2,"id":22},{"numberOfUnits":1,"key":11,"playerId":3,"id":1},{"numberOfUnits":1,"key":13,"playerId":2,"id":20},{"numberOfUnits":1,"key":37,"playerId":3,"id":7},{"numberOfUnits":1,"key":1,"playerId":2,"id":26},{"numberOfUnits":1,"key":30,"playerId":3,"id":3}],"id":2};
                        for(var player in temp.players){
                            if(player.username == LoginService.getUserName()){
                                deferred.resole();
                            }
                        }
                    });

                return deferred.promise;
            }
        }
    }]);


