window.onload = function () {

    var username = searchForKey("username");
    var login = document.getElementById('login');
    var logout = document.getElementById('logout');
    var username_li = document.getElementById('username');

    /* Check username */
    if (username == '') {
        username_li.style.display = 'none';
        logout.style.display = 'none';
    } else {
        login.style.display = 'none';
        updateMyAccountAnchor(username, username_li);
    }

}

function updateMyAccountAnchor(username, username_li) {

    anchors = username_li.getElementsByTagName('a');
    username_a = anchors[0];
    username_a.href = "./my-account.html?username=" + username;
    username_a.innerHTML = username;

}

function clearCookie() {
    document.cookie = '';
}

function addCookie(key, value) {
    document.cookie += key + '=' + value + ';';
}
function expireAfterClosure() {
    addCookie('Expires', 'Thu, 01-Jan-1970 00:00:01 GMT');
}
function searchForKey(givenKey) {
    // remove all spaces
    document.cookie = document.cookie.replace(/ /g, '');
    // split each pair
    var cookies = document.cookie.split(';');

    /* for each pair */
    for (var i = 0; i < cookies.length; i++) {
        var key_value = cookies[i].split('=');

        var key = key_value[0];
        var value = key_value[1];

        if (key == givenKey)
            return value;
    }

    return '';
}

function onSignupNewsletter() {
    
    var email = document.getElementById('email-newsletter').value;
    
    if ( email.length < 1 ) {
        alert('Παρακαλώ συμπληρώστε την διεύθυνση email σας.');
        return;
    }
    
    var xmlhttp = new XMLHttpRequest();

    xmlhttp.onreadystatechange = function () {

        var response = xmlhttp.responseText;

        if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {

            alert('Η εγγραφή σας στο newsletter της KaGemCo πραγματοποιήθηκες με επιτυχία.');

        }
    };

    var uri = "../AddEmailToNewsletter?email="+email;

    xmlhttp.open("POST", uri, true);
    xmlhttp.send();
    
}