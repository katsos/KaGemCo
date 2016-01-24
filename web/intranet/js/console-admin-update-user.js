var username;
var response;

function onSearch() {

    username = document.getElementById('username').value;

    if (username.length < 1)
        alert('Παρακαλώ συμπληρώστε το ψευδώνυμο...');
    else
        search();
}

function search() {

    var xmlhttp = new XMLHttpRequest();

    xmlhttp.onreadystatechange = function () {

        var response = xmlhttp.responseText;

        if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {

            document.getElementsByTagName('table').innerHTML = response;

        }
    };

    var uri = "../GetUser?username=" + username;

    xmlhttp.open("POST", uri, true);
    xmlhttp.send();

}

function fillTable() {
    
}