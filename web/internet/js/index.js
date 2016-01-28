var username;

window.onload = function () {

    username = searchForKey('username');
    
    /* Check username */
    if (username.length < 1) {
        $('#username').hide();
        $('#logout').hide();
    } else {
        $('#login').hide();
        $('#username a').text(username);
    }

};

function addCookie(key, value) {
    document.cookie += ';' + key + '=' + value + ';';
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

function onLogout() {
    expireAfterClosure();
    window.location = './index.html';
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

            if ( response == 'success' )
                alert('Η εγγραφή σας στο newsletter της KaGemCo πραγματοποιήθηκες με επιτυχία.');
            else
                alert('Η εγγραφή σας είναι αδύνατη αυτή τη στιγμή. Παρακαλώ δοκιμάστε αργότερα.');
        }
    };

    var uri = "../AddEmailToNewsletter?email="+email;

    xmlhttp.open("POST", uri, true);
    xmlhttp.send();
    
}

/* Scrolling-nav effect */ // requires jQuery Easing plugin
var heightOfToTop = 720;
var fadeDuration = 800;

$(function() {
    $('a.page-scroll').bind('click', function (event) {
        var $anchor = $(this);
        $('html, body').stop().animate({
            scrollTop: $($anchor.attr('href')).offset().top
        }, 1500, 'easeInOutExpo');
        event.preventDefault();
    });
});

/* */
$(window).scroll(function () {
    var scrolledPixels = $(window).scrollTop();

    if (scrolledPixels > heightOfToTop) {
        $('#to-top').fadeIn(fadeDuration);
    } else {
        $('#to-top').fadeOut(fadeDuration);
    }
});