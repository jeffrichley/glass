'use strict';

/**
 * @ngdoc service
 * @name statlabApp.dataservice
 * @description
 * # dataservice
 * Service in the statlabApp.
 */
angular.module('glassApp')
	.service('dataservice', function dataservice($http, $q, $timeout, $location, $window) {
		// AngularJS will instantiate a singleton by calling "new" on this function

		var uiConfigURL = 'http://localhost:8080/glass/rest/uiconfig';
		var describeURL = 'http://localhost:8080/glass/rest/describe/';
		var compareURL = 'http://localhost:8080/glass/rest/compare/';

		var svc = {};

		svc.getUIFields = function() {
			var deferred = $q.defer();
			
			$http({method:'GET', url:uiConfigURL}).
				success(function(data, status, headers, config) {
					deferred.resolve(data);
				}).
				error(function(data, status, headers, config) {
					console.log('error');
				});
			
			return deferred.promise;
		};
		
		svc.compare = function(fieldOne, fieldTwo) {
			var deferred = $q.defer();
			var url = compareURL + fieldOne + '/' + fieldTwo;
			$http({method:'GET', url:url}).
				success(function(data, status, headers, config) {
					deferred.resolve(data);
				}).
				error(function(data, status, headers, config) {
					console.log('error');
				});

			return deferred.promise;
		};
		
		svc.describe = function(field) {
			var deferred = $q.defer();
			var url = describeURL + field;
			$http({method:'GET', url:url}).
				success(function(data, status, headers, config) {
					deferred.resolve(data);
				}).
				error(function(data, status, headers, config) {
					console.log('error');
				});

			return deferred.promise;
		};
		
		return svc;
	});
