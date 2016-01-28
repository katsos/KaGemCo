function onLogin() {

    var username = document.getElementById('username').value;
    var password = document.getElementById('password').value;

    var xmlhttp = new XMLHttpRequest();

    xmlhttp.onreadystatechange = function () {

        var response = xmlhttp.responseText;

        if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
            
            if ( response == 'success' ){
                document.cookie = 'username='+username+';';
                window.location = './my-account.html';
            } else
                alert('Μη αποδεκτά στοιχεία σύνδεσης.');
        }
    };

    var uri = '../CheckInternetLoginCredentials?username='+username+'&password='+password;

    xmlhttp.open("POST", uri, true);
    xmlhttp.send();
    
}
