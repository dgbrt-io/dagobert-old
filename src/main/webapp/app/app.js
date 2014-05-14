var dagobert = angular.module('app', ['ngRoute', 'app.controllers', 'app.services']);

dagobert.config(['$routeProvider', function($routeProvider) {
	$routeProvider.when('/', {
		templateUrl: 'app/templates/index.html',
		controller: 'IndexCtrl'
	}).
	when('/services', {
		templateUrl: 'app/templates/services.html',
		controller: 'ServiceCtrl'
	}).
	when('/services/:name', {
		templateUrl: 'app/templates/services-detail.html',
		controller: 'ServiceCtrl'
	}).
	otherwise({
		redirectTo: '/'
	});
}]);

console.log("App loaded.")