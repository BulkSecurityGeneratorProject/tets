(function() {
    'use strict';

    angular
        .module('avivaApp')
        .controller('InsurerDialogController', InsurerDialogController);

    InsurerDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Insurer'];

    function InsurerDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Insurer) {
        var vm = this;

        vm.insurer = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.insurer.id !== null) {
                Insurer.update(vm.insurer, onSaveSuccess, onSaveError);
            } else {
                Insurer.save(vm.insurer, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('avivaApp:insurerUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.dob = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
