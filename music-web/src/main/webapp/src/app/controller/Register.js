
// 'use strict';

// /**
//  * Login controller.
//  */
// angular.module('music').controller('Signup', function($rootScope, $scope, $state, $dialog, User, Playlist, NamedPlaylist, Websocket) {
//   $scope.login = function() {
//     User.login($scope.user).then(function() {
//       User.userInfo(true).then(function(data) {
//         $rootScope.userInfo = data;
//       });

//       // Update playlist on application startup
//       Playlist.update().then(function() {
//         // Open the first track without playing it
//         Playlist.open(0);
//       });

//       // Fetch named playlist
//       NamedPlaylist.update();

//       // Connect this player
//       Websocket.connect();

//       $state.transitionTo('main.music.albums');
//     }, function() {
//       var title = 'Login failed';
//       var msg = 'Username or password invalid';
//       var btns = [{ result:'ok', label: 'OK', cssClass: 'btn-primary' }];

//       $dialog.messageBox(title, msg, btns);
//     });
//   };
// });

'use strict';

/**
 * Settings user edition page controller.
 */
angular.module('music').controller('Register', function($scope, $dialog, $state, $stateParams, Restangular) {
  /**
   * Returns true if in edit mode (false in add mode).
   */
  $scope.isEdit = function() {
    return $stateParams.username;
  };
  
  /**
   * In edit mode, load the current user.
   */
  if ($scope.isEdit()) {
    Restangular.one('user', $stateParams.username).get().then(function(data) {
      $scope.user = data;
    });
  }

  /**
   * Update the current user.
   */
  $scope.edit = function() {
    var promise = null;
    
    if ($scope.isEdit()) {
      promise = Restangular
      .one('user', $stateParams.username)
      .post('', $scope.user);
    } else {
      promise = Restangular
      .one('user')
      .put($scope.user);
    }
    
    promise.then(function() {
      $scope.loadUsers();
      $state.transitionTo('login');
    });
  };

  /**
   * Delete the current user.
   */
  $scope.remove = function () {
    var title = 'Delete user';
    var msg = 'Do you really want to delete this user?';
    var btns = [{result:'cancel', label: 'Cancel'}, {result:'ok', label: 'OK', cssClass: 'btn-primary'}];

    $dialog.messageBox(title, msg, btns, function(result) {
      if (result == 'ok') {
        Restangular.one('user', $stateParams.username).remove().then(function() {
          $scope.loadUsers();
          $state.transitionTo('login');
        }, function () {
          $state.transitionTo('login');
        });
      }
    });
  };

});