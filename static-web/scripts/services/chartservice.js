'use strict';

/**
 * @ngdoc service
 * @name statlabApp.chartservice
 * @description
 * # chartservice
 * Service in the statlabApp.
 */
angular.module('glassApp')
	.service('chartservice', function dataservice($http, $q, $timeout, $location, $window) {
		// AngularJS will instantiate a singleton by calling "new" on this function

		var svc = {};
		
		svc.drawLabelLabelTable = function(div, data) {
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
			
			var options = {
				showRowNumber: true
			};
			
			var table = new google.visualization.Table(div);
	        table.draw(chartData, options);
		};

		svc.drawLabelNumericTable = function(div, data) {
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
			
			var table = new google.visualization.Table(div);
	        table.draw(chartData, options);
		};
		
		svc.drawScatterPlot = function(div, data) {
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
					height: 400,
					trendlines: {
					      0: {
					        labelInLegend: 'Data Trend',
					        visibleInLegend: true,
					        color: 'red'
					      }
					    }
			};
			
			var chart = new google.visualization.ScatterChart(div);
			chart.draw(chartData, options);
		};
		
		return svc;
	});
