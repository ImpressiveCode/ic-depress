'use strict';

angular.module('depressApp.additional', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/plugins', {
    templateUrl: 'plugins/index.html',
    controller: 'PageController'
  });
}])

.controller('PageController', [function() {

}]);