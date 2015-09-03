package controllers;

import com.avaje.ebean.Ebean;
import models.User;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.signin;
import views.html.signup;
import play.Logger;
import utillities.PasswordHash;
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
        Form<User> boundForm = userForm.bindFromRequest();
        User user = User.findByEmail(boundForm.bindFromRequest().field("email").value());
        if (user == null) {
            flash("error", "Wrong email");
            return badRequest(signin.render(boundForm));
        }
        try {
            if (!PasswordHash.validatePassword(boundForm.bindFromRequest().field("password").value(), user.password)) {
                throw new IllegalArgumentException();
            }
        } catch (Exception e) {
            flash("error", "Wrong password");
            return badRequest(signin.render(boundForm));
        }

        flash("error", "You are signed in");
        return ok(signin.render(userForm));
    }

    public Result save() {
        Form<User> boundForm = userForm.bindFromRequest();
        String email = boundForm.bindFromRequest().field("email").value();
        User user = User.findByEmail(boundForm.bindFromRequest().field("email").value());
        if (!isValidEmailAddress(email)) {
            flash("error", "Invalid email");
            return badRequest(signup.render(boundForm));
        }
        if (user != null) {
            flash("error", "Account already linked to given email");
            return badRequest(signup.render(boundForm));
        }
        user = new User();
        String password = boundForm.bindFromRequest().field("password").value();
        try {
            password = PasswordHash.createHash(password);
        } catch (Exception e) {
            e.printStackTrace();
        }

        user.username = boundForm.bindFromRequest().field("username").value();
        user.password = password;
        user.email = email;

        Ebean.save(user);
        return redirect(routes.Application.index());
    }

    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

}
