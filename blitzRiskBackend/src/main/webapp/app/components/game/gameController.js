'use strict';
angular.module('blitzriskControllers').controller('GameController', ['$scope',
    function ($scope) {
     //   alert('id');
        /*$scope.clickRegion = function(){
            alert('id');
        }*/
    }
]).directive('riskmap', [function () {
    return {
        restrict: 'E',
        replace: true,
        template: "<object type='image/svg+xml' data='assets/img/status.svg'></object>",
        link: function (scope, element, attrs) {

                var statusElm = angular.element(element[0]
                    .getSVGDocument().getElementById("status"));
                statusElm.on("click", function(event) {
                    scope.$apply(function() {
                        scope.currentStatus='alert';
                    });
                });

        }
    }
}]);

/*.directive('risk-map', [function () {
 return {
 restrict: 'A',
 scope: true,
 template: "<object id='riskMap' type='image/svg+xml' data='assets/img/status.svg'></object>",
 link: function (scope, element, attrs) {
 scope.elementId = element.attr("id");
 scope.regionClick = function () {
 alert(scope.elementId);
 };
 }
 }
 }]);


 return {
 restrict: 'AE',
 replace: true,
 template: "<object type='image/svg+xml' data='assets/img/status.svg'></object>",
 link: function (scope, element, attrs) {
 var statusChanged = function(newValue,oldValue) {
 var statusElm = angular.element(element[0].getSVGDocument().getElementById("status"));

 statusElm.removeClass(newValue);
 statusElm.addClass(oldValue);
 };
 scope.$watch(attrs.watch, statusChanged);
 }
 }
 */