'use strict';

angular.module('myApp.newProject', [
    'ngRoute',
    'ngAnimate',
    'ngMaterial',
    'ngCookies'
]).config(['$routeProvider', function($routeProvider) {
    $routeProvider.when('/newProject/:ident', {
    templateUrl: 'newProject/newProject.html',
    controller: 'NewProjectCtrl'
  });
}])

.controller('NewProjectCtrl', ['$scope', '$http','$location','$mdSelect','hottyService',function($scope, $http, $location,$mdSelect, hottyService) {
    generateTopMenu("span#topMenu");
    generateLeftMenu("span#leftMenu");
    generateRightMenu("span#rightMenu");
    generateHorizontalSlider("span#horizontalSlider");
    generateVerticalSlider("span#verticalSlider");
    generateEmpty("span#emptyPages");

    $scope.createTemplate = function (templateName) {
        var newprojectPopup = $( "<div title='Новый проект'></div>" ),
            newprojectForm = $('#newProjectForm').clone().removeClass('hidden');
        newprojectForm.find("input[name='name']").keyup(function (e) {
            var value = $(this).val(), result = value.replace(/[^а-яА-ЯЁA-Za-z0-9 ]/g, '');
            $(this).val(result);
            newprojectForm.find("input[name='keydomain']").val(hottyService.urlRusLat(result));
        });
        newprojectForm.find("input[name='keydomain']").keyup(function (e) {
            var value = $(this).val(), result = value.replace(/[^A-Za-z0-9-]/g, '');
            $(this).val(result);
        });
        newprojectPopup.append(newprojectForm);
        newprojectPopup.dialog({
            autoOpen: true,
            width: 400,
            buttons: [
                {
                    text: "Ok",
                    click: function() {
                        hottyService.currentSiteJson = createEmptyExampleJson(newprojectPopup);
                        $http({
                            method: "POST",
                            url: CONTEXT_PATH+"/api/v1/sites",
                            data: hottyService.currentSiteJson
                        }).then(function successCallback(resp) {
                            //Quite save site
                        }, function errorCallback(data) {
                            alert(data);
                        });
                        hottyService.relocationToConstructor(templateName);
                        $( this ).remove();
                    }
                },
                {
                    text: "Cancel",
                    click: function() {
                        $( this ).remove();
                    }
                }
            ]
        });

    }
}]);