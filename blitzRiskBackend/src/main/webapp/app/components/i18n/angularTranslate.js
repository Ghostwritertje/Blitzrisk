/**
 * Created by Alexander on 5/3/2015.
 */
var angularTranslate = angular.module('angularTranslate', ['pascalprecht.translate']);

angularTranslate.config(function ($translateProvider) {
    $translateProvider.translations('en', {
        LOGIN: 'Log in',
        SIGNUP: '¨Sign up'
    });
    $translateProvider.translations('nl', {
        LOGIN: 'Inloggen',
        SIGNUP: 'Registreren'
    });
    $translateProvider.preferredLanguage('en');
});

angularTranslate.controller('translationController', function ($scope, $translate) {
    $scope.changeLanguage = function (key) {
        $translate.use(key);
    };
});