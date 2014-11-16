'use strict';

angular.module('depressApp.downloads', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/downloads', {
    templateUrl: 'downloads/index.html',
    controller: 'PageController'
  });
}])

.controller('PageController', [function() {

}]);