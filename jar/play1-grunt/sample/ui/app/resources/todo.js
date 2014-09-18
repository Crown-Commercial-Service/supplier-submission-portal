angular.module('app').factory('TodoRes', ['$resource', function ($resource) {
  'use strict';
  return $resource('/api/todo/:todoId', {todoId: '@id'});
}]);
