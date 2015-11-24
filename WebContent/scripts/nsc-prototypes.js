if (!Array.prototype.contains) {
	Array.prototype.contains = function(obj) {
		var i = this.length;
		while (i--) {
			if (this[i] === obj) {
				return true;
			}
		}
		return false;
	};
}


/*
 * http://stackoverflow.com/questions/5223/length-of-a-javascript-object-that-is-associative-array
 * 
 * "there's a convention that you don't add things to Object.prototype because it can break enumerations
 * in various libraries.  Adding methods to Object is usually safe, though.
 * 
 * http://stackoverflow.com/questions/126100/how-to-efficiently-count-the-number-of-keys-properties-of-an-object-in-javascrip
 * 
 * Object.keys is standard in any ES5-compatible environment - (Node, Chrome, IE9+, FF 4+, etc.). this
 * will leverage the keys functionality if it exists and fill it in if it doesn't.
 */
if (!Object.keys) {
	Object.keys = function(obj) {
		var keys = [];
		var key;
		for (key in obj) {
			if (obj.hasOwnProperty(key)) {
				keys.push(key);
			}
		}
		return keys;
	};
}