var cUsername; // username from cookie

/* Customer data */
var username;
var email;
var credits;
var name;
var surname;
var taxID;
var bankAccount;
/* /Customer data */

window.onload = function() {

    /* Get username from cookie */
    cUsername = searchForKey("username");
    
    /* Check if username isn't undefined */
    if ( cUsername.length < 1 || cUsername == 'undefined' || cUsername == 'null' )
        window.location = './login.html';
    
    /* Pull data for cookie's username */
    pullData();

    /* Display pulled data into table */
    displayData();

};

function displayData() {
    
    $('#username').text(username);
    $('#email').text(email);
    $('#credits').text(credits + '$');
    $('#name').text(name);
    $('#surname').text(surname);
    $('#taxID').text(taxID);
    $('#bankAccount').text(bankAccount);
    
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

function pullData() {
    
    var xmlhttp = new XMLHttpRequest();

    xmlhttp.onreadystatechange = function () {

        var response = xmlhttp.responseText;

        if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {

            // my code here

        }
    };

    var uri = "../GetCustomer?username="+cUsername;

    xmlhttp.open("POST", uri, true);
    xmlhttp.send();
    
}