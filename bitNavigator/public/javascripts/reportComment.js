/**
 * Created by ognjen on 24-Sep-15.
 */
var reportedComment;
$(document).ready(function() {

    $('.report-comment-link').click(function() {
        reportedComment = this.parentNode.parentNode;
    });

    $('#report-comment-yes').click(function() {
        reportedComment.style.backgroundColor = "lightgrey";
        reportedComment.children[3].remove();

        $.ajax({
            type: "post",
            url: urlToReportComment,
            data: "commentId=" + $(reportedComment).data().comment
        }).error(function(response) {
            alert("Could not report comment!")
        });
    });

});