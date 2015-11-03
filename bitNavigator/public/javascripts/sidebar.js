function hidePopover() {
    $(".mini-submenu").popover('hide');
}
$(function(){

    $(document).ready(function() {
        $(".mini-submenu").popover({
            placement: 'right',
            html: 'true',
            trigger: 'manual',
            title : '<span><strong>Nearby places</strong></span>'+
            '<button type="button" id="close" class="close" onclick=hidePopover()>&times;</button>',
            content : 'Click to see nearby places!'
        });
        $(".mini-submenu").popover('show');
    });



    $('#slide-submenu').on('click',function() {
        $(this).closest('.list-group').fadeOut('slide',function(){
            $('.mini-submenu').fadeIn();
        });

    });

    $('.mini-submenu').on('click',function(){
        hidePopover();
        $('.sidebar').children('.list-group').toggle('slide');
        $(this).hide();
    })
})
