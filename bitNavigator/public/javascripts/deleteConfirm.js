/**
 * Published by benjamin.
 */
$('body').on('click', 'a[data-role="delete"]', function(e){
    e.preventDefault();
    $toDelete = $(this);
    var conf = bootbox.confirm("Delete?", function(result){
        if(result == true){
            $.ajax({
                url: $toDelete.attr("href"),
                type: "delete"
            }).success(function(response){
                $toDelete.parents($toDelete.attr("data-delete-parent")).remove();
            });
        }
    });


});