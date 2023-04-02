'use strict';

/**
 * Album controller.
 */
angular.module('music').controller('Album', function($scope, $state, $stateParams, Restangular, Playlist, $modal) {

  // Load album
  Restangular.one('album', $stateParams.id).get().then(function(data) {
    $scope.album = data;

    // var accessType = $scope.album.access;
    $scope.isPublic = (data.access === "PUBLIC");
    console.log("Album isPublic: " + $scope.isPublic);

    // var ownerType = $scope.album.isOwner;
    $scope.isOwner = data.isOwner;
    console.log("Album isOwner: " + $scope.isOwner);

    // var accessType = $scope.album.access;
    $scope.userId = data.userId;
    console.log("Album userId: " + $scope.userId);
  });


  $scope.changeAccess = function() {
    Restangular.one('album', $stateParams.id).post('access', { access: ($scope.isPublic ? "PUBLIC" : "PRIVATE") }).then(function() {
      console.log("Album isPublic: " + $scope.isPublic);
    });
  };

  // Play a single track
  $scope.playTrack = function(track) {
    Playlist.removeAndPlay(track);
  };

  // Add a single track to the playlist
  $scope.addTrack = function(track) {
    Playlist.add(track, false);
  };

  // Add all tracks to the playlist in a random order
  $scope.shuffleAllTracks = function() {
    Playlist.addAll(_.shuffle(_.pluck($scope.album.tracks, 'id')), false);
  };

  // Play all tracks
  $scope.playAllTracks = function() {
    Playlist.removeAndPlayAll(_.pluck($scope.album.tracks, 'id'));
  };

  // Add all tracks to the playlist
  $scope.addAllTracks = function() {
    Playlist.addAll(_.pluck($scope.album.tracks, 'id'), false);
  };

  // Zoom the album art in a modal
  $scope.zoomAlbumArt = function() {
    $modal.open({
      template: '<img src="../api/album/' + $scope.album.id + '/albumart/large" />',
      windowClass: 'album-art-modal'
    });
  };

  // Edit album's tracks tags
  $scope.editTags = function() {
    $state.transitionTo('tag', {
      id: $stateParams.id
    });
  };

  // Like/unlike a track
  $scope.toggleLikeTrack = function(track) {
    Playlist.likeById(track.id, !track.liked);
  };

  // Update UI on track liked
  $scope.$on('track.liked', function(e, trackId, liked) {
    var track = _.findWhere($scope.album.tracks, { id: trackId });
    if (track) {
      track.liked = liked;
    }
  });
});