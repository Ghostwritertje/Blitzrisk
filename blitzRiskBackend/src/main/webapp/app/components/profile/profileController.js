/**
 * Created by jorandeboever on 19/02/15.
 */
'use strict';
angular.module('blitzriskControllers').controller('ProfileController', ['$scope', '$rootScope', '$http', '$location', 'LoginService',
    function ($scope, $rootScope, $http, $location, LoginService) {
        $scope.username = "";
        $scope.email = '';
        $scope.password = '';
        $scope.newPassword = '';
        $scope.confirmNewPassword = '';
        $scope.updatesAvailable = false;
        $scope.wrongPassword = false;
        $scope.errorPasswordRequired = false;
        $scope.errorPasswordWrong = false;

        $scope.emailState = 0; //state: 0 = unchanged, 1 is changed by user, 2 is change is saved on server
        $scope.usernameState = 0;
        $scope.passwordState = 0;

        var userPromise = LoginService.getUserDetails();
        userPromise.then(function (user) {
            $scope.username = user.username;
            $scope.email = user.email;

        });

        function reset() {
            $scope.password = '';
            $scope.newPassword = '';
            $scope.confirmNewPassword = '';
            $scope.wrongPassword = false;
            $scope.updatesAvailable = false;
            $scope.errorPasswordWrong = false;
            $scope.errorPasswordRequired = false;


            var userPromise = LoginService.getUserDetails();
            userPromise.then(function (user) {
                $scope.username = user.username;
                $scope.email = user.email;

            });
        }


        $scope.cancel = reset();

        $scope.save = function save() {
            if ($scope.password === "") {
                $scope.wrongPassword = true;
                $scope.errorPasswordRequired = true;
            }else if(!LoginService.authenticate($scope.password)){

                $scope.wrongPassword = true;
                $scope.errorPasswordWrong = true;


            } else  {
                var newPassword = null;
                if ($scope.newPassword.length > 0 && angular.equals($scope.newPassword, $scope.confirmNewPassword)) {
                    newPassword = $scope.newPassword;
                }
                var promise = LoginService.updateUser($scope.username, $scope.email, newPassword);

                promise.then(function () {
                    reset();
                    if ($scope.usernameState == 1) {
                        $scope.usernameState = 2;
                    } else {
                        $scope.usernameState = 0;
                    }
                    if ($scope.passwordState == 1) {
                        $scope.passwordState = 2;
                    } else {
                        $scope.passwordState = 0;
                    }
                    if ($scope.emailState == 1) {
                        $scope.emailState = 2;
                    } else {
                        $scope.emailState = 0;
                    }
                });
            }
        };


        $scope.setUpdatesAvailable = function (field) {
            $scope.updatesAvailable = true;
            switch (field) {
                case 'username':
                    $scope.usernameState = 1;
                    break;
                case 'password':
                    $scope.passwordState = 1;
                    break;
                case 'email':
                    $scope.emailState = 1;
                    break;
            }

        };

        $scope.go = function (path) {
            $location.path(path);
        };

        $scope.resetPassError = function () {
            $scope.wrongPassword = false;
            $scope.errorPasswordRequired = false;
            $scope.errorPasswordWrong = false;
        }

    }
])
;