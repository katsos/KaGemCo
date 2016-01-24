function onLogout() {

    deleteCookie();
    window.location = './login.html';

}

function deleteCookie() {
    document.cookie = '';
}