'use strict';
var app = angular.module('blitzrisk', [
    'blitzrisk.controllers',
    '' + 'blitzrisk.services',
    '$httpProvider',
    function($httpProvider) {
    $httpProvider.defaults.useXDomain = true;
    delete $httpProvider.defaults.headers.common['X-Requested-With'];
}]);
var controllers = angular.module('blitzrisk.controllers',['blitzrisk.services']);
var service = angular.module('blitzrisk.services', []);