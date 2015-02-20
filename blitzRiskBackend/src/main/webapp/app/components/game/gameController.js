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
    }
]).directive('riskmap', [function () {
    return {
        restrict: 'E',
        replace: true,
        template: "<object type='image/svg+xml' data='assets/img/riskMap.svg'></object>",
        link: function(scope, element, attrs) {
            scope.init = function() {
                var land = angular.element(element[0].getSVGDocument().getElementById("path5004"));
                land.removeClass("neutralcolor");
                land.addClass("player1color");
            };

            scope.changeTerritoryStyle = function(player, territory){
                alert(territory);
                var land = angular.element(element[0].getSVGDocument().getElementById(territory));
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
                arrow.attr("d", arrowline);
            };

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
                alert("test");
            }
        }
    }
}]);