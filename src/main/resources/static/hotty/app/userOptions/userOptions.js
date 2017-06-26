'use strict';

angular.module('myApp.userOptions', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/userOptions/:ident', {
    templateUrl: 'userOptions/userOptions.html',
    controller: 'UserOptionsCtrl'
  });
}])

.controller('UserOptionsCtrl', ['$scope','$http','$location','hottyService',function($scope,$http,$location,hottyService){
    $scope.currentLangPosition = 0;
    hottyService.currentModel.refreshScope = function () {
        $scope.$apply();
    }
    $scope.currentUser = hottyService.currentModel;

    $scope.saveCurrentUser = function () {
        $http({
            method: "PUT",
            url: CONTEXT_PATH+"/api/v1/users",
            data: $scope.currentUser
        }).then(function successCallback(resp) {
            hottyService.appCtrlScope.mainForSidenavList[3].clickFunction();
            hottyService.currentMenu = "";
            hottyService.showMessage("Изменения сохранены");
            $scope.currentUser.id = resp.data.id;
            $scope.currentUser.userLanguages = resp.data.userLanguages;
            $location.path('/userOptions/'+resp.data.id);
        }, function errorCallback(data) {
            alert(data);
        });
    }

    $scope.updateCurrentUser = function () {
        var suffix = (hottyService.currentModel.statusId == 1 ? "/block" : "/activate" );
        if($scope.currentUser.statusId == 1) {
            $scope.currentUser.statusId = 2
        } else $scope.currentUser.statusId = 1
        $scope.saveCurrentUser();
        /*$http({
         method: 'PUT',
         url: CONTEXT_PATH+"/api/v1/users/"+hottyService.currentModel.id+suffix
         }).then(function successCallback(resp) {
         hottyService.appCtrlScope.mainForSidenavList[3].clickFunction();
         hottyService.currentMenu = "";
         hottyService.showMessage("Изменения сохранены");
         if($scope.currentUser.statusId == 1) {
         $scope.currentUser.statusId = 2
         } else $scope.currentUser.statusId = 1
         //$location.path('/hello');
         }, function errorCallback(data) {
         alert(data);
         });*/
    }

    $scope.openPopup = hottyService.openPopup;
}]);