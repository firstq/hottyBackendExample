
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Example</title>
        <meta charset="UTF-8">
        
        <link href="http://localhost:8383/hottyBlocks/css/menu.css" type="text/css" rel="stylesheet">
		<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.4.9/angular.min.js"></script>
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.2.2/jquery.min.js"></script>
		<link rel="stylesheet" href="https://ajax.googleapis.com/ajax/libs/jqueryui/1.11.4/themes/smoothness/jquery-ui.css">
		<script src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.11.4/jquery-ui.min.js"></script>
        <script src="http://localhost:8383/hottyBlocks/js/plugin/tsvMenuPlugin.js" type="text/javascript"></script>
        
        <script type="text/javascript"> 
            $(document).ready(function(){
              $('div.menu').tsvMenuPlugin({url:"http://localhost:8084/hottyBackendExample/rest/service/menuitems/6/"});
          }); 
        </script>
    </head>
    <body>
        <div class="menu"><ul  id="nav"> </ul></div>
    </body>
</html>
