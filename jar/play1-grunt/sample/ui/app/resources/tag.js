angular.module('app').factory('TagRes', ['$resource', function ($resource) {
  'use strict';
  return $resource('/api/tag/:tagId', {tagId: '@id'});
}]);
