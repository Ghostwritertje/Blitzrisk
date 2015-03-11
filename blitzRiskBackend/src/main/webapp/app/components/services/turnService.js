/**
 * service responsible for turn and move calls.
 */
'use strict';

angular.module('blitzriskServices').factory('TurnService', ['$http', '$q', 'LoginService', '$log', '$interval',
    function ($http, $q, LoginService, $log, $interval) {
        var currentGameId = null;
        var turnId = null;
        var turnStatus = "WAITING";
        var playerId = null;


        var statusPoller = $interval(function () {
            $log.log("start status check");
            if(playerId != null)
                checkStatus();
            else
                $log.log("No playerId available in the turnService.");
        }, 5000);

        function checkStatus() {
            var defer = $q.defer();
            $http.get('api/getPlayerStatus', {headers: {'X-Auth-Token': LoginService.getToken(), 'playerId': playerId}})
                .success(function (data) {
                    if (data != turnStatus && turnStatus != "MOVE")
                        turnStatus = data;
                    defer.resolve(data);
                    $log.log("status check");
                    $log.log(turnStatus);
                })
                .error(function (data, status) {
                    defer.reject(status);
                });
            return defer.promise;
        }

        return {
            createTurn: function(){
                var defer = $q.defer();
                $http.get('api/createTurn', {headers: {'X-Auth-Token': LoginService.getToken(), 'playerId': playerId}})
                    .success(function (data) {
                        defer.resolve(data);
                    })
                    .error(function (data, status) {
                        defer.reject(status);
                        $log.log("Failed to create a turn.");
                        $log.log(status);
                    });
                return defer.promise;
            },
            getNumberOfReinforcments: function () {
                var defer = $q.defer();
                $http.get('api/numberOfReinforcements', {headers: {'X-Auth-Token': LoginService.getToken(), 'playerId': playerId}})
                    .success(function (data) {
                        defer.resolve(data);
                    })
                    .error(function (data, status) {
                        defer.reject(status);
                        $log.log("Failed to retrieve number of reinforcements.");
                        $log.log(status);
                    });
                return defer.promise;
            },
            getPlayerStatus: function (playerId) {
                var defer = $q.defer();
                $http.get('api/getPlayerStatus', {
                    headers: {
                        'X-Auth-Token': LoginService.getToken(),
                        'playerId': playerId
                    }
                })
                    .success(function (data) {
                        defer.resolve(data);
                        $log.log(data);
                    })
                    .error(function (data, status) {
                        defer.reject(status);
                    });
                return defer.promise;
            },
            setPlayerId: function (id) {
                playerId = id;
            },
            setTurnStatus: function(statusturn){
                turnStatus = statusturn;
            },
            getTurnStatus: function() {
                return turnStatus;
            },
            clean: function(){
                $interval.cancel(statusPoller);
            },
            sendMoves: function(moves){
               if(turnStatus === 'REINFORCE'){
                    return $http.post('api/reinforce', moves, {headers: {'X-Auth-Token': LoginService.getToken(), 'playerId': playerId}});
                }else if(turnStatus === 'ATTACK'){
                    return $http.post('api/attack' , moves, {headers: {'X-Auth-Token': LoginService.getToken(), 'playerId': playerId}});
                }else if(turnStatus === 'MOVE'){
                    return $http.post('api/moveUnits' , moves, {headers: {'X-Auth-Token': LoginService.getToken(), 'playerId': playerId}});
                }
            }
        }
    }]);
