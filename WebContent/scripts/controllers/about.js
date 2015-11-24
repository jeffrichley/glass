'use strict';

/**
 * @ngdoc function
 * @name glassApp.controller:AboutCtrl
 * @description
 * # AboutCtrl
 * Controller of the glassApp
 */
angular.module('glassApp')
  .controller('AboutCtrl', function ($scope) {
    $scope.awesomeThings = [
      'HTML5 Boilerplate',
      'AngularJS',
      'Karma'
    ];
  });
