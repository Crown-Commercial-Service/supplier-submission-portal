angular.module('app').controller('TodoViewCtrl', ['$scope', '$routeParams', 'TodoRes', function ($scope, $routeParams, Todo) {
  'use strict';

  $scope.todo = Todo.get({ todoId: $routeParams.todoId });

}]);
