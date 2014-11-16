'use strict';

angular.module('depressApp.quickstart', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/quickstart', {
    templateUrl: 'quickstart/index.html',
    controller: 'PageController'
  });
}])

.controller('PageController', [function() {

}]);