/**
 * Created by vman on 7/02/2015.
 */

//forwards everything to a separate service

/* message: the currently typed message in the textbox */
/* messages: array contains all received messages */
/* max: the maximum allowed chars in a message */

angular.module('blitzriskControllers').controller("ChatController", ['$scope', '$log',  'ChatService', 'LoginService', 'GameService',
    function ($scope,$log, ChatService, LoginService, GameService) {
    $scope.showChat = false;
    $scope.messages = [];
    $scope.currentGame = {};
    $scope.currentUsername = "";

    $scope.max = 140;
    var players;

    function getPlayer() {
        var username = LoginService.getUserName();
        $log.info("Username: " + username);
        $log.info("Players: " + players);
        var length = players.length;
        for (var i = 0; i < length; i++) {

            if (players[i].username == username) {
                return players[i];
            }
        }
    }

    GameService.getCurrentGame()
        .then(function (payload) {
            $scope.currentGame = payload.data;
            players = $scope.currentGame.players;
            ChatService.gameId = $scope.currentGame.id;
            addListener();
        });


    //sending a message
    $scope.addMessage = function () { //called when form is submitted
        if ($scope.newMessage.trim() !== "") {

            //     ChatService.send($scope.newMessage, ChatService.CHAT_BROKER, getPlayer()); //forwards the message to the service
            ChatService.send($scope.newMessage, getPlayer()); //forwards the message to the service

        }
        $scope.newMessage = ""; //empty the field, reset the message model
    };

    //receiving a message
    function addListener() {
        ChatService.currentPlayerName = getPlayer().username;
        ChatService.receive().then(null, null, function (message) { //runs a deferred. Each time a message is received,
            //updates the progress part of the directive


               $scope.messages.push(message);

        })
    }


    $scope.showChatScreen = function () {
        $scope.showChat = !$scope.showChat;
    };


}
]);