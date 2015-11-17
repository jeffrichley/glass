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
    'angularFileUpload',
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
      .when('/analyze/:fileName', {
        templateUrl: 'views/analyze.html',
        controller: 'AnalyzeCtrl'
      })
      .when('/upload', {
        templateUrl: 'views/datasets.html',
        controller: 'DatasetCtrl'
      })
      .when('/:fileId', {
    	  templateUrl: 'views/main.html',
    	  controller: 'MainCtrl'
      })
      .otherwise({
        redirectTo: '/'
      });
  });
