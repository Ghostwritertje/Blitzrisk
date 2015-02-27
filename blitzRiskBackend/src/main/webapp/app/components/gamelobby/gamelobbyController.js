/**
 * Created by jorandeboever on 26/02/15.
 */
angular.module('blitzriskControllers').controller('GamelobbyController', ['$scope', '$http', '$location', 'LoginService', 'GameService',
    function ($scope, $http, $location, LoginService, GameService) {
        //   $scope.game = {"playerTurn":0,"players":[{"id":9,"color":0}, {"id":4,"color":0}],"turns":[],"territories":[],"id":7};
        $scope.newUser = "";


        $scope.game = "";
        loadUsers();

        function loadUsers() {
            var promise = GameService.getCurrentGame();
            promise.then(function (payload) {
                $scope.game = payload.data;
                if($scope.game.started) $location.path('/game');
            })
        }


        $scope.addPlayer = function () {
            GameService.invitePlayerToGame($scope.newUser).then(function(){
                loadUsers();
            })
        };
        $scope.addRandomPlayer = function () {
            GameService.invitRandomPlayerToGame().then(function (){
                loadUsers();
            })
        }


    }
]);