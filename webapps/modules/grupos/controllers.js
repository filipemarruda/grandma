'use strict';

gruposApp.controller('GrupoCtrl', ['$scope', '$cookies', '$stateParams', '$rootScope', '$location', 'Grupo',
	function($scope, $cookies, $stateParams, $rootScope , $location, Model) {

		$scope.moduleName = gruposApp.name;

		$scope.find = function(){
			$scope.itens = Model.query(
				{},
				function(response, headers){
					$cookies.auth_token = headers('auth_token');
				},
				function(error){
					$rootScope.errorHandle(error.status);
				}
			);
		};

		$scope.findOne = function(){
			$scope.item = Model.get(
				{id: $stateParams.id},
				function(response, headers){
					$cookies.auth_token = headers('auth_token');
				},
				function(error){
					$rootScope.errorHandle(error.status);
				}
			);
		};

		$scope.create = function() {
			var item = new Model({
				nome: this.nome
			});
			item.$save(
				function(response, headers) {
					$cookies.auth_token = headers('auth_token');
					$location.path( $scope.moduleName + '/list' );
				},
				function(error){
					$rootScope.errorHandle(error.status);
				}
			);
		};

		$scope.update = function() {
			var item = $scope.item;
			item.$update(
				function(response, headers) {
					$cookies.auth_token = headers('auth_token');
					$location.path( $scope.moduleName + '/list' );
				}, function(error) {
					$rootScope.errorHandle(error.status);
				}
			);
		};
		
		$scope.remove = function(id) {
			if (id) {
				var item = new Model({
					id: id
				});
				item.$remove(function(response, headers) {
					$cookies.auth_token = headers('auth_token');
				}, function(error) {
					$rootScope.errorHandle(error.status);
				});

				for (var i in $scope.itens) {
					if ($scope.itens[i].id === item.id) {
						$scope.itens.splice(i, 1);
					}
				}
			} else {
				$scope.item.$remove(function(response, headers) {
					$cookies.auth_token = headers('auth_token');
					$location.path( $scope.moduleName + '/list' );
				}, function(error) {
					$rootScope.errorHandle(error.status);
				});
			}
		};
	}]
);