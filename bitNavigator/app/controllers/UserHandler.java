package controllers;

import com.avaje.ebean.Ebean;
import models.User;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.signin;
import views.html.signup;

/**
 * Created by ognje on 01-Sep-15.
 */
public class UserHandler extends Controller {

    private static final Form<User> userForm = Form.form(User.class);

    public Result signIn() {
        return ok(signin.render(userForm));
    }

    public Result signUp() {
        return ok(signup.render(userForm));
    }

    public Result checkSignIn(){
        return ok();
    }

    public Result save() {
        Form<User> boundForm = userForm.bindFromRequest();
        User user = new User();
        user.username = boundForm.bindFromRequest().field("username").value();
        user.password = boundForm.bindFromRequest().field("password").value();
        user.email = boundForm.bindFromRequest().field("email").value();

        Ebean.save(user);
        return redirect(routes.Application.index());
    }

}
