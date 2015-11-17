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

		var uiConfigURL = '/glass/rest/uiconfig';
		var describeURL = '/glass/rest/describe/';
		var compareURL = '/glass/rest/compare/';
		var importURL = '/glass/rest/import/';

		var myFileName = '';
		
		this.getPValueMessage = function(cor) {
			var msg = null;
			
			if (cor <= .01) {
				msg = 'There is a very strong statistical correlation';
			} else if (cor > .01 && cor <= .05) {
				msg = 'There is a significant statistical correlation';
			} else if (cor > .5 && cor <= .1) {
				msg = 'There is a small statistical correlation';
			} else {
				msg = 'There is no statistical correlation';
			}
			
			return msg;
		};
		
		this.getPearsonMessage = function(cor) {
			var corMessage = null;
			if (cor < .1) {
				corMessage = 'There is no statistical correlation';
			} else if (cor >= .1 && cor < .25) {
				corMessage = 'There is very little statistical correlation';
			} else if (cor >= .25 && cor < .5) {
				corMessage = 'There is a small statistical correlation';
			} else if (cor >= .5 && cor < .75) {
				corMessage = 'There is a significant statistical correlation';
			} else if (cor >= .75) {
				corMessage = 'There is a very strong statistical correlation';
			}
			return corMessage;
		};
		
		this.getUUID = function() {
			return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
			    var r = Math.random()*16|0, v = c == 'x' ? r : (r&0x3|0x8);
			    return v.toString(16);
			});
		};
		
		this.getUIFields = function(fileName) {
			myFileName = fileName;
			var deferred = $q.defer();
			var svcUrl = uiConfigURL + "/" + myFileName;
			$http({method:'GET', url:svcUrl}).
				success(function(data, status, headers, config) {
					deferred.resolve(data);
				}).
				error(function(data, status, headers, config) {
					console.log('error');
				});
			
			return deferred.promise;
		};
		
		this.import = function(fileName) {
			console.log("import");
			myFileName = fileName;						// save the name of the imported file
			var svcUrl = uiConfigURL + "/" + fileName;
			return $http({method:'GET', url:svcUrl});
		};
		
		this.compare = function(fieldOne, fieldTwo, uuid) {
			var deferred = $q.defer();
			var url = compareURL + myFileName + '/' + fieldOne + '/' + fieldTwo + '/' + uuid;
			$http({method:'GET', url:url}).
				success(function(data, status, headers, config) {
					deferred.resolve(data);
				}).
				error(function(data, status, headers, config) {
					console.log('error');
				});

			return deferred.promise;
		};
		
		this.describe = function(field) {
			var deferred = $q.defer();
			var url = describeURL + myFileName + '/' + field;
			$http({method:'GET', url:url}).
				success(function(data, status, headers, config) {
					deferred.resolve(data);
				}).
				error(function(data, status, headers, config) {
					console.log('error');
				});

			return deferred.promise;
		};
	});
