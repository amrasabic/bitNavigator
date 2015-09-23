/**
 * Created by Amra on 9/23/2015.
 */

function validateReservation() {
    var title = document.getElementById("rtitle").value;
    var description = document.getElementById("rdescription").value;

    var button = document.getElementById("reservation");

    button.disabled = true;

    if(title.length < 1 || description.length < 10) {
        button.disabled = true;
        return;
    }
    button.disabled = false;
}

function validateTitle() {
    var title = document.getElementById("rtitle").value;
    var par = document.getElementById("titleParagraph");
    var div = document.getElementById("titleDiv");
    var className = div.className;

    if(title.length < 1) {
        if(className == "form-group" ) {
            div.className = "form-group has-error";
            par.innerHTML = "This field is required.";
            return;
        }
        return;
    }
    div.className = "form-group";
    par.innerHTML = "";
}

function validateDescription() {
    var description = document.getElementById("rdescription").value;
    var par = document.getElementById("descParagraph");
    var div = document.getElementById("descriptionDiv");
    var className = div.className;

    if(description.length < 10) {
        if(className == "form-group" ) {
            div.className = "form-group has-error";
            par.innerHTML = "This field is required.";
            return;
        }
        return;
    }
    div.className = "form-group";
    par.innerHTML = "";
}