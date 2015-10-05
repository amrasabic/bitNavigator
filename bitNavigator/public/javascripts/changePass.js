/**
 * Created by hajrudin.sehic on 05/10/15.
 */

$(document).ready(function () {
    $("#old_password").blur(function () {
        var oldPass = document.getElementById("old_password").value;
       // var pass = "";
        $.ajax({
            url: "/checkPass",
            data: "oldPass=" + oldPass,
            type: "POST"
        }).success(function (response) {
            document.getElementById("passError").innerHTML = "";
        }).error(function (response) {
            document.getElementById("passError").innerHTML = "Wrong password";
        })
    })
});

function checkPasswords() {
    var password = document.getElementById("newPassword").value;
    var repassword = document.getElementById("confirmPassword").value;
    if (password.length < 8) {
        document.getElementById("rePass").innerHTML = "Password must be at least 8 characters long!";
        $('#submit-password').attr('disabled', 'disabled');
    } else if (password.length > 25) {
        document.getElementById("rePass").innerHTML = "Password must be at most 25 characters long!";
        $('#submit-password').attr('disabled', 'disabled');
    } else if (password != repassword) {
        document.getElementById("rePass").innerHTML = "Passwords must be same.";
        $('#submit-password').attr('disabled', 'disabled');
    } else {
        var valid = false;
        var c = '';
        for (var i = 0; i < password.length; i++) {
            c = password.charAt(i);
            if (c.toUpperCase() != c.toLowerCase()) {
                valid = true;
                break;
            }
        }
        if (!valid) {
            document.getElementById("rePass").innerHTML = "Password must contain at least one letter!";
            $('#submit-password').attr('disabled', 'disabled');
        } else {
            valid = false;
            var n = 0;
            for (var i = 0; i < password.length; i++) {
                n = password.charAt(i);
                console.log(n);
                if (!(isNaN(n))) {
                    valid = true;
                    break;
                }
            }
            if (!valid) {
                document.getElementById("rePass").innerHTML = "Password must contain at least one digit!";
                $('#submit-password').attr('disabled', 'disabled');
        } else {
                document.getElementById("rePass").innerHTML = "";
                $('#submit-password').removeAttr('disabled');
            }
        }
    }
}

