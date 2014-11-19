'use strict';

angular.module('depressApp.examples', ['ngRoute'])

    .config(['$routeProvider', function ($routeProvider) {
        $routeProvider.when('/examples', {
            templateUrl: 'examples/index.html',
            controller: 'ExamplesController'
        });
    }])

    .controller('ExamplesController', ['$anchorScroll', '$location', '$scope',
        function ($anchorScroll, $location, $scope, $log) {

            $scope.gotoAnchor = function (x) {
                if ($location.hash() !== x) {
                    // set the $location.hash to `newHash` and
                    // $anchorScroll will automatically scroll to it
                    $location.hash(x);
                } else {
                    // call $anchorScroll() explicitly,
                    // since $location.hash hasn't changed
                    $anchorScroll();
                }
            };

            $scope.showLightbox = function(x) {
                event.preventDefault();
                $(x).ekkoLightbox({
                    'data-type' : 'image'
                });
            }
        }



    ]);