'use strict';

/**
 * @ngdoc function
 * @name glassApp.controller:DatasetCtrl
 * @description
 * # DatasetCtrl
 * Controller of the glassApp
 */
//inject angular file upload directives and service.
angular.module('glassApp').controller('DatasetCtrl', function ($scope, $upload, $window, dataservice) {

	$scope.resultValid = false;
	$scope.pastedText = '';
	
	$scope.onFileSelect = function($files) {
	    //$files: an array of files selected, each file has name, size, and type.
	    for (var i = 0; i < $files.length; i++) {
	      var file = $files[i];
	      $scope.upload = $upload.upload({
	        url: 'fileupload',
	        method: 'POST',
	        data: {myObj: $scope.myModelObj},
	        file: file,
	        //formDataAppender: function(formData, key, val){}
	      }).progress(function(evt) {
			  if(window.console) {
				  console.log('percent: ' + parseInt(100.0 * evt.loaded / evt.total));
			  }
	      }).success(function(data, status, headers, config) {
	    	  // file is uploaded successfully
			  if(window.console) {
				  console.log(data);
			  }
			  $scope.resultValid = true;
			  $scope.datasetId = data;
	      });
	    }
	  };
	  
	  $scope.onSubmitText = function() {
		  if(window.console) {
			  console.log('onSubmitText()');
		  }
	  };
	  
	  $scope.onVisualize = function() {
		  if(window.console) {
			  console.log('onVisualize()');
		  }
		  var url = '/glass/#/' + $scope.datasetId;
		  $window.location.assign(url);
	  };
	  
});
