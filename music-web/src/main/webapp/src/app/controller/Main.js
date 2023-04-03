'use strict';

/**
 * Main controller.
 */
angular.module('music').controller('Main', function($rootScope, $state, $scope, Playlist, Album, Restangular) {
  $scope.partyMode = Playlist.isPartyMode();

  $scope.spotifySearch = function(query) {
    Restangular.one("search/spotifysearch").get(query).then(function(response) {
      $scope.myData = response;
    });
  };

  $scope.spotifyRecommend = function(playlistId) {
    Restangular.one("playlist/" + playlistId + "/spotifyrecommendation").get().then(function(response) {
      $scope.myData = response;
      console.log(response);
    });
  };

  // Keep party mode in sync
  $rootScope.$on('playlist.party', function(e, partyMode) {
    $scope.partyMode = partyMode;
  });

  // Start party mode
  $scope.startPartyMode = function() {
    Playlist.party(true, true);
    $state.transitionTo('main.playing');
  };

  // Stop party mode
  $scope.stopPartyMode = function() {
    Playlist.setPartyMode(false);
  };

  // Clear the albums cache if the previous state is not main.album
  $scope.$on('$stateChangeStart', function (e, to, toParams, from) {
    if (to.name == 'main.music.albums' && from.name != 'main.album') {
      Album.clearCache();
    }
  });
});