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
            // var gameBoard = {"playerTurn":0,"started":true,"players":[{"id":2,"color":1,"invitationStatus":"ACCEPTED","username":"test"},{"id":1,"color":0,"invitationStatus":"ACCEPTED","username":"test2"}],"turns":[],"territories":[{"numberOfUnits":1,"key":3,"playerId":2,"id":31},{"numberOfUnits":1,"key":42,"playerId":2,"id":41},{"numberOfUnits":1,"key":15,"playerId":1,"id":20},{"numberOfUnits":1,"key":18,"playerId":2,"id":1},{"numberOfUnits":1,"key":2,"playerId":2,"id":37},{"numberOfUnits":1,"key":29,"playerId":2,"id":29},{"numberOfUnits":1,"key":25,"playerId":2,"id":3},{"numberOfUnits":1,"key":13,"playerId":2,"id":11},{"numberOfUnits":1,"key":33,"playerId":2,"id":25},{"numberOfUnits":1,"key":38,"playerId":2,"id":19},{"numberOfUnits":1,"key":16,"playerId":2,"id":39},{"numberOfUnits":1,"key":7,"playerId":1,"id":22},{"numberOfUnits":1,"key":23,"playerId":1,"id":4},{"numberOfUnits":1,"key":35,"playerId":1,"id":10},{"numberOfUnits":1,"key":19,"playerId":1,"id":30},{"numberOfUnits":1,"key":39,"playerId":1,"id":2},{"numberOfUnits":1,"key":21,"playerId":2,"id":13},{"numberOfUnits":1,"key":26,"playerId":2,"id":5},{"numberOfUnits":1,"key":17,"playerId":1,"id":6},{"numberOfUnits":1,"key":27,"playerId":2,"id":21},{"numberOfUnits":1,"key":8,"playerId":1,"id":8},{"numberOfUnits":1,"key":30,"playerId":1,"id":40},{"numberOfUnits":1,"key":28,"playerId":2,"id":23},{"numberOfUnits":1,"key":22,"playerId":1,"id":14},{"numberOfUnits":1,"key":37,"playerId":2,"id":9},{"numberOfUnits":1,"key":14,"playerId":1,"id":32},{"numberOfUnits":1,"key":36,"playerId":2,"id":35},{"numberOfUnits":1,"key":34,"playerId":1,"id":18},{"numberOfUnits":1,"key":6,"playerId":1,"id":28},{"numberOfUnits":1,"key":40,"playerId":1,"id":34},{"numberOfUnits":1,"key":32,"playerId":2,"id":15},{"numberOfUnits":1,"key":24,"playerId":2,"id":7},{"numberOfUnits":1,"key":31,"playerId":1,"id":24},{"numberOfUnits":1,"key":4,"playerId":1,"id":42},{"numberOfUnits":1,"key":12,"playerId":1,"id":12},{"numberOfUnits":1,"key":11,"playerId":2,"id":27},{"numberOfUnits":1,"key":41,"playerId":1,"id":26},{"numberOfUnits":1,"key":9,"playerId":1,"id":36},{"numberOfUnits":1,"key":5,"playerId":1,"id":38},{"numberOfUnits":1,"key":10,"playerId":2,"id":17},{"numberOfUnits":1,"key":1,"playerId":2,"id":33},{"numberOfUnits":1,"key":20,"playerId":1,"id":16}],"id":1};
            var gameBoard = "";
            var players = null;
            var thisPlayer = null;

            angular.element(document).ready(function () {
                loadGameBoard();
                loadTerritoryLayout();
                var regiontext = angular.element(element[0].getSVGDocument().getElementById("19-text"));
                regiontext.attr("coord", "dit werkt");
                //regiontext.nodeValue = "2";
                /*function clickRegion(regionid) {
                    //Android.clickRegion(regionid);
                    //document.getElementById("mysvg").getElementById(regionid).className='player1color';
                    // Get the Object by ID
                    var a = document.getElementById("mysvg");
                    // Get the SVG document inside the Object tag
                    var svgDoc = a.contentDocument;
                    // Get one of the SVG items by ID;
                    //var qty = parseInt(document.getElementById("qty").value);
                    var svgItem = svgDoc.getElementById(regionid);
                    // Set the colour to something else
                    //svgItem.attr("class","player1color");

                    //svgItem.setAttribute('class','player1color');
                    var l = document.getElementById('thecolor')
                    var thecolor = l.options[l.selectedIndex].text;
                    svgItem.setAttribute('class',thecolor);
                    svgItem.innerHTML = 'AA';
                    //svgItem.setAttribute('fill',thecolor);

                    console.log("onload executed");
                    console.log(svgDoc.getElementById(regionid).getAttribute("xcoord"));

                }*/
                alert("text change");
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

                    //hier Werk ik nog aan. heb het nog niet gevonden. niet weg doen. grt gunther ;)
                    //var textFieldId = gameBoard.territories[i].key.toString().concat("-text");
                    //alert(textFieldId);
                    //var regionTextField = angular.element(element[0].getSVGDocument().getElementById(textFieldId));
                    //regionTextField.textContent  = 'hello';
                    //regionTextField.attr("text", "heyho");
                    //regionTextField.text = "bla";
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
                    var nX = null;
                    var nY = null;
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