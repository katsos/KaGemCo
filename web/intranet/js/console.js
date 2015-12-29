function onLogout() {
    
    deleteCookie("username");
    window.location = "./login.html";
    
}

function deleteCookie( name ) {
  document.cookie = name + '=; expires=Thu, 01 Jan 1970 00:00:01 GMT;';
}