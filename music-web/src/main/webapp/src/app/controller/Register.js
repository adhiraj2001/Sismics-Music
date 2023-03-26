
'use strict';

/**
 * Register controller.
 */
angular.module('music').controller('Register', function($rootScope, $scope, $state, $dialog, User, Restangular) {

  $scope.checkUsername = function(username) {
    User.checkUsername(username).then(function(data) {
      $scope.reg.valid = data;
    }, function() {
      var title = 'Usercheck failed';
      var msg = 'Username invalid'
      var btns = [{ result:'ok', label: 'OK', cssClass: 'btn-primary' }];

      $dialog.messageBox(title, msg, btns);
    });
  };

  $scope.register = function(user) {
    
    $scope.reg = user
    $scope.reg.valid = false;

    User.register(user).then(function() {
      //$scope.checkUsername(user.username);
      $state.transitionTo('login');
    }, function() {
      var title = 'Registration failed';
      var msg = 'Username or password invalid'
      var btns = [{ result:'ok', label: 'OK', cssClass: 'btn-primary' }];

      $dialog.messageBox(title, msg, btns);
    });
  };


  $scope.goto_login = function($event) {
    $state.transitionTo('login');
    $event.preventDefault();
  };

});





// 'use strict';

// /**
//  * Settings user edition page controller.
//  */
// angular.module('music').controller('Register', function($scope, $dialog, $state, User, Restangular) {
//   /**
//    * Returns true if in edit mode (false in add mode).
//    */
//   $scope.isEdit = function() {
//     return $stateParams.username;
//   };
  
//   /**
//    * In edit mode, load the current user.
//    */
//   if ($scope.isEdit()) {
//     Restangular.one('user', $stateParams.username).get().then(function(data) {
//       $scope.user = data;
//     });
//   }

//   /**
//    * Update the current user.
//    */
//   $scope.edit = function() {
//     var promise = null;
    
//     if ($scope.isEdit()) {
//       promise = Restangular
//       .one('user', $stateParams.username)
//       .post('', $scope.user);
//     } else {
//       promise = Restangular
//       .one('user')
//       .put($scope.user);
//     }
    
//     promise.then(function() {
//       $scope.loadUsers();
//       $state.transitionTo('login');
//     });
//   };

//   /**
//    * Delete the current user.
//    */
//   $scope.remove = function () {
//     var title = 'Delete user';
//     var msg = 'Do you really want to delete this user?';
//     var btns = [{result:'cancel', label: 'Cancel'}, {result:'ok', label: 'OK', cssClass: 'btn-primary'}];

//     $dialog.messageBox(title, msg, btns, function(result) {
//       if (result == 'ok') {
//         Restangular.one('user', $stateParams.username).remove().then(function() {
//           $scope.loadUsers();
//           $state.transitionTo('login');
//         }, function () {
//           $state.transitionTo('login');
//         });
//       }
//     });
//   };

// });