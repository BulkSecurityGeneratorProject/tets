(function() {
    'use strict';

    angular
        .module('avivaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('insurer', {
            parent: 'entity',
            url: '/insurer',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Insurers'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/insurer/insurers.html',
                    controller: 'InsurerController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('insurer-detail', {
            parent: 'entity',
            url: '/insurer/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Insurer'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/insurer/insurer-detail.html',
                    controller: 'InsurerDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Insurer', function($stateParams, Insurer) {
                    return Insurer.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('insurer.new', {
            parent: 'insurer',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/insurer/insurer-dialog.html',
                    controller: 'InsurerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                title: null,
                                firstName: null,
                                lastName: null,
                                dob: null,
                                maritalStatus: null,
                                postCode: null,
                                emailId: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('insurer', null, { reload: true });
                }, function() {
                    $state.go('insurer');
                });
            }]
        })
        .state('insurer.edit', {
            parent: 'insurer',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/insurer/insurer-dialog.html',
                    controller: 'InsurerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Insurer', function(Insurer) {
                            return Insurer.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('insurer', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('insurer.delete', {
            parent: 'insurer',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/insurer/insurer-delete-dialog.html',
                    controller: 'InsurerDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Insurer', function(Insurer) {
                            return Insurer.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('insurer', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
