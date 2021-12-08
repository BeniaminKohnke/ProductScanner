setInterval(() => {
    $.get('AjaxServlet', {
        value : "log"
    }, function(responseText) {
        if(responseText !== "-"){
            let lines = responseText.split("|");
            for(let i = 0; i < lines.length; i++){
                let row = document.getElementById("logs").insertRow(0);
                let cell = row.insertCell(0);
                cell.innerHTML = lines[i];
            }
        }
    });
}, 100);