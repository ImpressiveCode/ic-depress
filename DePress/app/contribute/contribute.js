'use strict';

angular.module('depressApp.contribute', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/contribute', {
    templateUrl: 'contribute/index.html',
    controller: 'PageController'
  });
}])

.controller('PageController', [function() {

}]);