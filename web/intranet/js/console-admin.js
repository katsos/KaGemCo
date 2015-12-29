String.prototype.isEmpty = function () {
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

    var uri = "http://localhost:8080/KaGemCo/GetUsersList";

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

    var uri = "http://localhost:8080/KaGemCo/GetCustomersList";

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

    var uri = "http://localhost:8080/KaGemCo/GetSalesmenList";

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

    var uri = "http://localhost:8080/KaGemCo/GetManagersList";

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
/* Every function with on as prefix correspond to onClick listerners of gui. */

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
    else window.location = "./console-admin.jsp";
}

function deleteUser( username ) {
    
    var xmlhttp = new XMLHttpRequest();

    xmlhttp.onreadystatechange = function () {

        var response = xmlhttp.responseText;

        if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {

            console.log("DEB: " + response);

            if ( response == username ) {
                alert("Η διαγραφή του χρήστη " + username + " πραγματοποιήθηκε με επιτυχία.");
                window.location = "./console-admin.jsp";
            } else {
                alert("Υπήρξε πρόβλημα με την διαγραφή του χρήστη " + username + ".\n" +
                      "Παρακαλώ επικοινωνήστε με τον διαχειριστή της βάσης δεδομένων.");
                window.location = "./console-admin.jsp";
            }
        }
    };

    var uri = "http://localhost:8080/KaGemCo/DeleteUser?username="+username;

    xmlhttp.open("POST", uri, true);
    xmlhttp.send(); 
}

function onEditUser( username ) {
    
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
        ajaxAddUser();

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

function ajaxAddUser() {

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
                window.location = "http://localhost:8080/KaGemCo/intranet/console-admin.jsp";
            else if ( response == "error" ) 
                document.getElementById("new-user-info").innerHTML 
                    = "Το ψευδώνυμο αυτό υπάρχει ήδη, παρακαλώ δοκιμάστε κάποιο διαφορετικό.";
            else 
                document.getElementById("new-user-info").innerHTML 
                    = "Υπήρξε κάποιο πρόβλημα κατά την καταχώρηση του χρήστη. Παρακαλω επικοινωνήστε με τον διαχειριστή του συστήματος.";
            
        }
    };

    var uri = "http://localhost:8080/KaGemCo/AddUserA?"
                    +"username="+username+"&password="+password+"&role="+role;

    xmlhttp.open("POST", uri, true);
    xmlhttp.send(); 
    
}