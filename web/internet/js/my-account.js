var cUsername; // username from cookie
var customer;

window.onload = function () {

    /* Get username from cookie */
    cUsername = searchForKey("username");

    /* Check if username isn't undefined */
    if (cUsername.length < 1 || cUsername == 'undefined' || cUsername == 'null')
        window.location = './login.html';

    /* Pull data for cookie's username */
    pullData();

    displayData();

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

function pullData() {

    $.ajax({
        type: 'POST',
        url: '../GetCustomer?username=' + cUsername,
        async: false,
        dataType: 'json',
        success:
                function (response) {
                    customer = response;
                }
    });

}

function displayData() {

    if (customer.error.length < 1) {

        $('#username')
                .text(customer.username);
        $('#credits')
                .text(customer.credits + ' €');
        $('#name')
                .text(customer.name);
        $('#surname')
                .text(customer.surname);
        $('#taxID')
                .text(customer.taxID);
        $('#bankAccount')
                .text(customer.bankAccount);

    }
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

function onDonation() {
    
    /* Get donation amount */
    var amount = $('#donation-amount').val();
    /* Print informational alert */
    alert('Tο ποσό των ' + amount +' € αφαιρέθηκε από τον τραπέζικό σας λογαριασμό και χάρισε χαμόγελα σε παιδιά που το έχουν ανάγκη.');
    /* Reset donation amount */
    $('#donation-amount').val('');
}

function onLogout() {
    
    document.cookie += '; Expires = Thu, 01-Jan-1970 00:00:01 GMT;';
    window.location = './login.html'
}