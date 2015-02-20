/**
 * Created by jorandeboever on 18/02/15.
 */
'use strict';
angular.module('blitzriskControllers').controller('OverviewController', ['$scope', '$http', '$location', 'LoginService',
    function ($scope, $http, $location, LoginService) {
        $scope.friends = [{"name": "Dummy1"}, {"name": "Dummy2"}, {"name": "Dummy3"}];

        $scope.recentlyPlayed = [{"name": "Dummy145"}, {"name": "Dummy234"}, {"name": "Dummy367"}];

        $scope.games = [{"date": "21 Januari", "numberOfTurns": "12"}, {"date": "28 Februari", "numberOfTurns": "178"}];


        $scope.go = function (path) {
            $location.path(path);
        };

    }
]);