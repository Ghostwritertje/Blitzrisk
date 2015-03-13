/**
 * service responsible for chat calls
 */
angular.module('blitzriskServices').service("ChatService", ['$q', '$timeout', '$log', 'GameService', function ($q, $timeout, $log, GameService) {
    var service = {},
        listener = $q.defer(),
        socket = {
            client: null,
            stomp: null
        },
        messageIds = [];

    service.gameId = 0;
    service.currentPlayerName = "";

    service.RECONNECT_TIMEOUT = 30000; //30 seconds
    service.SOCKET_URL = "/BlitzRisk/api/chat";
    service.CHAT_TOPIC = "/game/channel/";
    service.CHAT_BROKER = "/game/sendmessage/";

    /* PUBLIC FUNCTIONS */
    service.receive = function () {
        if (service.subscription != null) service.subscription.unsubscribe(); // stops listening to previous chatroom
        startListener();  //starts listening again
        return listener.promise; //returns the deferred used to send messages at
    };

    service.send = function (message, player) {
        var id = Math.floor(Math.random() * 1000000);
        $log.log("Player " + player.username + " sends message \'" + message);
        socket.stomp.send(service.CHAT_BROKER + service.gameId,    //send to "/sendmessage/XX"
            {priority: 9},
            JSON.stringify(
                { //stringified JSON with ID, so that it can be used by the getMessage() function
                    // to check if the message was added by this client or by another client.
                    message: message,
                    id: id,
                    player: player
                }
            )
        );
        messageIds.push(id); //ID is added to the messageIds array
    };

    /* PRIVATE FUNCTIONS */
    var reconnect = function () { //reconnect after 30 seconds
        $timeout(function () {
            initialize();
        }, this.RECONNECT_TIMEOUT);
    };

    var getMessage = function (newMessage) { //translates the websocket data body (payload) to the model required by the controller
        var out = {};
        var message = {
            "message": "hi",
            "id": 700831,
            "player": {"id": 1, "color": 0, "invitationStatus": "ACCEPTED", "username": "speler1"},
            "time": 1425406605763
        };
        $log.log("Message: " + message);
        message = newMessage;
        out.message = message.message;


        out.time = new Date(message.time); //sets the time as a Date object
        out.username = message.player.username;
        out.color = message.player.color;

        if (message.player.username === service.currentPlayerName) { //If the message ID is listed in the messageIds array, _.contains(messageIds, message.id)
            // then it means the message originated from this client, so it will set the self property to true.
            out.self = true;
          //  messageIds = _.remove(messageIds, message.id); //remove id so it is available again
        }
        return out;

    };

    var startListener = function () { //listens to /topic/message topic on which all messages will be received.
        service.subscription = socket.stomp.subscribe(service.CHAT_TOPIC + service.gameId, function (data) { // /channel/
            var messageBox = JSON.parse(data.body);//parse the JSON string to an object
            if (messageBox.constructor === Array) {
                for(var i = 0; i < messageBox.length; i++) {
                    var obj = messageBox[i];
                    listener.notify(getMessage(obj)); //sends data to the deferred which will be used by the controllers

                }


            }
            else {
                listener.notify(getMessage(messageBox)); //sends data to the deferred which will be used by the controllers
            }
        });
      /*  service.subscription = socket.stomp.subscribe("/channel/" + service.gameId, function (data) { // /channel/
            var messageBox = JSON.parse(data.body);//parse the JSON string to an object
            if (messageBox.constructor === Array) {
                for(var i = 0; i < messageBox.length; i++) {
                    var obj = messageBox[i];
                    listener.notify(getMessage(obj)); //sends data to the deferred which will be used by the controllers

                }


            }
            else {
                listener.notify(getMessage(messageBox)); //sends data to the deferred which will be used by the controllers
            }
        });*/
        //   service.send("aah");
    };

    var initialize = function () {
        socket.client = new SockJS(service.SOCKET_URL); //setup SocksJS websocket client "/spring-ng-chat2/chat"
        socket.stomp = Stomp.over(socket.client); //SocksJS websocket client will be used for the Stomp.js websocket client,
        //allows JSON + subscribe to topic + publish topic
        socket.stomp.connect({}, null); //when the client is connected to the websocket server, the startListener
        //is called
        socket.stomp.onclose = reconnect; //if connection to server is lost -> reconnect
    };

    initialize(); //happens exactly once because an AngularJS service is a Singleton. Each time the same instance is
    //returned.
    return service;
}]);