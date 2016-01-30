var json;

window.onload = function () {

    pullRequests();
    showRequests();

};

function pullRequests() {

    $.ajax({
        type: 'POST',
        url: '../GetManagerRequests',
        async: false,
        dataType: 'json',
        success:
                function (response) {

                    if (response.error.length > 1) {
                        $('#requests').html('Υπήρξε κάποιο πρόβλημα με την ανάκτηση των εγγραφών.'
                                + 'Παρακαλώ ανανεώστε την σελίδα.');
                    } else {
                        json = response;
                    }

                }
    });
}

function showRequests() {

    var r = json.requests;

    for (var i = 0; i < r.length; i++) {

        row = '<tr>' +
                '<td>' + r[i].requestID + '</td>' +
                '<td>' + r[i].description + '</td>' +
                '<td>' + r[i].status + '</td>' +
                '<td>' +
                '<a href="" onclick="onAccept(' + r[i].requestID + ');return false;"> Αποδοχή </a>' +
                '</td>' +
                '<td>' +
                '<a href="" onclick="onDecline(' + r[i].requestID + ');return false;"> Απόρριψη </a> ' +
                '</td>' +
                '</tr>';

        $('#requests').append(row);
    }

}

function onAccept(requestID) {

    $.ajax({
        type: 'POST',
        url: '../HandleManagerRequest?requestID=' + requestID + '&status=accepted',
        dataType: 'json',
        success:
                function (response) {

                    if (response.error < 1) {
                        alert('Το αίτημα με αριθμό ' + requestID + ' εγκρίθηκε επιτυχώς.');
                        window.location = './console-manager.html';
                    } else
                        alert(response.error);

                }
    });

}

function onDecline(requestID) {

    $.ajax({
        type: 'POST',
        url: '../HandleManagerRequest?requestID=' + requestID + '&status=rejected',
        dataType: 'json',
        success:
                function (response) {

                    if (response.error < 1) {
                        alert('Το αίτημα με αριθμό ' + requestID + ' απορρίφθηκε επιτυχώς.');
                        window.location = './console-manager.html';
                    } else
                        alert(response.error);

                }
    });

}

function onLogout() {

    document.cookie += ';Expires = Thu, 01-Jan-1970 00:00:01 GMT;';
    window.location = './login.html';

}