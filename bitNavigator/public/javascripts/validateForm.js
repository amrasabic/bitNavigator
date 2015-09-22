/**
 * Created by ognjen on 21-Sep-15.
 */
$(document).ready(function(){

    $('form').submit(function(e, options){

        var options = options || {};
        if(options.allow == true){
            return;
        }

        e.preventDefault();
        $("[data-error-for]").html("");
        $("[data-error-for]").hide();
        $form = $(this);

        $.ajax({
            url: urlToPost,
            method: "post",
            data: $(this).serialize()
        }).success(function(response){
            $form.trigger("submit", {allow: true});
        }).error(function(response){

            var errors = response.responseJSON;
            var keys = Object.keys(errors);
            for(var i = 0; i < keys.length; i++){
                var errorMessages = errors[keys[i]];
                var allErrors = "";
                for(var j = 0; j < errorMessages.length; j++){
                    allErrors += errorMessages[j];
                }
                //set the helper span message to the content of the error messages
                $('[data-error-for="'+keys[i]+'"]').html(allErrors).show();
            }

        });
    });
});
