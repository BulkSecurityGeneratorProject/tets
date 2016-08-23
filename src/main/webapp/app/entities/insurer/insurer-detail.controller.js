(function() {
    'use strict';

    angular
        .module('avivaApp')
        .controller('InsurerDetailController', InsurerDetailController);

    InsurerDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Insurer'];

    function InsurerDetailController($scope, $rootScope, $stateParams, entity, Insurer) {
        var vm = this;

        vm.insurer = entity;

        var unsubscribe = $rootScope.$on('avivaApp:insurerUpdate', function(event, result) {
            vm.insurer = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
