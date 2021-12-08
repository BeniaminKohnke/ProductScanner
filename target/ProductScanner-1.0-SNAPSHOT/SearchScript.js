document.getElementById("search").addEventListener("click", function() {
    $.get('AjaxServlet', {
        value : "search"
    }, function(responseText) {
        if(responseText !== "-"){
            $(`#lastAction`).text("LAST ACTION : " + responseText);
        }
    });
});