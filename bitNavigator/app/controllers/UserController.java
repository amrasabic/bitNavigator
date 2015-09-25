package controllers;

import models.*;
import play.data.Form;
import play.data.validation.Constraints;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Http;

import play.mvc.Security;
import utillities.Authenticators;
import utillities.SessionHelper;
import views.html.user.*;
import views.html.admin.*;
import play.Logger;
import utillities.PasswordHash;

import java.io.File;

import java.util.Calendar;


/**
 * Public class UserHandler extends Controller. Has methods within it that
 * leads random user to other subpages. Paths to subpages are defined in routes.
 * Created by ognjen on 01-Sep-15.
 */
public class UserController extends Controller {

    public static final String ERROR_MESSAGE = "error";
    public static final String SUCCESS_MESSAGE = "success";

    private static final Form<User> userForm = Form.form(User.class);
    private static final Form<SignUpForm> signUpForm = Form.form(SignUpForm.class);
    private static final Form<UserNameForm> userNameForm = Form.form(UserNameForm.class);
  //  private static Image image;

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
            return redirect(routes.Application.index());
        }
        try {
            if (!PasswordHash.validatePassword(boundForm.bindFromRequest().field(User.PASSWORD).value(), user.password)) {
                throw new IllegalArgumentException();
            }
        } catch (Exception e) {
            flash(ERROR_MESSAGE, "Email or password invalid!");
            return redirect(routes.Application.index());
        }
        session().clear();
        session("email", user.email);
        return redirect(routes.Application.index());
    }

    /**
     * Method sends all data from signup form to database and in that way
     * saves new user. This method use isValidEmailAddress method which checks
     * is form of entered email valid.
     * @return
     */
    public Result save() {
        Form<SignUpForm> boundForm = signUpForm.bindFromRequest();

        SignUpForm singUp = boundForm.get();
        if(User.findByEmail(singUp.email) != null) {
            flash(ERROR_MESSAGE, "Account already linked to given email!");
            return badRequest(signup.render(boundForm));
        }

        if (!singUp.password.equals(singUp.confirmPassword)) {
            flash(ERROR_MESSAGE, "Passwords do not match!");
            return badRequest(signup.render(boundForm));
        }

        User.newUser(singUp);
        session().clear();
        session("email", singUp.email);
        return redirect(routes.Application.index());
    }

    @Security.Authenticated(Authenticators.User.class)
    public Result profile (String email) {
        final User user = User.findByEmail(email);
        if(user == null) {
            return notFound(String.format("User %s does not exist.", email));
        }

        return ok(profile.render(new UserNameForm(user)));

    }

    @Security.Authenticated(Authenticators.User.class)
    public Result updateUser() {

        Form<UserNameForm> boundForm = Form.form(UserNameForm.class).bindFromRequest();

        User user = SessionHelper.getCurrentUser();

        if(user == null) {
            return notFound(String.format("User does not exist."));
        }

        if(!user.email.equals(boundForm.bindFromRequest().field("email").value())){
            return unauthorized("We good we good");
        }

        if(boundForm.hasErrors()) {
            flash("error", "Name can only hold letters!");
            return redirect(routes.UserController.profile(user.email));
        }
        user.firstName = boundForm.bindFromRequest().field("firstName").value();
        user.lastName = boundForm.bindFromRequest().field("lastName").value();
        user.phoneNumber = boundForm.bindFromRequest().field("mobileNumber").value();

        user = User.updateUser(boundForm.get());

        Http.MultipartFormData body = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart filePart = body.getFile("image");

        if(filePart != null){
            Logger.debug("Content type: " + filePart.getContentType());
            Logger.debug("Key: " + filePart.getKey());
            File file = filePart.getFile();
            Image image = Image.create(file);
            user.avatar = image;
            image.save();

        }

        user.update();
        return redirect(routes.Application.index());
    }

    @Security.Authenticated(Authenticators.Admin.class)
    public Result userList(){
        return ok(userlist.render(User.findAll()));
    }

    @Security.Authenticated(Authenticators.Admin.class)
    public Result delete(String email) {
        User user = User.findByEmail(email);
        if (user == null) {
            return notFound(String.format("User %s does not exists.", email));
        }
        for (Comment comment : Comment.findByUser(user)) {
            comment.delete();
        }
        for (Report report : Report.findByUser(user)) {
            report.delete();
        }
        for (Place place : Place.findByUser(user)) {
            place.delete();
        }
        for (Reservation reservation : Reservation.findByUser(user)) {
            reservation.delete();
        }
        user.delete();
        return redirect(routes.UserController.userList());
    }

    @Security.Authenticated(Authenticators.Admin.class)
    public Result adminView() {
        User admin = User.findByEmail(session("email"));
        if(admin == null || !admin.admin){
            return unauthorized("Permission denied!");
        }
        return ok(adminview.render());
    }

    public Result signOut() {
        session().clear();
        return redirect("/");
    }

    public static class UserNameForm {
        @Constraints.Email
        @Constraints.Required
        public String email;
        @Constraints.Pattern (value = "[a-zA-Z]+", message = "First name can only contain alphabetic characters")
        public String firstName;
        @Constraints.Pattern (value = "[a-zA-Z]+", message = "Last name can only contain alphabetic characters")
        public String lastName;
        @Constraints.Pattern (value = "^\\+387[3,6][1-6]\\d{6}", message = "Enter valid number")
        public String mobileNumber;
        public Image avatar;

        public UserNameForm() {

        }

        public UserNameForm(User u) {
            this.email = u.email;
            this.firstName = u.firstName;
            this.lastName = u.lastName;
            this.mobileNumber = u.phoneNumber;
            this.avatar = u.avatar;
        }
    }

    public static class SignUpForm  extends UserNameForm{
        @Constraints.MinLength (value = 8, message = "Password must be minimum 8 characters long")
        @Constraints.MaxLength (value = 25, message = "Password can not be longer than 25 characters")
        @Constraints.Required (message = "Password is required")
        public String password;
        @Constraints.MinLength (value = 8, message = "Password must be minimum 8 characters long")
        @Constraints.MaxLength (value = 25, message = "Password can not be longer than 25 characters")
        @Constraints.Required (message = "Passwords do not match")
        public String confirmPassword;
    }

    /**
     * This will just validate the form for the AJAX call
     * @return ok if there are no errors or a JSON object representing the errors
     */
    public Result validateForm(){
        //get the form data from the request - do this only once
        Form<SignUpForm> binded = signUpForm.bindFromRequest();
        //if we have errors just return a bad request
        if(binded.hasErrors()){
            flash("error", "check your inputs");
            return badRequest(binded.errorsAsJson());
        } else {
            //get the object from the form, for revere take a look at someForm.fill(myObject)
            SignUpForm signUp = binded.get();
            flash("success", "user added");
            return redirect("/");
        }
    }

    /**
     * This will just validate the form for the AJAX call
     * @return ok if there are no errors or a JSON object representing the errors
     */
    public Result validateUserNameForm(){
        //get the form data from the request - do this only once
        Form<UserNameForm> binded = userNameForm.bindFromRequest();
        //if we have errors just return a bad request
        if(binded.hasErrors()){
            Logger.info("---------*--------" + binded.errorsAsJson().toString());
            return badRequest(binded.errorsAsJson());
        } else {
            //get the object from the form, for revere take a look at someForm.fill(myObject)
            UserNameForm unf = binded.get();
            flash("success", "user edited");
            return redirect("/");
        }
    }

}
