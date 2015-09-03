package controllers;

import com.avaje.ebean.Ebean;
import models.User;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import utillities.UserValidator;
import views.html.signin;
import views.html.signup;
import play.Logger;
import utillities.PasswordHash;

import java.util.Calendar;

/**
 * Public class UserHandler extends Controller. Has methods within it that
 * leads random user to other subpages. Paths to subpages are defined in routes.
 * Created by ognje on 01-Sep-15.
 */
public class UserHandler extends Controller {

    private static final Form<User> userForm = Form.form(User.class);

    /**
     * Leads random user to signin subpage
     * @return
     */
    public Result signIn() {
        return ok(signin.render(userForm));
    }

    /**
     * Leads random user to signup page
     * @return
     */
    public Result signUp() {
        return ok(signup.render(userForm));
    }

    /**
     * Checks if all requirments are fullfiled to continue further. This method
     * gets email and password from database and compares them, if they match
     * user is logged in. Method use password hashing.
     * @return
     */
    public Result checkSignIn(){
        Form<User> boundForm = userForm.bindFromRequest();
        User user = User.findByEmail(boundForm.bindFromRequest().field("email").value());
        if (user == null) {
            flash("error", "Email or password invalid!");
            return badRequest(signin.render(boundForm));
        }
        try {
            if (!PasswordHash.validatePassword(boundForm.bindFromRequest().field("password").value(), user.password)) {
                throw new IllegalArgumentException();
            }
        } catch (Exception e) {
            flash("error", "Email or password invalid!");
            return badRequest(signin.render(boundForm));
        }

        flash("success", "You are signed in!");
        return ok(signin.render(userForm));
    }

    /**
     * Method sends all data from signup form to database and in that way
     * saves new user. This method use isValidEmailAddress method which checks
     * is form of entered email valid.
     * @return
     */
    public Result save() {
        Form<User> boundForm = userForm.bindFromRequest();

        String email = boundForm.bindFromRequest().field("email").value();
        if (!UserValidator.isEmailValid(email)) {
            flash("error", "Email format invalid!");
            return badRequest(signup.render(boundForm));
        }

        User user = User.findByEmail(boundForm.bindFromRequest().field("email").value());
        if (user != null) {
            flash("error", "Account already linked to given email!");
            return badRequest(signup.render(boundForm));
        }

        String password = boundForm.bindFromRequest().field("password").value();
        if (password.equals("null")) {
            flash("error", "Passwords do not match!");
            return badRequest(signup.render(boundForm));
        }

        if (UserValidator.isPasswordValid(password)[0].equals("error")) {
            flash(UserValidator.isPasswordValid(password)[0], UserValidator.isPasswordValid(password)[1]);
            return badRequest(signup.render(boundForm));
        }

        user = new User();

        try {
            password = PasswordHash.createHash(password);
        } catch (Exception e) {
            Logger.error("Could not create hash");
            flash("error", "Password invalid!");
            return badRequest(signup.render(boundForm));
        }

        user.firstName = boundForm.bindFromRequest().field("firstName").value();
        user.lastName = boundForm.bindFromRequest().field("lastName").value();
        user.password = password;
        user.email = email;
        user.accountCreated = Calendar.getInstance();

        Ebean.save(user);

        flash("success", "Account successfully created!");
        return ok(signup.render(userForm));
    }


}
