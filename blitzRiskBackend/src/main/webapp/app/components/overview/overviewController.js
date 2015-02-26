/**
 * Created by jorandeboever on 18/02/15.
 */
'use strict';
angular.module('blitzriskControllers').controller('OverviewController', ['$scope', '$http', '$location', 'LoginService', 'GameService',
    function ($scope, $http, $location, LoginService, GameService) {
        $scope.friends = [{"name": "Dummy1"}, {"name": "Dummy2"}, {"name": "Dummy3"}];

        $scope.recentlyPlayed = [{"name": "Dummy145"}, {"name": "Dummy234"}, {"name": "Dummy367"}];

        $scope.players =
            [{"color": 0, "invitationStatus": "ACCEPTED", "id": 1, "gameId": 12},
                {"color": 0, "invitationStatus": "PENDING", "id": 2, "gameId": 34},
                {"color": 0, "invitationStatus": "PENDING", "id": 4, "gameId": 32},
                {"color": 0, "invitationStatus": "PENDING", "id": 5, "gameId": 92}];

        loadGames();

        $scope.go = function (path) {
            $location.path(path);
        };


        $scope.createNewGame = function () {
            var promise = GameService.createNewGame();
            promise.then(function () {
                loadGames();
            })
        };

        $scope.acceptGame = function (gameId){
            var promise = GameService.acceptGame(gameId);
            promise.then(function () {
                loadGames();
            })
        };

        function loadGames() {
            var promise = GameService.getGamesList();
            promise.then(function (data) {
                $scope.players = data;
            });
        }

    }
]);