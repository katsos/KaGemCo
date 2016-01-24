/* console-admin.js // Nikos Katsos // 22-01-16
 * 
 * Consle-admin handles all onclick functions from console-admin.html
 * and send requests to server using ajax methods.
 * All functions named with the prefix "on" correspondes to onclick functions
 * of html elements.
 * All other functions handles ajax requests-responses.
 *
 */
String.prototype.isEmpty = function() {
    
    return (this.length === 0 || !this.trim());
};

function getUsers() {

    var xmlhttp = new XMLHttpRequest();

    xmlhttp.onreadystatechange = function () {

        var response = xmlhttp.responseText;

        if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {

            document.getElementById("users").innerHTML += response ;

        }
    };

    var uri = "../GetUsersList";

    xmlhttp.open("POST", uri, true);
    xmlhttp.send();
    
}
function getCustomers() {

    var xmlhttp = new XMLHttpRequest();

    xmlhttp.onreadystatechange = function () {

        var response = xmlhttp.responseText;

        if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
            
            document.getElementById("customers").innerHTML += response ;
            
        }
    };

    var uri = "../GetCustomersList";

    xmlhttp.open("POST", uri, true);
    xmlhttp.send();
    
}
function getSalesmen() {

    var xmlhttp = new XMLHttpRequest();

    xmlhttp.onreadystatechange = function () {

        var response = xmlhttp.responseText;

        if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {

            document.getElementById("salesmen").innerHTML += response += "</table>";

        }
    };

    var uri = "../GetSalesmenList";

    xmlhttp.open("POST", uri, true);
    xmlhttp.send();
    
};
function getManagers() {

    var xmlhttp = new XMLHttpRequest();

    xmlhttp.onreadystatechange = function () {

        var response = xmlhttp.responseText;

        if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {

            document.getElementById("managers").innerHTML += response += "</table>";

        }
    };

    var uri = "../GetManagersList";

    xmlhttp.open("POST", uri, true);
    xmlhttp.send();
    
};

window.onload = function onLoad() {  
    getUsers();
    getCustomers();
    getSalesmen();
    getManagers();
};

/* * * Listeners * * */

function onDeleteUser( username ) {
    
    if ( username == "admin" ) {
        alert("Δεν είναι δυνατή η διαγραφή του χρήστη admin!");
        return false;
    }
    
    var answer = "Είστε σίγουρος πως θέλετε να διαγράψετε τον χρήστη " + username +";\n" 
               + "Μετά την διαγραφή του δεν θα είναι δυνατή η ανάκτηση των δεδομένων του. ";
    
    if (confirm(answer)) {
        deleteUser(username);
    }
    else window.location = "./console-admin.html";
}

function deleteUser( username ) {
    
    var xmlhttp = new XMLHttpRequest();

    xmlhttp.onreadystatechange = function () {

        var response = xmlhttp.responseText;

        if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
            
            console.log(response);
            
            if ( response == 'success' ) {
                alert("Η διαγραφή του χρήστη " + username + " πραγματοποιήθηκε με επιτυχία.");
                window.location = "./console-admin.html";
            } else {
                alert("Υπήρξε πρόβλημα με την διαγραφή του χρήστη " + username + ".\n" +
                      "Παρακαλώ επικοινωνήστε με τον διαχειριστή της βάσης δεδομένων.");
                window.location = "./console-admin.html";
            }
        }
    };

    var uri = "../DeleteUser?username="+username;

    xmlhttp.open("POST", uri, true);
    xmlhttp.send(); 
}

function onCustomers() {
    resetVisibleDivs();
    document.getElementById("customers-table").style.display = "inline";
}

function onSalesmen() {
    resetVisibleDivs();
    document.getElementById("salesmen-table").style.display = "inline";
}

function onManagers() {
    resetVisibleDivs();
    document.getElementById("managers-table").style.display = "inline";
}      

function onUsers() {
    resetVisibleDivs();
    document.getElementById("users-table").style.display = "inline";
}

function resetVisibleDivs() {
    
    /* Gather the name of all tables */
    var divs = [];
    divs.push("users-table");
    divs.push("customers-table");
    divs.push("managers-table");
    divs.push("salesmen-table");
    divs.push("new-user-form");
    
    /* For each table in the list, turn */
    for ( var i=0; i<divs.length; i++ ) {

        document.getElementById( divs[i] ).style.display = "none";

    } 
}

function onAddNewUser( role ) {
    resetVisibleDivs(); 
    if ( typeof role !== "undefined" )
        document.getElementById( "radio-"+role ).checked = true;
    document.getElementById( "new-user-form" ).style.display = "inline";
}

function onSubmitNewUser() {

    var report = newUserIsValid();

    if ( report == "Η αίτηση εγκρίθηκε." ) 
        addUser();

    document.getElementById("new-user-info").innerHTML = report;
}

function newUserIsValid() {
    
    // if no role is selected
    if ( !document.getElementById("radio-manager").checked && !document.getElementById("radio-salesman").checked )
        return "Δεν έχετε επιλέξει ρόλο.";
    
    // if there is no username value
    if ( document.getElementById("new-user-username").value.isEmpty())
        return "Δεν έχετε συμπληρώσει το ψευδώνυμο.";
    
    // if there is no password value
    if ( document.getElementById("new-user-password").value.isEmpty())
        return "Δεν έχετε συμπληρώσει τον κωδικό.";
    
    return "Η αίτηση εγκρίθηκε.";
}

function addUser() {

    var firstname = document.getElementById("new-user-firstname").value;
    var lastname = document.getElementById("new-user-lastname").value;
    var username = document.getElementById("new-user-username").value;
    var password = document.getElementById("new-user-password").value;
    var role;
    
    if ( document.getElementById("radio-manager").checked )
        role = "manager";
    else
        role = "salesman";

    var xmlhttp = new XMLHttpRequest();

    xmlhttp.onreadystatechange = function () {

        var response = xmlhttp.responseText;

        if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {

            if ( response == "added" ) 
                window.location = "./console-admin.html";
            else if ( response == "error" ) 
                document.getElementById("new-user-info").innerHTML 
                    = "Το ψευδώνυμο αυτό υπάρχει ήδη, παρακαλώ δοκιμάστε κάποιο διαφορετικό.";
            else 
                document.getElementById("new-user-info").innerHTML 
                    = "Υπήρξε κάποιο πρόβλημα κατά την καταχώρηση του χρήστη. Παρακαλω επικοινωνήστε με τον διαχειριστή του συστήματος.";
            
        }
    };

    var uri = "../AddUserA?" +"firstname="+firstname +"&lastname="+lastname +"&username="+username+"&password="+password+"&role="+role;

    xmlhttp.open("POST", uri, true);
    xmlhttp.send(); 
    
}

function onUpdateUser( username ) {
    
    
}

function updateUser() {
    
    
}

function onDeleteCustomer( firstname, lastname, taxID ) {
   
    var answer = "Είστε σίγουρος πως θέλετε να διαγράψετε τον πελάτη " + firstname + ' ' + lastname + ' ['+taxID+']' +";\n" 
               + "Μετά την διαγραφή του δεν θα είναι δυνατή η ανάκτηση των δεδομένων του. ";
    
    if (confirm(answer)) {
        deleteCustomer( firstname, lastname, taxID );
    }
    else window.location = "./console-admin.html";
    
}

function deleteCustomer( firstname, lastname, taxID ) {

    var xmlhttp = new XMLHttpRequest();

    xmlhttp.onreadystatechange = function () {

        var response = xmlhttp.responseText;

        if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {

            if ( response == "deleted" ) {
                alert("Η διαγραφή του χρήστη " + firstname + ' ' + lastname + ' ['+taxID+']' + " πραγματοποιήθηκε με επιτυχία.");
                window.location = "./console-admin.html";
            } else {
                alert("Υπήρξε πρόβλημα με την διαγραφή του χρήστη " + firstname + ' ' + lastname + ' ['+taxID+']' + ".\n" +
                      "Παρακαλώ επικοινωνήστε με τον διαχειριστή της βάσης δεδομένων.");
                window.location = "./console-admin.html";
            }
        }
    };

    var uri = "../DeleteCustomer?taxID="+taxID;

    xmlhttp.open("POST", uri, true);
    xmlhttp.send(); 
}