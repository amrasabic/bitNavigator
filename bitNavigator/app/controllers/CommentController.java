package controllers;

import models.Comment;
import models.User;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * Created by ognje on 10-Sep-15.
 */
public class CommentController extends Controller {

    private static final Form<Comment> commentForm = Form.form(Comment.class);

}
