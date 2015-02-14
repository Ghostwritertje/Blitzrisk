'use strict';
var router = angular.module('blitzrisk', ['ngRoute', 'blitzriskControllers']);

router.config(['$routeProvider',
    function($routeProvider){
        $routeProvider.when('/login',{
            templateUrl: 'app/components/login/login.html',
            controller: 'LoginController'
        }).when('/game',{
            templateUrl: 'app/components/game/game.html',
            controller: 'GameController'
        }).otherwise({redirectTo: '/login'});
    }]);