angular.module('app').controller('GrowlCtrl', ['$scope', 'pubsub', function ($scope, pubsub) {
  'use strict';

  $scope.alerts = [];

  pubsub.subscribe('growl', function (event, alertType, message) {
    $scope.alerts.push({
      type: alertType || '',
      msg: message
    });
  });

  $scope.close = function (index) {
    $scope.alerts.splice(index, 1);
  };
}]);
