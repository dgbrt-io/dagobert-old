

var services = angular.module('app.services', ['ngResource']);

services.factory('Status', ['$resource', function($resource) {
	return $resource('rest', {}, {
		get: {
			method: 'GET',
	        params: {},
	        isArray: false
	    }
	});
}]);

services.factory('Service', ['$resource', function($resource) {
	return $resource('rest/services/:name', {}, {
		get: {
			method: 'GET',
			params: { name: "@name"},
			isArray: false
		},
		all: {
			method: 'GET',
			params: {},
			isArray: true
		}
	});
}]);
console.log("Services loaded.");