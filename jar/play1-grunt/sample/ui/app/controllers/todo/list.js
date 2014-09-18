angular.module('app').controller('TodoListCtrl', ['$scope', 'TodoRes', function ($scope, Todo) {
  'use strict';

  $scope.todos = Todo.query();

  $scope.remove = function (todo) {
    todo.$remove();
  };

}]);
