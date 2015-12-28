String.prototype.isEmpty = function () {
    return (this.length === 0 || !this.trim());
};

function onLogin() {

    if (formIsValid()) checkCredentials();

}

function formIsValid() {

    username = document.getElementById("username").value;
    password = document.getElementById("password").value;

    if (username.isEmpty() || password.isEmpty()) {
        document.getElementById("warning").innerHTML = "Παρακαλώ συμπληρώστε όλα τα πεδία";
        return false;
    } else {
        return true;
    }

}

function checkCredentials() {

    var xmlhttp = new XMLHttpRequest();

    xmlhttp.onreadystatechange = function () {

        if (xmlhttp.readyState === 4 && xmlhttp.status === 200) {

            response = xmlhttp.responseText;
            
            var username = getCookieValueByKey("username");
            var role = getCookieValueByKey("role");
            
            switch(role) {
                case "admin":
                    window.location = "./console-admin.jsp";
                    break;
                case "salesman":
                    window.location = "./console-salesman.jsp?username";
                    break;
                case "manager":
                    window.location = "./console-manager.jsp?username";
                    break;
                default:
                    document.getElementById("warning").innerHTML = "Λάθος στοιχεία σύνδεσης";
            }
        }

    };

    var uri = "http://localhost:8080/KaGemCo/CheckLoginCredentials?username=" + username + "&password=" + password;

    xmlhttp.open("POST", uri, true);
    xmlhttp.send();
}

function getCookieValueByKey( key ) {

    var pairs = document.cookie.split("; ");
    
    for ( var i=0; i<pairs.length; i++ ) {
        
        var splits = pairs[i].split('=');

        if ( splits[0] === key ) // if first part(key) is the requested
            return splits[1]; // return second part which is its value
    }
    
    return "not found";
}