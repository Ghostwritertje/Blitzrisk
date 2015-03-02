/**
 * Created by vman on 7/02/2015.
 */

//forwards everything to a separate service

/* message: the currently typed message in the textbox */
/* messages: array contains all received messages */
/* max: the maximum allowed chars in a message */

angular.module('blitzriskControllers').controller("ChatController", [ '$scope', 'ChatService', function ($scope, ChatService) {
    $scope.showChat = false;
   // $scope.messages = [{"message": "hallo", "self": true}, {"message": "jow", "self": false}, {"message": "how are u?", "self": true}, {"message": "Great, how are you?", "self": false}, {"message": "Really good, thanks!", "self": true}];
     $scope.messages = [];

    $scope.newMessage = "";
    $scope.max = 140;

    //sending a message
    $scope.addMessage = function () { //called when form is submitted
        ChatService.send($scope.newMessage); //forwards the message to the service
        $scope.newMessage = ""; //empty the field, reset the message model
    };

    //receiving a message
    ChatService.receive().then(null, null, function (message) { //runs a deferred. Each time a message is received,
        //updates the progress part of the directive
        $scope.messages.push(message);
    });

    $scope.showChatScreen = function () {
        $scope.showChat = !$scope.showChat;
    };

}
]);