var controllers = angular.module('app.controllers', [ 'app.services', 'ngRoute' ]);

controllers.controller('IndexCtrl', [ '$scope', '$http', '$routeParams', 'Status',
		function($scope, $http, $routeParams, Status) {
			$scope.status = Status.get();
			console.log("IndexCtrl loaded.");
		} ]);

controllers.controller('ServiceCtrl', [ '$scope', '$http', '$routeParams', 'Service',
		function($scope, $http, $routeParams, Service) {
			$scope.runningServices = Service.all();
			
			if ($routeParams.name) {
				$scope.selectedService = Service.get({ name : $routeParams.name });
			}
			
			console.log($scope.runningServices);
			console.log("ServiceCtrl loaded.");
		} ]);

console.log("Controllers loaded.");