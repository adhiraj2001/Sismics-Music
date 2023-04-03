'use strict';

/**
 * User service.
 */
angular.module('music').factory('User', function(Restangular) {
  var userInfo = null;
  
  return {
    /**
     * Returns user info.
     * @param force If true, force reloading data
     */
    userInfo: function(force) {
      if (userInfo == null || force) {
        userInfo = Restangular.one('user').get();
      }
      return userInfo;
    },
    
    /**
     * Login an user.
     */
    login: function(user) {
      return Restangular.one('user').post('login', user);
    },

    /**
     * Register an user.
     */
    register: function(user) {
      return Restangular.one('user').put({username: user.username, password: user.password, locale: null, email: user.email});
    },

    /**
     * Check an user.
     */
    checkUsername: function(username) {
      return Restangular.one('user/check_username').get({username: username});
    },
    
    /**
     * Logout the current user.
     */
    logout: function() {
      return Restangular.one('user').post('logout', {});
    }
  }
});