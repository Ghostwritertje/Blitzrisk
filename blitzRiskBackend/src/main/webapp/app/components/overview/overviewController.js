/**
 * Created by jorandeboever on 18/02/15.
 */
'use strict';
angular.module('blitzriskControllers').controller('OverviewController', ['$scope', '$http', '$location', 'LoginService', 'GameService',
    function ($scope, $http, $location, LoginService, GameService) {
        $scope.friends = [{"name": "Dummy1"}, {"name": "Dummy2"}, {"name": "Dummy3"}];

        $scope.recentlyPlayed = [{"name": "Dummy145"}, {"name": "Dummy234"}, {"name": "Dummy367"}];

        $scope.players = "";


        $scope.games = [{"playerTurn":0,"started":false,"players":[{"id":1,"color":0,"invitationStatus":"PENDING","username":"Joran"}, {"id":2,"color":2,"invitationStatus":"PENDING","username":"Mark"}],"turns":[],"territories":[],"id":1},{"playerTurn":0,"started":false,"players":[{"id":1,"color":0,"invitationStatus":"PENDING","username":"Joran"}, {"id":2,"color":2,"invitationStatus":"PENDING","username":"Mark"}],"turns":[],"territories":[],"id":2},{"playerTurn":0,"started":false,"players":[{"id":1,"color":0,"invitationStatus":"PENDING","username":"Joran"}, {"id":2,"color":2,"invitationStatus":"PENDING","username":"Mark"}],"turns":[],"territories":[],"id":3}, {"playerTurn":0,"started":false,"players":[{"id":1,"color":0,"invitationStatus":"PENDING","username":"Joran"}, {"id":2,"color":2,"invitationStatus":"PENDING","username":"Mark"}],"turns":[],"territories":[],"id":4}];


        loadGames();

        $scope.go = function (path) {
            $location.path(path);
        };


        $scope.createNewGame = function () {
            var promise = GameService.createNewGame();
            promise.then(function (payload) {
                GameService.setCurrentGame(payload.data);
                $location.path('/gamelobby')
            })
        };

        $scope.addPlayer = function () {
            GameService.invitePlayerToGame($scope.newUser).then(function(){
                loadGames();
            })
        };
        $scope.addRandomPlayer = function () {
            GameService.invitRandomPlayerToGame().then(function (){
                loadGames();
            })
        };

        $scope.selectGame = function(gameId){
            $scope.selectedGameId = gameId;

        };

        $scope.acceptGame = function (playerId) {
            var promise = GameService.acceptGame(playerId);
            promise.then(function () {
                loadGames();
            })
        };

        $scope.enterLobby = function (gameId) {
            GameService.setCurrentGame(gameId);
            $location.path('/gamelobby');


        };

        $scope.enterGame = function (gameId) {
            GameService.setCurrentGame(gameId);
            $location.path('/game');


        };

        function loadGames() {
            var promise = GameService.getGamesList();
            promise.then(function (data) {
                $scope.games = data;
            });
        }


    }
]);
