var getCodes = function(decodeObjects) {
	var codes = [];
	for (var i = 0; i < decodeObjects.length; i++) {
		codes.push(decodeObjects[i].code);
	}
	return codes;
};

var convertDate = function(date) {
	if (date) {
		var day = date.getDate().toString();
		if (String(day).length == 1) {
			day = "0" + day;
		}
		var month = Number(date.getMonth() + 1).toString(); // months are zero based
		if (String(month).length == 1) {
			month = "0" + month;
		}
		var year = date.getFullYear().toString();
		return year + '-' + month + '-' + day;
	}
	return undefined;
};

var toggle = function(array, element) {
	// use idx for contains - don't splice the array as you iterate through it
	var idx = indexOf(array, element);
	if (idx === -1) {
		array.push(element);
	} else {
		array.splice(idx, 1);
	}
};

var selectAll = function(array, selectArray) {
	for (var i = 0; i < selectArray.length; i++) {
		var element = selectArray[i];
		var idx = indexOf(array, element);
		if (idx === -1) {
			array.push(element);
		}
	}
};

var deselectAll = function(array, deselectArray) {
	for (var i = deselectArray.length - 1; i >= 0; i--) {
		var element = deselectArray[i];
		var idx = indexOf(array, element);
		if (idx !== -1) {
			array.splice(idx, 1);
		}
	}
};

var indexOf = function(array, element) {
	var idx = -1;
	for (var i = 0; i < array.length; i++) {
		if (array[i] === element) {
			idx = i;
		}
	}
	return idx;
};