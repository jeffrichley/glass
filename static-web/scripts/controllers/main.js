'use strict';

/**
 * @ngdoc function
 * @name glassApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the glassApp
 */
angular.module('glassApp')
  .controller('MainCtrl', function ($scope, $routeParams, $location, dataservice) {
    $scope.awesomeThings = [
      'HTML5 Boilerplate',
      'AngularJS',
      'Karma'
    ];
    $scope.isImport = false;
    $scope.type = 'success';
    $scope.dataFileName = '';
    
    var fileId = $routeParams.fileId;

    if(angular.isDefined(fileId)) {
    	$scope.isImport = true;
    	$scope.dataFileName = fileId;

    	console.log('fileId: ', fileId);

    	dataservice.import(fileId).then(
    			function(payload) {
    				$location.path('/analyze');
    			},
    			function(error) {
    				if(error.status == 406) {
    					for(var i = 0; i < error.data.length; i++) {
    						uiDoc.errors.push(error.data[i]);
    					}
    				} else {
    					dialogs.notify('Validation failed ...', error.statusText)
    					.result.then(function(btn) {
    					});
    				}
    			},
    			function(progress) {
    				//console.log('progress: ', progress);
    			});
    }

  });
