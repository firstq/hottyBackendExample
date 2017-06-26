'use strict';

angular.module('myApp.queryDetails', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/queryDetails/:ident', {
    templateUrl: 'queryDetails/queryDetails.html',
    controller: 'QueryDetailsCtrl'
  });
}])

.controller('QueryDetailsCtrl', ['$scope','$http','$location','hottyService',function($scope,$http,$location,hottyService){
  $scope.isArchivateVisible = hottyService.queryType != "archive";
  $scope.currentRequest = hottyService.currentModel;
  $scope.createDate = hottyService.createDate;
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