(function() {
    'use strict';
    angular
        .module('avivaApp')
        .factory('Insurer', Insurer);

    Insurer.$inject = ['$resource', 'DateUtils'];

    function Insurer ($resource, DateUtils) {
        var resourceUrl =  'api/insurers/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.dob = DateUtils.convertLocalDateFromServer(data.dob);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.dob = DateUtils.convertLocalDateToServer(data.dob);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.dob = DateUtils.convertLocalDateToServer(data.dob);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
