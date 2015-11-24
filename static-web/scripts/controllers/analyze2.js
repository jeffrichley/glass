'use strict';

/**
 * @ngdoc function
 * @name glassApp.controller:AnalyzeCtrl
 * @description
 * # AnalyzeCtrl
 * Controller of the glassApp
 */
angular.module('glassApp').controller('Analyze2Ctrl', function ($scope, GraphFactory, dataservice, chartservice) {
	
	$scope.COMPARE_ACTION = 'Compare';
	$scope.DESCRIBE_ACTION = 'Describe';
	
	/* the actual action - describe or compare */
	$scope.action = undefined;
	/* all fields with their types - e.g. {label:'Label', type:'NUMERIC'}*/
	$scope.fields = [];
	// all fields and their types split into 3 tiers - allows us to keep multi select functionality
	$scope.tieredFields = {
		tier1: [], tier2: [], tier3: []
	};

	/* the google charts - defined as activities */
	$scope.activities = [];
	
	var initTieredFields = function() {
		var length = $scope.fields.length;
		for (var i = 0; i < $scope.fields.length; i++) {
			if (i < (length / 3)) {
				$scope.tieredFields.tier1.push($scope.fields[i]);
			} else if (i < (2 * length / 3)) {
				$scope.tieredFields.tier2.push($scope.fields[i]);
			} else {
				$scope.tieredFields.tier3.push($scope.fields[i]);
			}
		}
	};
	
	$scope.primaryField = undefined;
	// ancillary fields isn't the right word here - the list of fields you can describe or compare the primary field against
	$scope.ancillaryFields = [];
	
	$scope.primaryFieldSelected = function(field) {
		$scope.primaryField = field;
	};
	
	$scope.toggleField = function(field) {
		toggle($scope.ancillaryFields, field);
	};
	
	$scope.fieldChecked = function(field) {
		return indexOf($scope.ancillaryFields, field) !== -1;
	};
	
 	// actions clicked
	$scope.describeClicked = function() {
		$scope.action = $scope.DESCRIBE_ACTION;
		$scope.ancillaryFields = []; // wipe out selected fields on click
	};
	
	$scope.compareClicked = function() {
		$scope.action = $scope.COMPARE_ACTION;
		$scope.ancillaryFields = []; // wipe out selected fields on click
	};
	
	dataservice.getUIFields().then(function(data) {
		$scope.fields = data.headers;
		initTieredFields();
	});
	
	$scope.plotClicked = function() {
		var field1 = $scope.selectedFields.field1;
		if ($scope.action === $scope.COMPARE_ACTION) {
			var field2 = $scope.selectedFields.field2;
			GraphFactory.getComparisonGraph(field1, field2);
		} else {
			GraphFactory.getDescriptionGraph(field1);
		}
	};
	
	/*
	 * Chirag - if the comparison or the description already exists - simply scroll down to the graph.
	 * might be worth doing.
	 */
	// for google charts
	$scope.visualizeClicked = function() {
		var field2s = [];
		// if it's a comparison we need to iterate through the ancillary fields and add them
		for (var i = 0; i < $scope.ancillaryFields.length; i++) {
			field2s.push($scope.ancillaryFields[i].label);
		}
		if ($scope.action === $scope.COMPARE_ACTION) {
			var field1 = $scope.primaryField.label;
			doCompare(field1, field2s);
		} else {
			doDescribe(field2s);
		}
	};
	
	
	var getComparisonActivity = function(primaryField, ancillaryField, uuid) {
		return {
			fieldOne: primaryField,
			fieldTwo: ancillaryField,
			title: primaryField + ' VS. ' + ancillaryField,
			id: uuid
		};
	};
	
	var getDescriptionActivity = function(field, uuid) {
		return {
			fieldOne: field,
			title: field,
			id: uuid
		};
	};
	
	// OLD CODE
	// actual chart activities
	
	var compareCallback = function(data) {
		var activity = data.clientActivity;
		activity.data = data;
		activity.correlation = data.correlation;
		var corr = Math.abs(data.correlation);
		var div = document.getElementById(activity.id);
		if (data.compareType == 'NUMERIC_NUMERIC') {
			activity.correlationMsg = dataservice.getPearsonMessage(corr);
			chartservice.drawScatterPlot(div, data);
		} else if (data.compareType == 'LABEL_NUMERIC') {
			activity.correlationMsg = dataservice.getPValueMessage(corr);
			chartservice.drawLabelNumericTable(div, data);
		} else if (data.compareType == 'LABEL_LABEL') {
			chartservice.drawLabelLabelTable(div, data);
		}
	};
	
	var doCompare = function(primaryField, ancillaryFields) {
		for (var i = 0; i < ancillaryFields.length; i++) {
			var ancillaryField = ancillaryFields[i];
			if (primaryField !== ancillaryField) {
				var uuid = dataservice.getUUID();
				var activity = getComparisonActivity(primaryField, ancillaryField, uuid);
				$scope.activities.unshift(activity);
				// now that we've built the activity - hit the server and populate the chart
				dataservice.compare(activity).then(compareCallback);
			}
		}
	};
	
	var describeCallback = function(data) {
		var activity = data.clientActivity;
		var chartDataValues = [[data.title, 'Values']];
		for (var i = 0; i < data.pairs.length; i++) {
			var pair = data.pairs[i];
			var entry = [pair.label, pair.value];
			chartDataValues.push(entry);
		}
		var chartdata = google.visualization.arrayToDataTable(chartDataValues);
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
            var div = document.getElementById(activity.id);
            chart = new google.visualization.BarChart(div);
		} else {
			options = {
				legend: 'none',
				title: data.title,
				hAxis: {title: data.title,  titleTextStyle: {color: 'red'}},
				height: 400
			};
			var div = document.getElementById(activity.id);
            chart = new google.visualization.BarChart(div);
		}
        chart.draw(chartdata, options);
	};
	
	var doDescribe = function(fields) {
		for (var i = 0; i < fields.length; i++) {
			var field = fields[i];
			var uuid = dataservice.getUUID();
			var activity = getDescriptionActivity(field, uuid);
			$scope.activities.unshift(activity);
			dataservice.describe(activity).then(describeCallback);
		}
	};
});
