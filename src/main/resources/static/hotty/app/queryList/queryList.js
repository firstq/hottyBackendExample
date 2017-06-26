'use strict';

angular.module('myApp.queryList', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
    $routeProvider.when('/queryList/', {
        templateUrl: 'queryList/queryList.html',
        controller: 'QueryListCtrl'
    });
}])

.controller('QueryListCtrl', ['$scope','$http','$location','hottyService',function($scope,$http,$location,hottyService){
    $scope.isArchivateVisible = hottyService.queryType != "archive";
    $scope.showDetails = function (request) {
        hottyService.currentModel = request;
        $location.path('/queryDetails/'+request.id);
    }
    $scope.requests = [];
    $scope.createDate = hottyService.createDate;
    $http.get(CONTEXT_PATH+"/api/v1/adviceRequests"+(hottyService.queryType == "archive" ? "/archive" : "" ))
        .then(function (response) {
            for (var i = 0, len = response.data.length; i < len; i++) {
                var value = response.data[i];
                $scope.requests.push(value);
            }
    });
    $scope.archivate = function(model){
        model.isArchive = true;
        $http({
            method: "PUT",
            url: CONTEXT_PATH + "/api/v1/adviceRequests",
            data: model
        }).then(function successCallback(resp) {
            hottyService.showMessage("Сообщение отправлено в архив");
            hottyService.queryType = "archive";
            $location.path('/queryList');
        }, function errorCallback(data) {
            alert(data);
        });
    };
}]);