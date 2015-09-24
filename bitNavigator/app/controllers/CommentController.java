package controllers;

import models.Comment;
import models.Report;
import models.User;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import utillities.Authenticators;
import utillities.SessionHelper;
import views.html.comments.commentslist;
import views.html.comments.reportedcommentslist;

import java.util.List;


/**
 * Created by ognje on 10-Sep-15.
 */
public class CommentController extends Controller {

    @Security.Authenticated(Authenticators.User.class)
    public Result reportComment() {
        DynamicForm form = Form.form().bindFromRequest();
        int commentId = -1;
        try {
            commentId = Integer.parseInt(form.data().get("commentId"));
        } catch (Exception e) {
            return badRequest("error");
        }
        User user = SessionHelper.getCurrentUser();
        Comment comment = Comment.findById(commentId);

        Report.addReport(comment, user);
        return ok("success");
    }

    @Security.Authenticated(Authenticators.Admin.class)
    public Result commentsList() {
        return ok(commentslist.render(Comment.findAll()));
    }

    @Security.Authenticated(Authenticators.Admin.class)
    public Result reportedComments() {
        List<Report.ReportHelper> list = Report.getAllReports();
        Logger.info(list.toString());
        return ok(reportedcommentslist.render(list));
    }

    @Security.Authenticated(Authenticators.Admin.class)
    public Result deleteComment(int id) {
        Comment comment = Comment.findById(id);
        if (comment == null) {
           return badRequest("Comment does not exist!");
        }
        comment.delete();
        return ok();
    }

}
