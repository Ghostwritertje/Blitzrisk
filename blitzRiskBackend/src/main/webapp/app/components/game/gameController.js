'use strict';
angular.module('blitzriskControllers').controller('GameController', ['$scope', "GameService",
    function ($scope, GameService) {

        $scope.testClass = function(){
            alert("test");
            var land = angular.element(this.getSVGDocument().getElementById("path5004"));
            land.removeClass("neutralcolor");
            land.addClass("player1color");

        };
        $scope.doClick = function() {
            alert("clicked");
        };

        function init(){
            alert("test");
            var territoryLayoutPromise = GameService.initTerritoryLayout();
            territoryLayoutPromise.then(function(data){
                alert("jeej");
            }).catch(function(data){alert("aaah");})
        };

        init();
    }
]).directive('riskmap', [ "GameService", function (GameService) {
    return {
        restrict: 'E',
        replace: true,
        template: "<object type='image/svg+xml' data='assets/img/riskMap.svg'></object>",
        link: function(scope, element, attrs) {
            scope.changeTerritoryStyle = function(player, territory){
                //alert(territory);
                scope.hideArrows();
                showNeighbour(territory);

                /*var land = angular.element(element[0].getSVGDocument().getElementById(territory));
                //land.removeClass("neutralcolor");
                land.addClass("player".concat(player).concat("color"));
                scope.hideArrows();
                var homeTerr = angular.element(element[0].getSVGDocument().getElementById(19));
                var homeX = homeTerr.attr("xcoord");
                var homeY = homeTerr.attr("ycoord");
                var homet = angular.element(element[0].getSVGDocument().getElementById(21));
                var nX = homet.attr("xcoord");
                var nY = homet.attr("ycoord");
                var arrowline = "M ".concat(homeX).concat(",").concat(homeY).concat(" ").concat(nX).concat(",").concat(nY);
                var arrow = angular.element(element[0].getSVGDocument().getElementById("arrow2"));
                arrow.attr("d", arrowline);*/
            };

            function showNeighbour(territoryId){
                var territoryLayout = GameService.getTerritoryLayout();
                var neighbours = null;
                for(var i = 0, lenght = territoryLayout.length; i< lenght; i++){
                    if(territoryLayout[i].territoryKey == territoryId){
                        neighbours = territoryLayout[i].neighbours;
                    }
                }
                for(var i = 0, lenght = neighbours.length; i< lenght; i++){
                    var homeTerritory = angular.element(element[0].getSVGDocument().getElementById(territoryId));
                    var homeX = homeTerritory.attr("xcoord");
                    var homeY = homeTerritory.attr("ycoord");
                    var neighbourTerritory = angular.element(element[0].getSVGDocument().getElementById(neighbours[i]));
                    var nX = neighbourTerritory.attr("xcoord");
                    var nY = neighbourTerritory.attr("ycoord");
                    var arrowline = "M ".concat(homeX).concat(",").concat(homeY).concat(" ").concat(nX).concat(",").concat(nY);
                    var arrow = angular.element(element[0].getSVGDocument().getElementById("arrow".concat(i+1)));
                    arrow.attr("d", arrowline);
                    arrow.attr("class", "arrowvisible");
                }

            }

            scope.mouseOver = function(territory){
                var land = angular.element(element[0].getSVGDocument().getElementById(territory));
                land.addClass("player1colorhover");
                /*var test = angular.element(element[0].getSVGDocument().getElementById(territory));
                alert(test.attr("xcoord"));*/
                //player1colorhover
            };

            scope.mouseOut = function(territory){
                var land = angular.element(element[0].getSVGDocument().getElementById(territory));
                //land.removeClass("player1colorhover");
                land.attr("class", "player1color");
            }

            scope.hideArrows = function () {
                var i;
                for (i = 1; i < 7; ++i) {
                    var arrow = angular.element(element[0].getSVGDocument().getElementById("arrow".concat(i)));
                    arrow.attr("class", "arrowhidden");
                }
            }

            scope.voidClick = function(){
                scope.hideArrows();
            }
        }
    }
}]);