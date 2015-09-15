package controllers;

import models.Comment;
import models.Report;
import models.User;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * Created by ognje on 10-Sep-15.
 */
public class CommentController extends Controller {

    public Result reportComment() {
        DynamicForm form = Form.form().bindFromRequest();

        int commentId = -1;
        String email = form.data().get("email");
        try {
            commentId = Integer.parseInt(form.data().get("commentId"));
        } catch (Exception e) {
            return ok("error");
        }
        User user = User.findByEmail(email);
        Comment comment = Comment.findById(commentId);

        Report.addReport(comment, user);
        return ok("success");
    }


}
