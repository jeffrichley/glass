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
    'ngTouch'
  ])
  .config(function ($routeProvider) {
    $routeProvider
//      .when('/', {
//        templateUrl: 'views/main.html',
//        controller: 'MainCtrl'
//      })
//      .when('/about', {
//        templateUrl: 'views/about.html',
//        controller: 'AboutCtrl'
//      })
      .when('/analyze/:dataId', {
//        templateUrl: 'views/analyze3.html',
    	  templateUrl: 'views/analyze5.html',
        controller: 'Analyze2Ctrl'
      })
      .when('/analyze2/:dataId', {
        templateUrl: 'views/analyze4.html',
        controller: 'Analyze2Ctrl'
      })
      .when('/analyze3/:dataId', {
    	 templateUrl: 'views/analyze3.html',
    	 controller: 'Analyze2Ctrl'
      });
//      .otherwise({
//        redirectTo: '/'
//      });
  });
