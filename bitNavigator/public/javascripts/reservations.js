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