'use strict';

angular.module('depressApp.additional', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/additional', {
    templateUrl: 'additional/index.html',
    controller: 'PageController'
  });
}])

.controller('PageController', [function() {

}]);