
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html ng-app='myAppName'>
    <head>
        <title>Example</title>
        <meta charset="UTF-8">
        
        <link href="http://localhost:8383/hottyBlocks/css/menu.css" type="text/css" rel="stylesheet">
		<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.4.9/angular.min.js"></script>
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.2.2/jquery.min.js"></script>
		<link rel="stylesheet" href="https://ajax.googleapis.com/ajax/libs/jqueryui/1.11.4/themes/smoothness/jquery-ui.css">
		<script src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.11.4/jquery-ui.min.js"></script>
        <script src="http://localhost:8383/hottyBlocks/js/plugin/tsvMenuPlugin.js" type="text/javascript"></script>
		<script src="http://localhost:8084/hottyBackendExample/resources/js/blockPlugin.js" type="text/javascript"></script>
		<script src="http://localhost:8084/hottyBackendExample/resources/js/imagePlugin.js" type="text/javascript"></script>
		<script src="http://localhost:8084/hottyBackendExample/resources/js/textPlugin.js" type="text/javascript"></script>
		<script src="http://localhost:8084/hottyBackendExample/resources/js/videoPlugin.js" type="text/javascript"></script>
		<script src="http://localhost:8084/hottyBackendExample/resources/js/menuPlugin.js" type="text/javascript"></script>
        <script> 
//            $(document).ready(function(){
//              $('div.menu').tsvMenuPlugin({url:"http://localhost:8084/hottyBackendExample/rest/service/menuitems/6/"});
//          }); 
			//http://localhost:8084/hottyBackendExample/rest/service/blocks
			
			var App = angular.module('myAppName', []);
			App.directive('simpleBlock', function() {
				return {
					// Restrict it to be an attribute in this case
					restrict: 'A',
					// responsible for registering DOM listeners as well as updating the DOM
					link: function(scope, element, attrs) {
						$(element).blockPlugin(scope.$eval(attrs.simpleBlock));
					}
				};
			});
			
			App.controller('RepeatController', function($scope,$http) {
					$http.get("http://localhost:8084/hottyBackendExample/rest/service/blocks").success(function(data,status,headers,config){
						console.log(status);
						console.log(headers);
						console.log(config);
						$scope.blocks = data;
					 });
					 console.log($scope.blocks);
				});
			
        </script>
    </head>
    <body>
		<div style="height: 500px" ng-controller='RepeatController'>
			<div style="{{ block.styles }}" ng-repeat="block in blocks" simple-block='{}'> {{ block.blockType }} </div>
		</div>
    </body>
</html>
