

function resetVisibleDivs() {
    
    /* Gather the name of all tables */
    var divs = [];
    divs.push("register");
    divs.push("find");
    divs.push("update");
    divs.push("recharge");
    
    /* For each table in the list, turn */
    for ( var i=0; i<divs.length; i++ ) {

        document.getElementById( divs[i] ).style.display = "none";

    } 
}

function onNewCustomer() {
    resetVisibleDivs();
    document.getElementById("register").style.display = "inline";
}

function onFindCustomer() {
    resetVisibleDivs();
    document.getElementById("find").style.display = "inline";
}

function onUpdateCustomer() {
    resetVisibleDivs();
    document.getElementById("update").style.display = "inline";
}

function onRecharge() {
    resetVisibleDivs(); 
    document.getElementById("recharge").style.display = "inline";
}