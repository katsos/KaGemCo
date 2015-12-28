String.prototype.isEmpty = function () {
    return (this.length === 0 || !this.trim());
};

/** OnLogin **/

function onLogin() {

    if (formIsValid()) sendAjax();

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

function sendAjax() {

    var xmlhttp = new XMLHttpRequest();

    xmlhttp.onreadystatechange = function () {

        if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {

            response = xmlhttp.responseText;

            if (response == "admin") {
                window.location = "./console-admin.jsp";
            } else if (response.startsWith("sm")) {
                window.location = "./console-salesman.jsp";
            } else if (response.startsWith("man")) {
                window.location = "./console-manager.jsp";
            } else {
                document.getElementById("warning").innerHTML = "Λάθος στοιχεία σύνδεσης";
            }
        }

    };

    var uri = "http://localhost:8080/KaGemCo/CheckLoginCredentials?username=" + username + "&password=" + password;

    xmlhttp.open("POST", uri, true);
    xmlhttp.send();
}