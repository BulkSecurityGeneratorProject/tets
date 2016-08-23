(function() {
    'use strict';

    angular
        .module('avivaApp')
        .controller('InsurerDeleteController',InsurerDeleteController);

    InsurerDeleteController.$inject = ['$uibModalInstance', 'entity', 'Insurer'];

    function InsurerDeleteController($uibModalInstance, entity, Insurer) {
        var vm = this;

        vm.insurer = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Insurer.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
