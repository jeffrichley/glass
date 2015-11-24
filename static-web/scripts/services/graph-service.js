angular.module('glassApp')
.service('GraphService', function dataservice($q, $http, $routeParams) {
	
	var dataId = $routeParams.dataId;
	
	var getUUID = function() {
		return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
		    var r = Math.random()*16|0, v = c == 'x' ? r : (r&0x3|0x8);
		    return v.toString(16);
		});
	};
	
	this.getDescription = function(field) {
		var deferred = $q.defer();
		var url = '/glass/rest/describe/' + dataId + '/' + field;
		$http.get(url).success(function(data) {
			deferred.resolve(data);
		}).error(function(data, status, headers, config) {
			if (window.console) {
				console.log('error');
			}
		});
		return deferred.promise;
	};
	
	this.getComparison = function(field1, field2) {
//		console.log('field1 = ', field1);
//		console.log('field2 = ', field2);
		var deferred = $q.defer();
		var url = '/glass/rest/compare/' + dataId + '/' + field1.label + '/' + field2.label + '/' + getUUID();
		$http.get(url).success(function(data) {
			deferred.resolve(data);
		}).error(function(data, status) {
			if (window.console) {
				console.log('error: ', data);
			}
		});
		return deferred.promise;
	};
});