'use strict';

angular.module('depressApp.examples', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/examples', {
    templateUrl: 'examples/index.html',
    controller: 'PageController'
  });
}])

.controller('PageController', [function() {

}]);