document.getElementById("scan").addEventListener("click", function() {
    $.get('AjaxServlet', {
        value : "scan"
    }, function(responseText) {
        if(responseText !== "-"){
            $(`#lastAction`).text("LAST ACTION : " + responseText);
        }
    });
});