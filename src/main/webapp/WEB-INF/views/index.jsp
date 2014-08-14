<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        
        <link href="http://192.168.56.1/TsvMenuExample/public_html/css/menu.css" type="text/css" rel="stylesheet">
        <script src="http://code.jquery.com/jquery-1.11.1.min.js" type="text/javascript"></script>
        <script src="http://192.168.56.1/TsvMenuExample/public_html/js/plugin/tsvMenuPlugin.js" type="text/javascript"></script>
        
        <script type="text/javascript"> 
            $(document).ready(function(){
              $('div.menu').tsvMenuPlugin({url:"/tsvMenu/menu/listdata"});
          }); 
        </script>
    </head>
    <body>
       <div class="menu"><ul  id="nav"> </ul></div>
    </body>
</html>
