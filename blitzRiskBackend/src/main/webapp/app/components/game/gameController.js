'use strict';
angular.module('blitzriskControllers').controller('GameController', ['$scope',
    function ($scope) {
     //   alert('id');
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