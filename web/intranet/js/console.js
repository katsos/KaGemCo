function onLogout() {
    
    deleteCookie("username");
    //window.location( "http://localhost:8080/KaGemCo/intranet/login.jsp" );
    window.location = "./login.jsp";
    
}

function deleteCookie( name ) {
  document.cookie = name + '=; expires=Thu, 01 Jan 1970 00:00:01 GMT;';
}