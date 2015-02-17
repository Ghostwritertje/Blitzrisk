'use strict';
angular.module('blitzrisk.controllers').controller('gameCtrl', ['$scope',
    function ($scope) {
        alert('id');
        /*$scope.clickRegion = function(){
            alert('id');
        }*/
    }
]).directive('region', [function () {
    return {
        restrict: 'A',
        scope: true,
        link: function (scope, element, attrs) {
            scope.elementId = element.attr("id");
            scope.regionClick = function () {
                alert(scope.elementId);
            };
        }
    }
}]);