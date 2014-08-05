'use strict';

/**
 * @ngdoc function
 * @name glassApp.controller:AnalyzeCtrl
 * @description
 * # AnalyzeCtrl
 * Controller of the glassApp
 */
angular.module('glassApp').controller('AnalyzeCtrl', function ($scope, dataservice) {
	
	$scope.showInitialSplash = true;

	// setup data
	dataservice.getUIFields().then(function(data) {
		$scope.headers = data.headers;
		$scope.selectedHeaders = [];
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
				fieldTwos.push($scope.headers[i]);
//				if (fieldOne) {
//					fieldTwo = $scope.headers[i];
//				} else {
//					fieldOne = $scope.headers[i];
//				}
			}
		}
		
		
		for (var i = 0; i < fieldTwos.length; i++) {
			
			var fieldTwo = fieldTwos[i];

			var header = '<div class="navbar navbar-default" role="navigation">' +
		  			 '	<div class="container-fluid">' +
		  			 '		<div class="navbar-header">' +
		  			 '			<a class="navbar-brand" href="#">Comparing '+fieldOne+' vs. '+fieldTwo+'</a>' +
		  			 '		</div>' +
		  			 '	</div>' +
		  			 '</div>';
		
			var progressBar = '<div class="progress">' +
		  				  '	 <div class="progress-bar progress-bar-striped active"  role="progressbar" aria-valuenow="100" aria-valuemin="0" aria-valuemax="100" style="width: 100%">' +
		  				  '	   <span class="sr-only"></span>' +
		  				  '  </div>' +
		  				  '</div>';
		
			
			
			
			$('#charts').prepend(header + '<div id="'+fieldOne+'-'+fieldTwo+'-compare">'+progressBar+'</div><div></div>');
		
			dataservice.compare(fieldOne, fieldTwo).then(function(data) {
				// set up the data for the chart
				if (data.compareType == 'NUMERIC_NUMERIC') {
					var chartDataValues = [];
					chartDataValues.push([data.fieldOne, data.fieldTwo]);
					for (var i = 0; i < data.points.length; i++) {
						var pair = data.points[i];
						var entry = [pair.x, pair.y];
						chartDataValues.push(entry);
					}
					var chartData = google.visualization.arrayToDataTable(chartDataValues);
					var options = {
							title: data.title,
							hAxis: {title: data.fieldOne},
							vAxis: {title: data.fieldTwo},
							legend: 'none',
							height: 400
					};
					var chart = new google.visualization.ScatterChart(document.getElementById(data.fieldOne+'-'+data.fieldTwo+'-compare'));
					chart.draw(chartData, options);
				} else if (data.compareType == 'LABEL_NUMERIC') {
					var chartData = new google.visualization.DataTable();
					chartData.addColumn('string', 'Entries');
					chartData.addColumn('number', 'Sum');
					chartData.addColumn('number', 'Count');
					chartData.addColumn('number', 'Average');
					chartData.addColumn('number', 'Median');
					
					var chartDataValues = [];
					for (var i = 0; i < data.pairs.length; i++) {
						var pair = data.pairs[i];
						var entry = [pair.label, pair.sum, pair.count, pair.average, pair.median];
						chartDataValues.push(entry);
					}
					chartData.addRows(chartDataValues);
					
					var options = {
						showRowNumber: true
					};
					
					var div = document.getElementById(data.fieldOne+'-'+data.fieldTwo+'-compare');
					if (div == null) {
						div = document.getElementById(data.fieldTwo+'-'+data.fieldOne+'-compare');
					}
					
					// Google table doesn't support title
	//				$(div).prepend('<div>'+data.title+'</div>');
					
					var table = new google.visualization.Table(div);
			        table.draw(chartData, options);
				} else if (data.compareType == 'LABEL_LABEL') {
					var chartData = new google.visualization.DataTable();
					chartData.addColumn('string', data.fieldOne);
					for (var i = 0; i < data.columnLabels.length; i++) {
						chartData.addColumn('number', data.columnLabels[i]);
					}
					
					var chartDataValues = [];
					for (var i = 0; i < data.rowLabels.length; i++) {
						var entry = [data.rowLabels[i]];
						for (var j = 0; j < data.columnLabels.length; j++) {
							entry.push(data.data[i][j]);
						}
						chartDataValues.push(entry);
					}
					chartData.addRows(chartDataValues);
					
					var div = document.getElementById(data.fieldOne+'-'+data.fieldTwo+'-compare');
					if (div == null) {
						div = document.getElementById(data.fieldTwo+'-'+data.fieldOne+'-compare');
					}
					
					var options = {
						showRowNumber: true
					};
					
					var table = new google.visualization.Table(div);
			        table.draw(chartData, options);
				}
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
			   
			   
			   $('#charts').prepend(headerBar+'<div id="'+header+'-compare">'+progressBar+'</div>');
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
