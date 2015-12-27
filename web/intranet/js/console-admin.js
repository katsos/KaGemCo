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
    
    if (confirm(answer)){
        console.log("RIP " + username );
        deleteUser(username);
    }
    else window.location = "./console-admin.jsp";
}

function deleteUser( username ) {
    
}

function onEditUser( username ) {
    
}