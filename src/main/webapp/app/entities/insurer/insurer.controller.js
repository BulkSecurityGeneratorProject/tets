(function() {
    'use strict';

    angular
        .module('avivaApp')
        .controller('InsurerController', InsurerController);

    InsurerController.$inject = ['$scope', '$state', 'Insurer'];

    function InsurerController ($scope, $state, Insurer) {
        var vm = this;
        
        vm.insurers = [];

        loadAll();

        function loadAll() {
            Insurer.query(function(result) {
                vm.insurers = result;
            });
        }
    }
})();
