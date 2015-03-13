'use strict';
angular.module('blitzriskControllers').controller('GameController', ['$scope',
    function ($scope) {
        $scope.players = null;

        $scope.turnStatus = "WAITING";

        $scope.reinforcePopupVisible = false;
        $scope.reinforceNumberMax = 0;
        $scope.reinforceValue = 0;

        $scope.attackPopupVisible = false;
        $scope.attackForceMax = 0;
        $scope.attackValue = 0;

        $scope.movePopupVisible = false;
        $scope.moveForceMax = 0;
        $scope.moveValue = 0;

        $scope.submitReinforcment = function(){
            $scope.reinforcePopupVisible = false;
            $scope.reinforce($scope.reinforceValue);
        };

        $scope.submitMove = function(){
            $scope.movePopupVisible = false;
            $scope.move($scope.moveValue);
        };

        $scope.commitAttacks= function(){
            $scope.commitAttack();
        };
    }
]).directive('riskmap', ["GameService", "LoginService", "TurnService", "$log", function (GameService, LoginService, TurnService, $log) {
    return {
        restrict: 'E',
        replace: true,
        template: "<object type='image/svg+xml' data='assets/img/riskMap.svg'></object>",
        link: function (scope, element, attrs) {
            var gameBoard = "";
            var players = null;
            var thisPlayer = null;
            var selectedHomeRegion = null;
            var selectedDestinationRegion = null;

            var numberOfUnassignedReinforcements = 0;
            var moves = [];


            scope.$watch(TurnService.getTurnStatus, function(newValue){
                $log.log("new turn status: " + newValue);
                scope.turnStatus = newValue;
                if(newValue == "REINFORCE"){
                    TurnService.createTurn().then(function(data){
                        TurnService.getNumberOfReinforcments().then(function(availableReinforcements){
                            numberOfUnassignedReinforcements = availableReinforcements;
                        });
                    });
                }
            });

            scope.$on('$destroy', function () {
                $log.log("destroy riskmap Directive.");
                TurnService.clean();
            });

            angular.element(document).ready(function () {
                loadGameBoard();
                loadTerritoryLayout();
            });

            scope.reinforce = function(numberOfUnits){
                if(numberOfUnassignedReinforcements >= numberOfUnits){
                    numberOfUnassignedReinforcements -= numberOfUnits;
                    changeTerritoryText(selectedHomeRegion, (parseInt(getNumberOfUnitsOnTerritory(selectedHomeRegion)) + parseInt(numberOfUnits)));
                    setNumberOfUnitsOnTerritory(selectedHomeRegion, (parseInt(getNumberOfUnitsOnTerritory(selectedHomeRegion)) + parseInt(numberOfUnits)));
                    addMove(selectedHomeRegion, selectedHomeRegion, numberOfUnits);
                }else{
                    $log.log("You are placing units you don't have.");
                }

                if(numberOfUnassignedReinforcements == 0){
                    sendMoves();
                }

                selectedHomeRegion = null;
            };

            function sendMoves(){
                TurnService.sendMoves(moves).then(function(calculatedMoves){
                    $log.log("calulated moves");
                    $log.log(calculatedMoves);
                    var length = calculatedMoves.data.length;
                    for(var i = 0; i < length; i++){
                        $log.log(calculatedMoves.data[i]);
                        updateCalculatedMoves(calculatedMoves.data[i]);
                    }
                    clearMoves();
                });
            }

            function updateCalculatedMoves(calculatedMove){
                if(calculatedMove.origin == calculatedMove.destination){
                    var territoryId = getTerritoryIdFromDBId(calculatedMove.origin);
                    changeTerritoryText(territoryId, parseInt(calculatedMove.originNrOfUnits));
                    setNumberOfUnitsOnTerritory(territoryId, parseInt(calculatedMove.originNrOfUnits));
                }else{
                    var homeTerrId = getTerritoryIdFromDBId(calculatedMove.origin);
                    var destTerrId = getTerritoryIdFromDBId(calculatedMove.destination);

                    changeTerritoryText(homeTerrId, parseInt(calculatedMove.originNrOfUnits));
                    setNumberOfUnitsOnTerritory(homeTerrId, parseInt(calculatedMove.originNrOfUnits));
                    changeTerritoryText(destTerrId, parseInt(calculatedMove.destinationNrOfUnits));
                    setNumberOfUnitsOnTerritory(destTerrId, parseInt(calculatedMove.destinationNrOfUnits));
                }
            }

            scope.move = function(numberOfUnits){
                var numberOfUnitsOnHomeRegion = parseInt(getNumberOfUnitsOnTerritory(selectedHomeRegion));
                var numberOfUnitsOnDestinationRegion = parseInt(getNumberOfUnitsOnTerritory(selectedDestinationRegion));
                if(numberOfUnits <= (numberOfUnitsOnHomeRegion - 1)){
                    setNumberOfUnitsOnTerritory(selectedHomeRegion, (numberOfUnitsOnHomeRegion - numberOfUnits));
                    changeTerritoryText(selectedHomeRegion, (numberOfUnitsOnHomeRegion - numberOfUnits));

                    if(TurnService.getTurnStatus() == "MOVE"){
                        setNumberOfUnitsOnTerritory(selectedDestinationRegion, (numberOfUnitsOnDestinationRegion + parseInt(numberOfUnits)));
                        changeTerritoryText(selectedDestinationRegion, (numberOfUnitsOnDestinationRegion + parseInt(numberOfUnits)));
                    }
                    addMove(selectedHomeRegion, selectedDestinationRegion, numberOfUnits);
                }
                hideArrows();
                selectedHomeRegion = null;
                selectedDestinationRegion = null;
            };

            scope.commitAttack = function(){
                sendMoves();
            };

            /*scope.attack = function(numberOfUnits){
                var numberOfUnitsOnHomeRegion = parseInt(getNumberOfUnitsOnTerritory(selectedHomeRegion));
                var numberOfUnitsOnDestinationRegion = parseInt(getNumberOfUnitsOnTerritory(selectedDestinationRegion));
                if(numberOfUnits <= (numberOfUnitsOnHomeRegion - 1)){
                    setNumberOfUnitsOnTerritory(selectedHomeRegion, (numberOfUnitsOnHomeRegion - numberOfUnits));
                    setNumberOfUnitsOnTerritory(selectedDestinationRegion, (numberOfUnitsOnDestinationRegion + parseInt(numberOfUnits)));
                    changeTerritoryText(selectedHomeRegion, (numberOfUnitsOnHomeRegion - numberOfUnits));
                    changeTerritoryText(selectedDestinationRegion, (numberOfUnitsOnDestinationRegion + parseInt(numberOfUnits)));
                    addMove(selectedHomeRegion, selectedDestinationRegion, numberOfUnits);
                }
                scope.hideArrows();
                selectedHomeRegion = null;
                selectedDestinationRegion = null;
            };*/

            function clearMoves(){
                moves = [];
            }

            function addMove(originRegion, destinationRegion, numberOfUnits){
                moves.push({'turnId': 1, 'origin': getTerritoryDBId(originRegion), 'destination': getTerritoryDBId(destinationRegion), 'unitsToAttackOrReinforce': numberOfUnits});
                $log.log(moves);
            }

            function getTerritoryDBId(territoryId){
                var length = gameBoard.territories.length;
                for (var i = 0; i < length; i++) {
                    if(gameBoard.territories[i].key == territoryId){
                        return gameBoard.territories[i].id;
                    }
                }
            }

            function getTerritoryIdFromDBId(territoryDBId){
                var length = gameBoard.territories.length;
                for (var i = 0; i < length; i++) {
                    if(gameBoard.territories[i].id == territoryDBId){
                        return gameBoard.territories[i].key;
                    }
                }
            }

            function loadGameBoard() {
                GameService.getCurrentGame()
                    .then(function (payload) {
                        gameBoard = payload.data;
                        initializeBoard();
                    });
            }

            function loadTerritoryLayout() {
                GameService.loadTerritoryLayout()
                    .then(function (payload) {
                    });
            }

            function initializeBoard() {
                //players = gameBoard.players;
                scope.players = gameBoard.players;
                $log.log(scope.players);
                var length = scope.players.length;
                for(var i = 0; i < length; i++){
                    if(scope.players[i].username == LoginService.getUserName()){
                        thisPlayer = scope.players[i];
                        TurnService.setPlayerId(thisPlayer.id);
                        break;
                    }
                }

                length = gameBoard.territories.length;
                for (var i = 0; i < length; i++) {
                    var region = angular.element(element[0].getSVGDocument().getElementById(gameBoard.territories[i].key));
                    var colorClass = "player".concat(getPlayer(gameBoard.territories[i].playerId).color + 1).concat("color");
                    region.attr("class", colorClass);
                    changeTerritoryText(gameBoard.territories[i].key, gameBoard.territories[i].numberOfUnits);
                }
            }

            function getPlayer(playerId) {
                var length = scope.players.length;
                for (var i = 0; i < length; i++) {
                    if (scope.players[i].id == playerId) {
                        return scope.players[i];
                    }
                }
            }

            scope.selectRegion = function(territoryId){
                var isMyTerr = isMyTerritory(territoryId);
                var status = TurnService.getTurnStatus();
                if(status == "REINFORCE" && selectedHomeRegion == null && isMyTerr){
                    selectedHomeRegion = territoryId;
                    scope.reinforceNumberMax = numberOfUnassignedReinforcements;
                    scope.reinforceValue = 0;
                    scope.reinforcePopupVisible = true;
                    scope.$apply();
                }else if(status == "REINFORCE" && selectedHomeRegion != null){
                    selectedHomeRegion= null;
                    scope.reinforcePopupVisible = false;
                    scope.$apply();
                }else if(status == "MOVE" || status == "ATTACK" && selectedHomeRegion == null && isMyTerr){
                    selectedHomeRegion = territoryId;
                    changeTerritoryStyle(territoryId);
                    scope.moveForceMax = (parseInt(getNumberOfUnitsOnTerritory(territoryId)) - 1);
                    scope.moveValue = 0;
                }else if((status == "MOVE" && isMyTerr) || (status == "ATTACK" && !isMyTerr) && selectedHomeRegion != null  && selectedDestinationRegion == null){
                    selectedDestinationRegion = territoryId;
                    scope.movePopupVisible = true;
                    scope.$apply();
                }else if(status == "MOVE" || status == "ATTACK" && selectedHomeRegion != null && selectedDestinationRegion != null){
                    selectedHomeRegion = null;
                    selectedDestinationRegion = null;
                    scope.movePopupVisible = false;
                    hideArrows();
                    scope.$apply();
                }
            };

            function changeTerritoryStyle(territory) {
                hideArrows();
                if(isMyTerritory(territory)){
                    GameService.getTerritoryLayout().then(function (layout) {
                        showNeighbour(layout, territory);
                    });
                }
            }

            function showNeighbour(layout, territoryId) {
                var territoryLayout = layout;
                var neighbours = null;

                var length = territoryLayout.length;
                for (var i = 0; i < length; i++) {
                    if (territoryLayout[i].territoryKey == territoryId) {
                        neighbours = territoryLayout[i].neighbours;
                    }
                }

                length = neighbours.length;
                var homeTerritory = angular.element(element[0].getSVGDocument().getElementById(territoryId));
                var homeX = homeTerritory.attr("xcoord");
                var homeY = homeTerritory.attr("ycoord");
                var arrowId = 1;
                for (var i = 0; i < length; i++) {
                    var neighbourTerritory = angular.element(element[0].getSVGDocument().getElementById(neighbours[i]));
                    if(neighbours[i] == 1 && territoryId == 30){
                        drawArrow(arrowId, homeX, homeY, "1533.75", homeY, 1);
                        arrowId++;
                        drawArrow(arrowId, "0", neighbourTerritory.attr("ycoord"), neighbourTerritory.attr("xcoord"), neighbourTerritory.attr("ycoord"), 1);
                        arrowId++;
                    }else if(neighbours[i] == 30 && territoryId == 1){
                        drawArrow(arrowId, homeX, homeY, "0", homeY, 30);
                        arrowId++;
                        drawArrow(arrowId,  "1533.75",  neighbourTerritory.attr("ycoord"), neighbourTerritory.attr("xcoord"), neighbourTerritory.attr("ycoord"), 30);
                        arrowId++;
                    }else{
                        drawArrow(arrowId, homeX, homeY, neighbourTerritory.attr("xcoord"), neighbourTerritory.attr("ycoord"), neighbours[i]);
                        arrowId++;
                    }
                }
            }

            function drawArrow(arrowId, fromX, fromY, toX, toY, destinationTerritoryId){
                var arrowline = "M ".concat(fromX).concat(",").concat(fromY).concat(" ").concat(toX).concat(",").concat(toY);
                var arrow = angular.element(element[0].getSVGDocument().getElementById("arrow".concat(arrowId)));
                arrow.attr("d", arrowline);
                if(isMyTerritory(destinationTerritoryId) && TurnService.getTurnStatus() == "MOVE"){
                    arrow.attr("class", "arrowToSelf");
                }else if(!isMyTerritory(destinationTerritoryId) && TurnService.getTurnStatus() == "ATTACK"){
                    arrow.attr("class", "arrowToEnemy");
                }
            }

            function isMyTerritory(territoryId){
                var length = gameBoard.territories.length;
                for (var i = 0; i < length; i++) {
                    if(gameBoard.territories[i].key == territoryId && thisPlayer.id == gameBoard.territories[i].playerId){
                        return true;
                    }
                }
                return false;
            }

            function getNumberOfUnitsOnTerritory(territory){
                var length = gameBoard.territories.length;
                for (var i = 0; i < length; i++) {
                    if(gameBoard.territories[i].key == territory){
                        return gameBoard.territories[i].numberOfUnits;
                    }
                }
            }

            function setNumberOfUnitsOnTerritory(territory, numberOfUnits){
                var length = gameBoard.territories.length;
                for (var i = 0; i < length; i++) {
                    if(gameBoard.territories[i].key == territory){
                        gameBoard.territories[i].numberOfUnits = numberOfUnits;
                    }
                }
            }

            scope.mouseOver = function (territory) {
                var land = angular.element(element[0].getSVGDocument().getElementById(territory));
                var colorClass = land.attr("class");
                if (colorClass != "neutralcolor") {
                    land.attr("class", colorClass.concat("hover"));
                }
            };

            scope.mouseOut = function (territory) {
                var land = angular.element(element[0].getSVGDocument().getElementById(territory));
                var colorClass = land.attr("class");
                if (colorClass != "neutralcolor") {
                    land.attr("class", colorClass.replace("hover", ""));
                }
            };

            function hideArrows() {
                var i;
                for (i = 1; i < 7; ++i) {
                    var arrow = angular.element(element[0].getSVGDocument().getElementById("arrow".concat(i)));
                    arrow.attr("class", "arrowhidden");
                }
            }

            scope.voidClick = function () {
                hideArrows();
                scope.reinforcePopupVisible = false;
                selectedHomeRegion = null;
            }
        }
    }
}]);