'use strict';

angular.module('myApp.toast', ['ngRoute',
    'ngAnimate',
    'ngMaterial',
    'ngCookies',
    'ngScrollbar'])

.controller('ToastCtrl', ['$scope','$http','hottyService', '$mdToast' ,function($scope, $http ,hottyService, $mdToast){
    $scope.message = hottyService.curMessage;
}]);