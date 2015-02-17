'use strict';
var blitzrisk = angular.module('blitzrisk', [
    'blitzriskControllers',
    'blitzriskServices',
    '$httpProvider',
    function($httpProvider) {
    $httpProvider.defaults.useXDomain = true;
    delete $httpProvider.defaults.headers.common['X-Requested-With'];
}]);
var blitzriskControllers = angular.module('blitzriskControllers',['blitzriskServices']);
var blitzriskServices = angular.module('blitzriskServices', []);


