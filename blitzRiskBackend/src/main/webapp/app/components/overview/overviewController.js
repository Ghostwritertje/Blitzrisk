/**
 * Created by jorandeboever on 18/02/15.
 */
'use strict';
angular.module('blitzriskControllers').controller('OverviewController', ['$scope', '$http', '$location', 'LoginService', 'GameService',
    function ($scope, $http, $location, LoginService, GameService) {
        $scope.friends = [{"name": "Dummy1"}, {"name": "Dummy2"}, {"name": "Dummy3"}];

        $scope.recentlyPlayed = [{"name": "Dummy145"}, {"name": "Dummy234"}, {"name": "Dummy367"}];

        $scope.players = "";

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

        function loadGames() {
            var promise = GameService.getGamesList();
            promise.then(function (data) {
                $scope.players = data;
            });
        }


    }
]);
