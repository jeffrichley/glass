'use strict';

/**
 * @ngdoc function
 * @name glassApp.controller:AnalyzeCtrl
 * @description
 * # AnalyzeCtrl
 * Controller of the glassApp
 */
angular.module('glassApp').controller('AnalyzeCtrl', function ($scope, dataservice, chartservice) {
	
	$scope.showInitialSplash = true;
	$scope.activities = [];

	// setup data
	dataservice.getUIFields().then(function(data) {
		$scope.headers = [];
		for (var i = 0; i < data.headers.length; i++) {
			$scope.headers.push(data.headers[i].label);
		}
		
//		$scope.headers = data.headers;
		// which buttons were selected
		$scope.selectedHeaders = [];
		// which button is the key to be compared to
		$scope.selectedKey = null;
		
		$scope.buttonStyles = [];
		for (var i = 0; i < $scope.headers.length; i++) {
			$scope.buttonStyles[i] = 'btn btn-lg btn-primary nsc-header-btn';
			$scope.selectedHeaders[i] = false;
		}
		
		$scope.$watchCollection('selectedHeaders', function(newValue, oldValue){
			for (var i = 0; i < newValue.length; i++) {
				if (newValue[i]) {
					$scope.buttonStyles[i] = 'btn btn-lg btn-info nsc-header-btn';
					if ($scope.selectedKey == null && newValue[i] != oldValue[i]) {
						$scope.selectedKey = i;
					}
				} else {
					$scope.buttonStyles[i] = 'btn btn-lg btn-primary nsc-header-btn';
					// if this was the key, we need to "unkey" it and make another key
					if (i == $scope.selectedKey) {
						$scope.selectedKey = null;
						for (var i = 0; i < $scope.selectedHeaders.length && $scope.selectedKey == null; i++) {
							if ($scope.selectedHeaders[i]) {
								$scope.selectedKey = i;
							}
						}
					}
				}
			} 
		});
	});

	// setup actions
	$scope.selectKey = function(index) {
		if ($scope.selectedKey == index) {
			$scope.selectedKey = null;
		} else {
			$scope.selectedKey = index;
			$scope.selectedHeaders[index] = true;
		}
	};
	
	// draw the charts that come back from the server 
	$scope.compare = function() {
		$scope.showInitialSplash = false;
		
		var fieldOne = $scope.headers[$scope.selectedKey];
		var fieldTwos = [];
		for (var i = 0; i < $scope.selectedHeaders.length; i++) {
			if ($scope.selectedHeaders[i] && i != $scope.selectedKey) {
				var uuid = dataservice.getUUID();
				fieldTwos.push(
					{
						fieldTwo:$scope.headers[i],
						uuid: uuid
					}
				);
				
				var activity = {
					fieldOne: fieldOne,
					fieldTwo: $scope.headers[i],
					title: 'Comparing ' + fieldOne + ' vs. ' + $scope.headers[i],
					data: null,
					correlation: null,
					id: uuid
				};
				
				$scope.activities.unshift(activity);
			}
		}
		
		
		for (var i = 0; i < fieldTwos.length; i++) {
			var fieldTwo = fieldTwos[i].fieldTwo;
			var uuid = fieldTwos[i].uuid;
		
			dataservice.compare(fieldOne, fieldTwo, uuid).then(function(data) {
				var activity = null;
				for (var i = 0; i < $scope.activities.length && activity == null; i++) {
					var tmpActivity = $scope.activities[i];
					if (tmpActivity.id == data.requestUUID) {
						activity = tmpActivity;
					}
				}
				activity.data = data;
				activity.correlation = data.correlation;
		
				var cor = Math.abs(data.correlation);
				var corMessage = null;
				
				var div = document.getElementById(data.requestUUID);
				
				if (data.compareType == 'NUMERIC_NUMERIC') {
					corMessage = dataservice.getPearsonMessage(cor);
					chartservice.drawScatterPlot(div, data);
				} else if (data.compareType == 'LABEL_NUMERIC') {
					corMessage = dataservice.getPValueMessage(cor);
					chartservice.drawLabelNumericTable(div, data);
				} else if (data.compareType == 'LABEL_LABEL') {
					chartservice.drawLabelLabelTable(div, data);
				}
				
				activity.correlationMsg = corMessage;
			});
		}
	};
	
	$scope.describe = function() {
		$scope.showInitialSplash = false;
		
		for (var i = 0; i < $scope.selectedHeaders.length; i++) {
		   if ($scope.selectedHeaders[i]) {
			   var header = $scope.headers[i];
			   // add the div at the top of the charts area
			   
			   var headerBar = '<div class="navbar navbar-default" role="navigation">' +
	  			 '	<div class="container-fluid">' +
	  			 '		<div class="navbar-header">' +
	  			 '			<a class="navbar-brand" href="#">Describing '+header+'</a>' +
	  			 '		</div>' +
	  			 '	</div>' +
	  			 '</div>';
			   
			   var progressBar = '<div class="progress">' +
				  				 '	 <div class="progress-bar progress-bar-striped active"  role="progressbar" aria-valuenow="100" aria-valuemin="0" aria-valuemax="100" style="width: 100%">' +
				  				 '	   <span class="sr-only"></span>' +
				  				 '  </div>' +
				  				 '</div>';
			   
			   
			   $('#charts').prepend(headerBar+'<div class="result-chart-area" id="'+header+'-compare">'+progressBar+'</div>');
			   dataservice.describe(header).then(function(data){
				   var chartdataValues = [[data.title, 'Values']];
				   
				   for (var i = 0; i < data.pairs.length; i++) {
					   var pair = data.pairs[i];
					   var entry = [pair.label, pair.value];
					   chartdataValues.push(entry);
				   }
				   var chartdata = google.visualization.arrayToDataTable(chartdataValues);
				   var chart = null;
				   var options = null;
				   // if less than 20 draw a bar chart, larger draws a column chart
				   if (data.pairs.length < 20) {
					   var height = data.pairs.length * 25;
					   if (height < 300) {
						   height = 300;
					   }
		               options = {
		            	 legend: 'none',
		                 title: data.title,
		                 vAxis: {title: data.title,  titleTextStyle: {color: 'red'}},
		                 height: height
		               };
		              chart = new google.visualization.BarChart(document.getElementById(data.title+'-compare'));
				   } else {
					   options = {
		            	 legend: 'none',
		                 title: data.title,
		                 hAxis: {title: data.title,  titleTextStyle: {color: 'red'}},
		                 height: 400
		               };
		              chart = new google.visualization.ColumnChart(document.getElementById(data.title+'-compare'));
				   }
				   
	               chart.draw(chartdata, options);
			   });
		   }
		}
	};
	
	$scope.$watch('selectedHeaders', function(newVal) {
		if (newVal != null) {
			var count = 0;
			for (var i = 0; i < newVal.length; i++) {
			   if (newVal[i]) {
				   count++;
			   }
			}
			
			if (count > 0) {
				$scope.numToDescribe = count;
			} else {
				$scope.numToDescribe = null;
			}
			
			if (count > 1) {
				$scope.numToCompare = count - 1;
			} else {
				$scope.numToCompare = null;
			}
		}
	}, true);
	
});
