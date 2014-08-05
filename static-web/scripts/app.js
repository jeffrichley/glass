'use strict';

/**
 * @ngdoc overview
 * @name glassApp
 * @description
 * # glassApp
 *
 * Main module of the application.
 */
angular
  .module('glassApp', [
    'ngAnimate',
    'ngCookies',
    'ngResource',
    'ngRoute',
    'ngSanitize',
    'ngTouch',
    'ui.bootstrap'
  ])
  .config(function ($routeProvider) {
    $routeProvider
      .when('/', {
        templateUrl: 'views/main.html',
        controller: 'MainCtrl'
      })
      .when('/about', {
        templateUrl: 'views/about.html',
        controller: 'AboutCtrl'
      })
      .when('/analyze', {
        templateUrl: 'views/analyze.html',
        controller: 'AnalyzeCtrl'
      })
      .otherwise({
        redirectTo: '/'
      });
  });
