(function() {
    'use strict';

    angular
        .module('avivaApp')
        .controller('VehicleController', VehicleController);

    VehicleController.$inject = ['$scope', '$state', 'Vehicle'];

    function VehicleController ($scope, $state, Vehicle) {
        var vm = this;
        
        vm.vehicles = [];

        loadAll();

        function loadAll() {
            Vehicle.query(function(result) {
                vm.vehicles = result;
            });
        }
    }
})();
