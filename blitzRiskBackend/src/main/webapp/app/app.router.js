'use strict';
var router = angular.module('blitzrisk', ['ngRoute','door3.css', 'blitzriskControllers']);

router.config(['$routeProvider',
    function ($routeProvider) {
        $routeProvider.when('/login', {
            templateUrl: 'app/components/login/login.html',
            controller: 'LoginController',
            css: 'app/components/login/login.css'
        }).when('/register', {
            templateUrl: 'app/components/register/register.html',
            controller: 'RegisterController',
            css: ['app/components/register/register.css', 'app/components/login/login.css']
        }).when('/game', {
            templateUrl: 'app/components/game/game.html',
            controller: 'GameController',
            css: 'app/components/game/game.css'
        }).when('/overview', {
            templateUrl: 'app/components/overview/overview.html',
            controller: 'OverviewController',
            css: 'app/components/overview/overview.css'
        }).when('/gamelobby', {
            templateUrl: 'app/components/gamelobby/gamelobby.html',
            controller: 'GamelobbyController',
            css: 'app/components/gamelobby/gamelobby.css'
        }).when('/profile', {
            templateUrl: 'app/components/profile/profile.html',
            controller: 'ProfileController',
            css: 'app/components/profile/profile.css'
        }).otherwise(
            {
                redirectTo: '/login'});
    }]);


//Security:
//If user is not logged in, he is send to log-in page
router.run(function ($rootScope, $location, LoginService) {
    //Add pages here that users can access without logging in
    var unsecuredPages = ['/login', '/register' ];


    $rootScope.$on('$routeChangeStart', function (event, next, current) {
        $rootScope.currentPage = $location.url();
        if (!LoginService.isLoggedIn() && unsecuredPages.indexOf($location.url()) < 0) {

            event.preventDefault();
            $rootScope.currentPage = '/login';
            $location.path('/login');
        }

    });
});
