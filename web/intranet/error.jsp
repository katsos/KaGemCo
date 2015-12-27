<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Error Page</title>
        
        <style>
        	body {
			color: white;
			background-color: rgba( 0, 126, 128, 1);
                        text-align: center;
		}
		main {
			padding-top: 30px;
		
			width: 50%;
			min-width: 400px;
			max-width: 600px;
			margin: 0 auto;
		}
                a {
			color: white;
			font-size: 10pt;
			text-decoration: none; 
		}
		a:hover {
			font-weight: 500;
			text-decoration: underline;
		}
        </style>
            
    </head>
    <body>
        <h1>Error: <%= session.getAttribute("message") %></h1> 
        <br>
        <a href="./login.jsp"> Return to login page. </a>
    </body>
</html>
