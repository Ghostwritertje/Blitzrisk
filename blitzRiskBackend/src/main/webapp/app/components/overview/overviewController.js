/**
 * controller controlling the overview page
 */
'use strict';
angular.module('blitzriskControllers').controller('OverviewController', ['$scope', '$http', '$location', '$interval', 'LoginService', 'GameService', 'FriendService',
    function ($scope, $http, $location, $interval, LoginService, GameService, FriendService) {
        $scope.recentlyPlayed = [{"name": "Dummy145"}, {"name": "Dummy234"}, {"name": "Dummy367"}];
        $scope.selectedGameId = "";

        $scope.newUsers = [];

        $scope.games = [];

        loadGames();

        $scope.interval = $interval(loadGames, 5000); //reloads data every 5 seconds

        $scope.go = function (path) {
            $location.path(path);

        };

        $scope.$on('$destroy', function () {
            $interval.cancel($scope.interval);
        });


        $scope.createNewGame = function () {
            var promise = GameService.createNewGame();
            promise.then(function (payload) {
                $scope.selectedGameId = payload.data;
                loadGames();
            })
        };

        $scope.addPlayer = function (gameId) {
            GameService.invitePlayerToGame(gameId, $scope.newUsers[gameId]).then(function () {
                loadGames();
                $scope.newUsers[gameId] = "";
            })
        };
        $scope.addRandomPlayer = function (gameId) {
            GameService.invitRandomPlayerToGame(gameId).then(function () {
                loadGames();
            })
        };

        $scope.selectGame = function (gameId) {
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
            promise.then(function (payload) {
                if ($scope.games != payload)  $scope.games = payload;
            });
        }

        /*  Friends  */
        $scope.friends = [];
        $scope.friendRequests = [];
        $scope.newFriend = "";
        $scope.addFriendError = false;
        $scope.recentlyPlayed = [];
        reloadFriends();

        $scope.addFriend = function () {
            FriendService.addFriend($scope.newFriend).then(
                function () {
                    $scope.newFriend = "";
                    reloadFriends();
                },function(){
                    $scope.newFriend = "";
                    $scope.addFriendError = true;

                }
            )
        };

        $scope.acceptFriend = function (friendName) {
            FriendService.acceptFriendRequest(friendName).then(
                function () {
                    reloadFriends();
                }
            )
        };

        function reloadFriends() {
            FriendService.getFriends()
                .then(function (payload) {
                    $scope.friends = payload.data;
                }
            )

            ;
            FriendService.getFriendRequests()
                .then(function (payload) {
                    $scope.friendRequests = payload.data;
                }
            );
            FriendService.getRecentlyPlayed()
                .then(function (payload) {
                    $scope.recentlyPlayed = payload.data;

                }
            );
        }
    }
]);
