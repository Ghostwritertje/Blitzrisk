'use strict';

angular.module('blitzriskServices').factory('userService', ['$http', '$q',
    function ($http, $q) {

        return {
            addFriend: function (userId, friendId) {
                var deferred = $q.defer();

                $http.post(hosturl + "addFriend/userId/" + userId + "/friendId/" + friendId, {headers: {'X-Auth-Token': token}})
                    .success(function (data) {
                        deferred.resolve('success');
                    }).
                    error(function () {
                        deferred.reject('Token Expired');
                    });
                return deferred.promise;

            }
        }
    }]);
