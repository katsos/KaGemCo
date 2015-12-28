<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>

        <!-- Meta -->
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="author" content="Nikos Katsos">

        <title>Welcome Admin</title>
        <link href="./css/console.css" rel="stylesheet">

        <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
        <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
        <!--[if lt IE 9]>
            <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
            <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
        <![endif]-->

    </head>
    <body id="admin-console">

        <header>
            <ul class="stats" style="display:none;">
                <li>Πελάτες: ${costumersCount} </li>
                <li>Αριθμοί: ${phoneNumbersCount}</li>
                <li>Πωλητές: ${salesmenCount}</li>
                <li>Χρήστες: ${signedUsersCount}</li>
            </ul>

            <ul class="options">
                <li><a class="button" href="#" onClick="onCustomers(); return false;"> Πελάτες </a></li>
                <li><a class="button" href="#" onClick="onSalesmen(); return false;"> Πωλητές </a></li>
                <li><a class="button" href="#" onClick="onManagers(); return false;"> Διευθυντές </a></li>        
                <li><a class="button" href="#" onClick="onUsers(); return false;"> Κατάλογος Χρηστών  </a></li>

                <li class="notif-li">
                    <a class="notif-button button" href=""> Eιδοποιήσεις </a></li>
                <li class="log-out-li">
                    <a class="log-out button" href="#" onClick="onLogout();return false;">  Αποσύνδεση  </a></li>
            </ul>    
        </header>

        <main>

            <div id="users-table">
                <table id="users">
                    <tr>
                        <th> Ψευδώνυμο </th>
                        <th> Ρόλος </th>
                        <th> - </th>
                        <th> X </th>
                    </tr>
                </table>
                <a class="add-new" id="add-new-user" href="#" > Προσθήκη νέας εγγραφής... </a>
            </div>
            <div id="customers-table" style="display: none;">
                <table id="customers">
                    <tr>
                        <th> Όνομα </th>
                        <th> Επώνυμο </th>
                        <th> Έτος Γέννησης </th>
                        <th> Φύλο </th>
                        <th> ΑΦΜ </th>
                        <th> Αρ. Τραπ. Λογαρ. </th>
                        <th> - </th>
                        <th> X </th>
                    </tr>
                </table>
                <a class="add-new" href="#" > Προσθήκη νέας εγγραφής... </a>
            </div>
            <div id="salesmen-table" style="display:none;">
                <table id="salesmen">
                    <tr>
                        <th> Username </th>
                        <th> Role </th>
                    </tr>
                </table>
                <a class="add-new" href="#" > Προσθήκη νέας εγγραφής... </a>
            </div>
            <div id="managers-table" style="display: none;">
                <table id="managers">
                    <tr>
                        <th> Όνομα </th>
                        <th> Επώνυμο </th>
                        <th> Έτος Γέννησης </th>
                        <th> Κατάστημα </th>
                    </tr>
                </table>
                <a class="add-new" href="#" > Προσθήκη νέας εγγραφής... </a>
            </div>

        </main>

        <footer></footer>

        <script src="./js/console.js"></script>
        <script src="./js/console-admin.js"></script>

    </body>
</html>