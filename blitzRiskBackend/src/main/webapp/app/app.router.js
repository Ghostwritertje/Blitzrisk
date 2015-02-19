'use strict';
var router = angular.module('blitzrisk', ['ngRoute', 'blitzriskControllers']);

router.config(['$routeProvider',
    function ($routeProvider) {
        $routeProvider.when('/login', {
            templateUrl: 'app/components/login/login.html',
            controller: 'LoginController'
        }).when('/register', {
            templateUrl: 'app/components/register/register.html',
            controller: 'RegisterController'
        }).when('/game', {
            templateUrl: 'app/components/game/game.html',
            controller: 'GameController'
        }).when('/overview', {
            templateUrl: 'app/components/overview/overview.html',
            controller: 'OverviewController'
        }).otherwise({redirectTo: '/login'});
    }]);


//Security:
//If user is not logged in, he is send to log-in page
router.run(function ($rootScope, $location, LoginService) {
    //Add pages here that users can access without logging in
    var unsecuredPages = ['/login', '/register'];


    $rootScope.$on('$routeChangeStart', function (event, next, current) {
        if (!LoginService.isLoggedIn() && unsecuredPages.indexOf($location.url()) < 0) {

            event.preventDefault();
            $location.path('/login');
        }

    });
});