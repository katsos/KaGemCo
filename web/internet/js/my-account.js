var cUsername; // username from cookie
var customerJson;

window.onload = function () {

    /* Get username from cookie */
    cUsername = searchForKey("username");

    /* Check if username isn't undefined */
    if (cUsername.length < 1 || cUsername == 'undefined' || cUsername == 'null')
        window.location = './login.html';

    pullUserData();
    
    displayUserData();

};

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

function pullUserData() {

    $.ajax({
        type: 'POST',
        url: '../GetCustomer?username=' + cUsername,
        async: false,
        dataType: 'json',
        success:
                function (response) {
                    customerJson = response;
                }
    });

}

function displayUserData() {
    
    if (customerJson.error.length < 1) {

        var c = customerJson.customer;

        $('#username')
                .text(c.username);
        $('#firstname')
                .text(c.firstname);
        $('#lastname')
                .text(c.lastname);
        $('#taxID')
                .text(c.taxID);
        $('#bankAccount')
                .text(c.bankAccountNo);

        var a = customerJson.accounts;
        var aTable = $('#accounts').append('<tbody>');
        
        for( var i=0; i<a.length; i++ ) {

            var account = '<tr>';
                account +=  '<th>' + a[i].phoneNumber + '</th>';
                account +=  '<td>' + a[i].balance + '€</td>';
                account +='</tr>';

            aTable.append(account);

        }

    }
    
    var numOfAccounts = a.length;
    
    if ( numOfAccounts > 6 )
        $('#new-number').hide();
}

function onRecharge() {

    var phoneNumber = $('#phone-number').val();
    var rechargeAmount = $('#amount').val();

    $.ajax({
        type: 'POST',
        url: '../Recharge?' + 'phoneNumber=' + phoneNumber + '&rechargeAmount=' + rechargeAmount,
        dataType: 'json',
        success:
                function (response) {

                    if (response.success == 'success') {

                        alert('Το ποσό των ' + rechargeAmount + '€ πιστώθηκε στον λογαριασμό σας.');
                        window.location = './my-account.html';
                        
                    } else {
                        alert('Η ανανέωση του χρόνου ομιλία σας δεν μπόρεσε να πραγματοποιηθεί.' + response.error);
                    }

                }
    });

}

function onNewNumber() {

    var taxID = customerJson.customer.taxID;

    $.ajax({
        type: 'POST',
        url: '../AddAccount?' + 'taxID='+taxID + '&balance=10',
        dataType: 'json',
        success:
                function (response) {
                    
                    if ( response.error.length < 1 )
                        alert('Είστε πλέον κάτοχος του αριθμού ' + response.phoneNumber 
                            + ' με πιστωμένα ' + response.balance + ' € χρόνου ομιλίας.' 
                            + 'Μπορείτε να περάσετε από οποιοδήποτε κατάστημά μας, να παραλάβετε την καινούργια κάρτα sim σας');
                 
                    window.location = './my-account.html';
                }
    });
    
}

function onLogout() {
    
    document.cookie += '; Expires = Thu, 01-Jan-1970 00:00:01 GMT;';
    window.location = './login.html';
    
}