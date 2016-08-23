(function () {
    'use strict';

    angular
        .module('avivaApp')
        .config(materialDesignConfig);

    function materialDesignConfig() {
        // Initialize material design
        $.material.init();
    }
})();
