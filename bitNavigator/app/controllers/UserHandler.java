package controllers;

import com.avaje.ebean.Ebean;
import models.User;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import utillities.PasswordHash;
import utillities.UserValidator;
import views.html.profile;
import views.html.signin;
import views.html.signup;

import java.util.Calendar;

/**
 * Public class UserHandler extends Controller. Has methods within it that
 * leads random user to other subpages. Paths to subpages are defined in routes.
 * Created by ognjen on 01-Sep-15.
 */
public class UserHandler extends Controller {

    public static final String ERROR_MESSAGE = "error";
    public static final String SUCCESS_MESSAGE = "success";

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
        User user = User.findByEmail(boundForm.bindFromRequest().field(User.EMAIL).value());
        if (user == null) {
            flash(ERROR_MESSAGE, "Email or password invalid!");
            return badRequest(signin.render(boundForm));
        }
        try {
            if (!PasswordHash.validatePassword(boundForm.bindFromRequest().field(User.PASSWORD).value(), user.password)) {
                throw new IllegalArgumentException();
            }
        } catch (Exception e) {
            flash(ERROR_MESSAGE, "Email or password invalid!");
            return badRequest(signin.render(boundForm));
        }
        session("email", user.email);
        flash(SUCCESS_MESSAGE, "You are signed in!");
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

        String email = boundForm.bindFromRequest().field(User.EMAIL).value();
        if (!UserValidator.isEmailValid(email)) {
            flash(ERROR_MESSAGE, "Email format invalid!");
            return badRequest(signup.render(boundForm));
        }

        String firstName = boundForm.bindFromRequest().field(User.FIRST_NAME).value();
        String lastName = boundForm.bindFromRequest().field(User.LAST_NAME).value();
        if (UserValidator.isNameValid(firstName, lastName)[0].equals(ERROR_MESSAGE)) {
            flash(UserValidator.isNameValid(firstName, lastName)[0], UserValidator.isNameValid(firstName, lastName)[1]);
            return badRequest(signup.render(boundForm));
        }

        User user = User.findByEmail(boundForm.bindFromRequest().field(User.EMAIL).value());
        if (user != null) {
            flash(ERROR_MESSAGE, "Account already linked to given email!");
            return badRequest(signup.render(boundForm));
        }

        String password = boundForm.bindFromRequest().field(User.PASSWORD).value();
        if (password.equals("null")) {
            flash(ERROR_MESSAGE, "Passwords do not match!");
            return badRequest(signup.render(boundForm));
        }

        if (UserValidator.isPasswordValid(password)[0].equals(ERROR_MESSAGE)) {
            flash(UserValidator.isPasswordValid(password)[0], UserValidator.isPasswordValid(password)[1]);
            return badRequest(signup.render(boundForm));
        }

        user = new User();
        try {
            password = PasswordHash.createHash(password);
        } catch (Exception e) {
            Logger.error("Could not create hash");
            flash(ERROR_MESSAGE, "Password invalid!");
            return badRequest(signup.render(boundForm));
        }

        user.firstName = firstName;
        user.lastName = lastName;
        user.password = password;
        user.email = email;
        user.accountCreated = Calendar.getInstance();

        Ebean.save(user);

        flash(SUCCESS_MESSAGE, "Account successfully created!");
        return ok(signup.render(userForm));
    }

    public Result signOut() {
        session().clear();
        return redirect("/");
    }


    public Result profile (Integer id) {
        final User user = User.findById(id);
        if(user == null)
        {
            return notFound(String.format("User %s does not exist.", id));
        }
        Form <User> filledForm =  userForm.fill(user);
        return ok(profile.render(filledForm));

    }
}
