window.onload = function getUsers() {

    var xmlhttp = new XMLHttpRequest();

    xmlhttp.onreadystatechange = function () {

        response = xmlhttp.responseText;

        if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {

            document.getElementById("users").innerHTML += response;

        }
    };

    var uri = "http://localhost:8080/KaGemCo/GetUsersList";

    xmlhttp.open("POST", uri, true);
    xmlhttp.send();
    
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
    else window.location = "./console-admin.jsp";
}

function deleteUser( username ) {
    
    var xmlhttp = new XMLHttpRequest();

    xmlhttp.onreadystatechange = function () {

        response = xmlhttp.responseText;

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