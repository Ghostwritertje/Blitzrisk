'use strict';
angular.module('blitzriskControllers').controller('GameController', ['$scope', "GameService",
    function ($scope, GameService) {

    }
]).directive('riskmap', ["GameService", "LoginService", function (GameService, LoginService) {
    return {
        restrict: 'E',
        replace: true,
        template: "<object type='image/svg+xml' data='assets/img/riskMap.svg'></object>",
        link: function (scope, element, attrs) {
            var gameBoard = "";
            var players = null;
            var thisPlayer = null;

            angular.element(document).ready(function () {
                loadGameBoard();
                loadTerritoryLayout();
            });

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
                players = gameBoard.players;
                var length = players.length;
                for(var i = 0; i < length; i++){
                    if(players[i].username == LoginService.getUserName()){
                        thisPlayer = players[i];
                        break;
                    }
                }

                length = gameBoard.territories.length;
                for (var i = 0; i < length; i++) {
                    var region = angular.element(element[0].getSVGDocument().getElementById(gameBoard.territories[i].key));
                    var colorClass = "player".concat(getPlayer(gameBoard.territories[i].playerId).color + 1).concat("color");
                    region.attr("class", colorClass);
                    var id = gameBoard.territories[i].key.toString().concat("-text");
                    changeTerritoryText(id, gameBoard.territories[i].numberOfUnits);
                }
            }

            function getPlayer(playerId) {
                var length = players.length;
                for (var i = 0; i < length; i++) {
                    if (players[i].id == playerId) {
                        return players[i];
                    }
                }
            }

            scope.changeTerritoryStyle = function (territory) {
                scope.hideArrows();
                if(isMyTerritory(territory)){
                    GameService.getTerritoryLayout().then(function (layout) {
                        showNeighbour(layout, territory);
                    });
                }
            };

            function showNeighbour(layout, territoryId) {
                var territoryLayout = layout;
                var neighbours = null;

                var lenght = territoryLayout.length;
                for (var i = 0; i < lenght; i++) {
                    if (territoryLayout[i].territoryKey == territoryId) {
                        neighbours = territoryLayout[i].neighbours;
                    }
                }

                lenght = neighbours.length;
                var homeTerritory = angular.element(element[0].getSVGDocument().getElementById(territoryId));
                var homeX = homeTerritory.attr("xcoord");
                var homeY = homeTerritory.attr("ycoord");
                var arrowId = 1;
                for (var i = 0; i < lenght; i++) {
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
                if(isMyTerritory(destinationTerritoryId)){
                    arrow.attr("class", "arrowToSelf");
                }else{
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

            scope.hideArrows = function () {
                var i;
                for (i = 1; i < 7; ++i) {
                    var arrow = angular.element(element[0].getSVGDocument().getElementById("arrow".concat(i)));
                    arrow.attr("class", "arrowhidden");
                }
            }

            scope.voidClick = function () {
                scope.hideArrows();
            }
        }
    }
}]);