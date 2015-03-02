/**
 * Created by vman on 7/02/2015.
 */
angular.module('blitzriskServices').service("ChatService", ['$q', '$timeout', 'GameService', function ($q, $timeout, GameService) {
    var service = {},
        listener = $q.defer(),
        socket = {
            client: null,
            stomp: null
        },
        messageIds = [];

    service.gameId = 0;

    service.RECONNECT_TIMEOUT = 30000; //30 seconds
    service.SOCKET_URL = "/BlitzRisk/api/chat";
    service.CHAT_TOPIC = "/topic/message";
    service.CHAT_BROKER = "/blitzrisk/chat";

    /* PUBLIC FUNCTIONS */
    service.receive = function () {
        service.subscription.unsubscribe(); // stops listening to previous chatroom
        startListener();  //starts listening again
        return listener.promise; //returns the deferred used to send messages at
    };

    service.send = function (message, player) {
        var id = Math.floor(Math.random() * 1000000);
        socket.stomp.send(
            '/blitzrisk/notify/' + service.gameId,    //send to "/app/chat"
            {priority: 9},
            JSON.stringify(
                { //stringified JSON with ID, so that it can be used by the getMessage() function
                    // to check if the message was added by this client or by another client.
                    message: message,
                    id: id,
                    username: player.username,
                    color: player.color
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

    var getMessage = function (data) { //translates the websocket data body (payload) to the model required by the controller
        var message = JSON.parse(data),//parse the JSON string to an object
            out = {};
        out.message = message.message;
        out.time = new Date(message.time); //sets the time as a Date object
        out.username = message.username;
        out.color = message.color;

        if (_.contains(messageIds, message.id)) { //If the message ID is listed in the messageIds array,
            // then it means the message originated from this client, so it will set the self property to true.
            out.self = true;
            messageIds = _.remove(messageIds, message.id); //remove id so it is available again
        }
        return out;
    };

    var startListener = function () { //listens to /topic/message topic on which all messages will be received.
        service.subscription = socket.stomp.subscribe("/topic/push/" + service.gameId, function (data) { // /topic/message
            listener.notify(getMessage(data.body)); //sends data to the deferred which will be used by the controllers
        });
        //   service.send("aah");
    };

    var initialize = function () {
        socket.client = new SockJS(service.SOCKET_URL); //setup SocksJS websocket client "/spring-ng-chat2/chat"
        socket.stomp = Stomp.over(socket.client); //SocksJS websocket client will be used for the Stomp.js websocket client,
        //allows JSON + subscribe to topic + publish topic
        socket.stomp.connect({}, startListener); //when the client is connected to the websocket server, the startListener
        //is called
        socket.stomp.onclose = reconnect; //if connection to server is lost -> reconnect
    };

    initialize(); //happens exactly once because an AngularJS service is a Singleton. Each time the same instance is
    //returned.
    return service;
}]);