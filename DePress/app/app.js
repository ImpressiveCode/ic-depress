'use strict';

// Declare app level module which depends on views, and components
angular.module('depressApp', [
  'ngRoute',
  'depressApp.main',
  'depressApp.additional',
  'depressApp.about',
  'depressApp.contribute',
  'depressApp.downloads',
  'depressApp.examples',
  'depressApp.quickstart',

]).
config(['$routeProvider', function($routeProvider) {
  $routeProvider.otherwise({redirectTo: '/main'});
}]);
