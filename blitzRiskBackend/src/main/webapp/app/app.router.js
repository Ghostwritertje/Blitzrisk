'use strict';
var router = angular.module('blitzrisk', ['ngRoute', 'blitzrisk.controllers']);

router.config(['$routeProvider',
    function($routeProvider){
        $routeProvider.when('/login',{
            templateUrl: 'app/components/login/login.html',
            controller: 'loginCtrl'
        }).when('/game',{
            templateUrl: 'app/components/game/game.html',
            controller: 'gameCtrl'
        }).otherwise({redirectTo: '/login'});
    }]);