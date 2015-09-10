package controllers;

import com.avaje.ebean.Ebean;
import models.User;
import play.data.Form;
import play.data.validation.Constraints;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import utillities.UserValidator;
import views.html.index;
import views.html.signin;
import views.html.signup;
import views.html.profile;
import play.Logger;
import utillities.PasswordHash;

import javax.persistence.Column;
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
    private static final Form<SignUpForm> signUpForm = Form.form(SignUpForm.class);

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
        return ok(signup.render(signUpForm));
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
            return badRequest(index.render());
        }
        try {
            if (!PasswordHash.validatePassword(boundForm.bindFromRequest().field(User.PASSWORD).value(), user.password)) {
                throw new IllegalArgumentException();
            }
        } catch (Exception e) {
            flash(ERROR_MESSAGE, "Email or password invalid!");
            return badRequest(index.render());
        }
        session().clear();
        session("email", user.email);
        return ok(index.render());
    }

    /**
     * Method sends all data from signup form to database and in that way
     * saves new user. This method use isValidEmailAddress method which checks
     * is form of entered email valid.
     * @return
     */
    public Result save() {
        Form<SignUpForm> boundForm = signUpForm.bindFromRequest();
        if(boundForm.hasErrors()) {
            flash(ERROR_MESSAGE, "Wrong input");
            return badRequest(signup.render(boundForm));
        }

        SignUpForm singUp = boundForm.get();
        if(User.findByEmail(singUp.email) != null) {
            flash(ERROR_MESSAGE, "Account already linked to given email!");
            return badRequest(signup.render(boundForm));
        }

        if (!singUp.password.equals(singUp.confirmPassword)) {
            flash(ERROR_MESSAGE, "Passwords do not match!");
            return badRequest(signup.render(boundForm));
        }

        try {
            singUp.password = PasswordHash.createHash(singUp.password);
        } catch (Exception e) {
            Logger.error("Could not create hash");
            flash(ERROR_MESSAGE, "Password invalid!");
            return badRequest(signup.render(boundForm));
        }

        User.newUser(singUp);
        session().clear();
        session("email", singUp.email);
        return ok(index.render());
    }

    public Result profile (String email) {
        final User user = User.findByEmail(email);
        if(user == null)
        {
            return notFound(String.format("User %s does not exist.", email));
        }
        Form <User> filledForm =  userForm.fill(user);
        return ok(profile.render(filledForm));

    }

    public Result updateUser(String email) {

        Form<User> boundForm = userForm.bindFromRequest();

        User user = User.findByEmail(boundForm.bindFromRequest().field("email").value());

        if(user == null)
        {
            return notFound(String.format("User %s does not exist.", email));
        }

        user.firstName = boundForm.bindFromRequest().field("firstName").value();
        user.lastName = boundForm.bindFromRequest().field("lastName").value();

        user.update();

        return ok(index.render());
    }

    public Result signOut() {
        session().clear();
        return redirect("/");
    }

    public static class SignUpForm  {
        @Constraints.Email
        @Constraints.Required
        public String email;
        @Constraints.Pattern ("[a-zA-Z]+")
        public String firstName;
        @Constraints.Pattern ("[a-zA-Z]+")
        public String lastName;
        @Constraints.MinLength (8)
        @Constraints.MaxLength (25)
        @Constraints.Required
        public String password;
        @Constraints.MinLength (8)
        @Constraints.MaxLength (25)
        @Constraints.Required
        public String confirmPassword;
    }

}
