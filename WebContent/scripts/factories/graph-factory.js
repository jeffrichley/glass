angular.module('glassApp')
.service('GraphFactory', function dataservice(GraphService) {

	var factory = {};
	
	factory.getDescriptionGraph = function(field) {
		
	};
	
	factory.getComparisonGraph = function(field1, field2) {
//		console.log('field1 = ', field1);
//		console.log('field2 = ', field2);
		GraphService.getComparison(field1, field2).then(function(graphData) {
//			console.log('graphData = ', graphData);
			var comparison = getComparison(graphData);
			
		});
	};
	
	var getComparison = function(graphData) {
		var compareType = graphData.compareType;
		if ('NUMERIC_NUMERIC' === compareType) {
			return getNumericNumericGraph(graphData);
		} else if ('LABEL_NUMERIC' === compareType) {
			return getLabelNumericGraph(graphData);
		} else {
			return getLabelLabelGraph(graphData);
		}
	};
	
	// some type of regression line at first
	var getNumericNumericGraph = function(graphData) {
		console.log('graph data = ', graphData);
		var points = graphData.points;
		// turn the data into a format that d3 can understand. 
		var data = [];
		for (var i = 0; i < points.length; i++) {
			var point = points[i];
			var row = {};
			row[graphData.fieldOne] = point.x;
			row[graphData.fieldTwo] = point.y;
			row.count = point.count;
			data.push(row);
		}
		
		// margins of the graph - aligns the axes against the graph content
		var margins = {
			top: 40,
			right: 60,
			bottom: 40,
			left: 100
		};
		// height of actual graph without the margins
		var graphWidth = 730 - (margins.right + margins.left);
		var graphHeight = 480 - (margins.top + margins.bottom);

		// the range of the actual data
		var xDomain = graphData.fieldOneRange;
		var yDomain = graphData.fieldTwoRange;
		
		// initialize the axes
		var x = d3.scale.linear()
				// actual physical dimension
				.range([0, graphWidth])
				// value range on the axis
				.domain([xDomain.minValue, xDomain.maxValue]).nice();
		var xAxis = d3.svg.axis().scale(x).orient('bottom');
		var xAxisName = graphData.fieldOne;
		
		// invert the y axis
		var y = d3.scale.linear()
				// actual physical dimension
				.range([graphHeight, 0])
				// value range on the axis
				.domain([yDomain.minValue, yDomain.maxValue]).nice();
		var yAxis = d3.svg.axis().scale(y).orient('left');
		var yAxisName = graphData.fieldTwo;
		
//		var line = d3.svg.line()
//	    		.x(function(d) { return x(d); })
//	    		.y(function(d) { return y(slope * ); });
		
		// initialize the graph
		var svg = d3.select('#graph').append('svg')
				.attr('width', graphWidth + (margins.right + margins.left))
				.attr('height', graphHeight + (margins.top + margins.bottom))
			.append('g')
				.classed('graph', true)
				.attr('transform', 'translate(' + margins.left + ',' + margins.top + ')');
		// since we have margins - translate the actual graph into the margins
//		svg.attr('transform', 'translate(' + margins.left + ',' + margins.top + ')');
		
		// add the g for x axis (i guess think of g's like the matrix stack in openGl)
		svg.append('g')
				.attr('class', 'xAxis').attr('transform', 'translate(0,' + graphHeight + ')')
				.call(xAxis)
				// add x axis label
				.append('text').attr('class', 'label').attr('x', graphWidth).attr('y', -6).style('text-anchor', 'end').text(xAxisName);
		// add the g for y axis
		svg.append('g')
				.attr('class', 'yAxis').call(yAxis)
				// add y axis label
				.append('text').attr('class', 'label').attr('transform', 'rotate(-90)').attr('y', 16).style('text-anchor', 'end').text(yAxisName);
		
		// create dots for every data point
		svg.selectAll('.dot')
				.data(data)
				.enter().append('circle')
					.attr('class', 'dot')
					// set radius
					.attr('r', 3.5)
					// set scaled x value (assuming this is center x of the circle)
					.attr('cx', function(d) { return x(d[graphData.fieldOne]); })
					// set scaled y value (assuming this is center y of the circle)
					.attr('cy', function(d) { return y(d[graphData.fieldTwo]); });
		
		
		
		// calculate the regression line
		var regressionData = graphData.regressionData;
		var intercept = regressionData.intercept;
		var slope = regressionData.slope;
		
		// regression line consists of 2 points - (minX, minY) - (maxX, maxY)
		var regressionLine = {
//				point1: {
//					x: 0,
//					y: (slope * 0) + intercept
//				},
			point1: {
				x: xDomain.minValue,
				y: (slope * xDomain.minValue) + intercept
			},
			point2: {
				x: xDomain.maxValue,
				y: (slope * xDomain.maxValue) + intercept
			}
		};
		
		console.log('x scale = ', x);
		console.log('y scale = ', y);
		console.log('regression data = ', regressionData);
		console.log('regression line = ', regressionLine);
		
		d3.select('svg g.graph')
			    .append('line')
			    .attr('id', 'bestfit');
		d3.select('#bestfit')
				.attr({
					'x1': x(regressionLine.point1.x),
					'y1': y(regressionLine.point1.y),
					'x2': x(regressionLine.point2.x),
					'y2': y(regressionLine.point2.y)})
				.style('opacity', 1);
	};
	
	// some type of bar chart 
	var getLabelNumericGraph = function(graphData) {
		
	};
	
	// some type of tabular data
	var getLabelLabelGraph = function(graphData) {
		
	};
	
	return factory;
});