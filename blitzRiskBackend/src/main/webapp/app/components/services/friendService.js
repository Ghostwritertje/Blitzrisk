/**
 * calls for the friends service
 */
'use strict';

angular.module('blitzriskServices').factory('FriendService', ['$http', '$q', 'LoginService',
    function ($http, $q, LoginService) {


        return {
            getFriends: function getFriends() {
                return $http.get('api/friends', {headers: {'X-Auth-Token': LoginService.getToken()}});
            },
            getFriendRequests: function getFriendRequests() {
                return $http.get('api/friendRequests', {headers: {'X-Auth-Token': LoginService.getToken()}});
            },
            getRecentlyPlayed: function getRecentlyPlayed(){
                return $http.get('api/recentlyplayed', {headers: {'X-Auth-Token': LoginService.getToken()}});
            },
            addFriend: function addFriend(username) {
                return $http.post('api/addFriend/' + username, null, {headers: {'X-Auth-Token': LoginService.getToken()}});
            },
            acceptFriendRequest: function acceptFriendRequest(username) {
                return $http.post('api/acceptFriend/' + username, null, {headers: {'X-Auth-Token': LoginService.getToken()}});
            }
        }
    }
]);