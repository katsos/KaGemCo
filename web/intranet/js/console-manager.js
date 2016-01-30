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

                    if ( response.error.length > 1 ) {
                        $('#requests').html('Υπήρξε κάποιο πρόβλημα με την ανάκτηση των εγγραφών.'
                                                +'Παρακαλώ ανανεώστε την σελίδα.');
                    } else {
                        json = response;
                    }
                    
                }
    });
}

function showRequests() {
    
    var r = json.requests;

    for( var i=0; i<r.length; i++ ) {
        
        row = '<tr>' +
                '<td>' + r[i].requestID + '</td>' +
                '<td>' + r[i].description + '</td>' +
                '<td>' + r[i].status+ '</td>' +
                '<td>' + 
                    '<a href="" onclick="onAccept(' + r[i].requestID + ');return false;"> Αποδοχή </a>'+ 
                '</td>' +
                '<td>' + 
                    '<a href="" onclick="onDecline(' + r[i].requestID + ');return false;"> Απόρριψη </a> '+ 
                '</td>' +
              '</tr>';
        
        $('#requests').append(row);
    }
    
}

function onAccept( requestID ) {
    
//    $.ajax({
//        type: 'POST',
//        url: '../GetManagerRequests',
//        async: false,
//        dataType: 'json',
//        success:
//                function (response) {
//
//                    if ( response.error.length > 1 ) {
//                        $('#requests').html('Υπήρξε κάποιο πρόβλημα με την ανάκτηση των εγγραφών.'
//                                                +'Παρακαλώ ανανεώστε την σελίδα.');
//                    } else {
//                        json = response;
//                    }
//                    
//                }
//    });
    
}

function onDecline( requestID ) {
    
    
    
}

function onLogout() {

    document.cookie += ';Expires = Thu, 01-Jan-1970 00:00:01 GMT;';
    window.location = './login.html';

}