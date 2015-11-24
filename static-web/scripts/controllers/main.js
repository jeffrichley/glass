'use strict';

/**
 * @ngdoc function
 * @name glassApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the glassApp
 */
angular.module('glassApp')
  .controller('MainCtrl', function ($scope) {
    $scope.awesomeThings = [
      'HTML5 Boilerplate',
      'AngularJS',
      'Karma'
    ];
  });
