/**
 * service responsible for turn and move calls.
 */
'use strict';

angular.module('blitzriskServices').factory('TurnService', ['$http', '$q', 'LoginService', '$log', '$interval',
    function ($http, $q, LoginService, $log, $interval) {
        var turnId = null;
        var turnStatus = "WAITING";
        var playerId = null;
        var statusPoller = null;

        function polling(){
            statusPoller = $interval(function () {
                $log.log("start status check");
                if(playerId != null)
                    checkStatus();
                else
                $log.log("No playerId available in the turnService.");
            }, 5000);
        }

        polling();

        function checkStatus() {
            var defer = $q.defer();
            $http.get('api/player/' + playerId + '/getPlayerStatus', {headers: {'X-Auth-Token': LoginService.getToken()}})
                .success(function (data) {
                    if (data != turnStatus)
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
            startPolling: function(){
                if(statusPoller != null){
                    polling();
                }
            },
            createTurn: function(){
                var defer = $q.defer();
                $http.get('api/player/' + playerId + '/createTurn', {headers: {'X-Auth-Token': LoginService.getToken()}})
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
                $http.get('api/player/' + playerId + '/numberOfReinforcements', {headers: {'X-Auth-Token': LoginService.getToken()}})
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
            setPlayerId: function (id) {
                playerId = id;
            },
            setTurnId: function(turn){
                turnId = turn;
            },
            getTurnId: function(){
                return turnId;
            },
            getTurnStatus: function() {
                return turnStatus;
            },
            clean: function(){
                $interval.cancel(statusPoller);
                statusPoller == null;
            },
            sendMoves: function(moves){
               if(turnStatus === 'REINFORCE'){
                    return $http.post('api/player/' + playerId + '/reinforce', moves, {headers: {'X-Auth-Token': LoginService.getToken()}});
                }else if(turnStatus === 'ATTACK'){
                    return $http.post('api/player/' + playerId + '/attack' , moves, {headers: {'X-Auth-Token': LoginService.getToken()}});
                }else if(turnStatus === 'MOVE'){
                    return $http.post('api/player/' + playerId + '/moveUnits' , moves, {headers: {'X-Auth-Token': LoginService.getToken()}});
                }
            }
        }
    }]);
