'use strict';

angular.module('depressApp.main', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/main', {
    templateUrl: 'main/index.html',
    controller: 'PageController'
  });
}])

.controller('PageController', [function() {

}]);