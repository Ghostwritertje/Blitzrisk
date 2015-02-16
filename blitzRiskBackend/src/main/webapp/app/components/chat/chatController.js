/**
 * Created by vman on 7/02/2015.
 */

//forwards everything to a separate service

/* message: the currently typed message in the textbox */
/* messages: array contains all received messages */
/* max: the maximum allowed chars in a message */

angular.module('blitzriskControllers').controller("chatCtrl", function ($scope, ChatService) {
    $scope.messages = [];
    $scope.message = "";
    $scope.max = 140;

    //sending a message
    $scope.addMessage = function () { //called when form is submitted
        ChatService.send($scope.message); //forwards the message to the service
        $scope.message = ""; //empty the field, reset the message model
    };

    //receiving a message
    ChatService.receive().then(null, null, function (message) { //runs a deferred. Each time a message is received,
        //updates the progress part of the directive
        $scope.messages.push(message);
    });
});