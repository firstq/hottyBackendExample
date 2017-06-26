'use strict';

angular.module('myApp.helloPage', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/hello', {
    templateUrl: 'helloPage/helloPage.html',
    controller: 'HelloPageCtrl'
  });
}])

.controller('HelloPageCtrl', ['$scope','$http','hottyService',function($scope, $http, hottyService){
    
}]);