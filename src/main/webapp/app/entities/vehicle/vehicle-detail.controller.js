(function() {
    'use strict';

    angular
        .module('avivaApp')
        .controller('VehicleDetailController', VehicleDetailController);

    VehicleDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Vehicle'];

    function VehicleDetailController($scope, $rootScope, $stateParams, entity, Vehicle) {
        var vm = this;

        vm.vehicle = entity;

        var unsubscribe = $rootScope.$on('avivaApp:vehicleUpdate', function(event, result) {
            vm.vehicle = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
