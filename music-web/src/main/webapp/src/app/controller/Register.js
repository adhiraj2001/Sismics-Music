
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