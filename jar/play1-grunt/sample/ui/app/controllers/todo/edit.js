angular.module('app').controller('TodoEditCtrl', ['$scope', '$routeParams', 'pubsub', 'TodoRes', function ($scope, $routeParams, pubsub, Todo) {
  'use strict';

  $scope.todo = Todo.get({ todoId: $routeParams.todoId });

  $scope.save = function (todo) {
    todo.$save();
    pubsub.publish('growl', ['success', 'Todo ' + todo.name + ' saved']);
  };

}]);
