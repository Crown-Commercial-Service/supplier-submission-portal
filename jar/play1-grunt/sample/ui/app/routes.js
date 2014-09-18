angular.module('app').config(['$routeProvider', '$locationProvider', function ($routeProvider, $locationProvider) {
	'use strict';

	$locationProvider.html5Mode(true);

  $routeProvider
    .when('/', { templateUrl: '/views/main.html', controller: 'MainCtrl' })
    .when('/login', { templateUrl: '/views/login.html', controller: 'LoginCtrl' })

    .when('/todos', { templateUrl: '/views/todo/list.html', controller: 'TodoListCtrl' })
    .when('/todo/:todoId', { templateUrl: '/views/todo/view.html', controller: 'TodoViewCtrl' })
    .when('/todo/:todoId/edit', { templateUrl: '/views/todo/edit.html', controller: 'TodoEditCtrl' });

    // Fallback
    //.otherwise({ redirectTo: '/' });
}]);
