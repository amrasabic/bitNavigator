/**
 * Created by ognjen on 14-Sep-15.
 */
$(function(){

    $(document).ready(function(){
        $('.rating-select .stars').trigger("mouseleave");
    });

    $('.rating-select .stars').on('mouseover', function(){
        $(this).removeClass('rate-not-selected').addClass('rate-selected');
        $(this).prevAll().removeClass('rate-not-selected').addClass('rate-selected');
        $(this).nextAll().removeClass('rate-selected').addClass('rate-not-selected');
    });

    $('.rating-select').on('mouseleave', function(){
        active = $(this).parent().find('.selected');
        if(active.length) {
            active.removeClass('rate-not-selected').addClass('rate-selected');
            active.prevAll().removeClass('rate-not-selected').addClass('rate-selected');
            active.nextAll().removeClass('rate-selected').addClass('rate-not-selected');
        } else {
            $(this).find('.stars').removeClass('rate-selected').addClass('rate-not-selected');
        }
    });

    $('.rating-select .stars').click(function(){
        if($(this).hasClass('selected')) {
            $('.rating-select .selected').removeClass('selected');
            $('#comment-rate').val("0");
        } else {
            $('.rating-select .selected').removeClass('selected');
            $(this).addClass('selected');
            var rate = $(this).data().rate;
            $('#comment-rate').val(rate);
        }
    });
});