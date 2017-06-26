'use strict';

angular.module('myApp.mainEditor', ['ngRoute',
  'ngMaterial'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/mainEditor/:ident', {
    templateUrl: 'mainEditor/mainEditor.html',
    controller: 'MainEditorCtrl'
  });
}])

.controller('MainEditorCtrl', ['$scope','$http','$location','hottyService',function($scope, $http,$location,hottyService){
  //Если урл не UUID
  if(hottyService.currentSiteJson != undefined){
    $scope.pages = hottyService.currentSiteJson.pages;
  } else {
    $location.path('/newProject/new');
  }
  $scope.deletePage = function (page) {
    hottyService.currentSiteJson.pages.splice( $.inArray(page, hottyService.currentSiteJson.pages), 1 );
  };
}]);