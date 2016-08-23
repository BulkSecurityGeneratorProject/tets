(function() {
    'use strict';

    angular
        .module('avivaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('vehicle', {
            parent: 'entity',
            url: '/vehicle',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Vehicles'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/vehicle/vehicles.html',
                    controller: 'VehicleController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('vehicle-detail', {
            parent: 'entity',
            url: '/vehicle/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Vehicle'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/vehicle/vehicle-detail.html',
                    controller: 'VehicleDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Vehicle', function($stateParams, Vehicle) {
                    return Vehicle.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('vehicle.new', {
            parent: 'vehicle',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/vehicle/vehicle-dialog.html',
                    controller: 'VehicleDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                model: null,
                                company: null,
                                chasisNumber: null,
                                make: null,
                                crid: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('vehicle', null, { reload: true });
                }, function() {
                    $state.go('vehicle');
                });
            }]
        })
        .state('vehicle.edit', {
            parent: 'vehicle',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/vehicle/vehicle-dialog.html',
                    controller: 'VehicleDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Vehicle', function(Vehicle) {
                            return Vehicle.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('vehicle', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('vehicle.delete', {
            parent: 'vehicle',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/vehicle/vehicle-delete-dialog.html',
                    controller: 'VehicleDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Vehicle', function(Vehicle) {
                            return Vehicle.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('vehicle', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
