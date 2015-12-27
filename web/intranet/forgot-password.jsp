<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <main>
            <h3> Επαναφορά Κωδικού </h3>
            <form action="../ForgotPassword" method="POST">
                <input id="username" type="text" name="username" placeholder="Ψευδώνυμο"> <br>
                <input id="reset" type="submit" name="reset" value="Επαναφορά">
                <p id="warning">{This is gonna get filled from javascript}</p>
            </form>
        </main>
    </body>
</html>
