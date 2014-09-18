angular.module('app').controller('LoginCtrl', ['$scope', function ($scope) {
  'use strict';

  $scope.user = {};

  $scope.login = function (user) {
    console.log('login with', user.email, user.password);
  };

}]);
