'use strict';

angular.module('depressApp.about', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/about', {
    templateUrl: 'about/index.html',
    controller: 'PageController'
  });
}])

.controller('PageController', [function() {

}]);