package controllers;

import models.*;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.data.validation.Constraints;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;
import utillities.Authenticators;
import utillities.PasswordHash;
import utillities.SessionHelper;
import utillities.UserValidator;
import views.html.admin.adminview;
import views.html.user.profile;
import views.html.user.signup;
import views.html.user.userlist;
import play.Play;
import utillities.MailHelper;
import utillities.SessionHelper;
import views.html.user.*;
import views.html.admin.*;

import utillities.PasswordHash;
import utillities.MailHelper;

import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import java.util.Calendar;
import java.util.UUID;

import play.libs.F.Function;
import play.libs.F.Promise;
import play.libs.Json;
import play.libs.ws.WS;
import play.libs.ws.WSResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;





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
    private static String url = Play.application().configuration().getString("url");

    public Result connectWithFB() {
        DynamicForm boundForm = Form.form().bindFromRequest();
        if (User.findByEmail(boundForm.data().get("email")) != null) {
            if (User.findByEmail(boundForm.data().get("email")).isValidated()) {
                session().clear();
                session("email", boundForm.data().get("email"));
            } else {
                flash(ERROR_MESSAGE, "Account is not validated!");
            }
            return ok();
        }
        User user = new User();
        user.email = boundForm.data().get("email");
        user.firstName = boundForm.data().get("first_name");
        user.lastName = boundForm.data().get("last_name");
        user.accountCreated = Calendar.getInstance();
        user.setToken(UUID.randomUUID().toString());
        String host = url + "validate/" + user.getToken();
        MailHelper.send(user.email, host);
        user.save();
        return ok();
    }

    /**
     * Leads random user to signup page
     *
     * @return
     */
    public Result signUp() {
        return ok(signup.render(signUpForm));
    }

    /**
     * Checks if all requirments are fullfiled to continue further. This method
     * gets email and password from database and compares them, if they match
     * user is logged in. Method use password hashing.
     *
     * @return
     */
    public Result checkSignIn() {
        Form<User> boundForm = userForm.bindFromRequest();
        if (boundForm.hasErrors()) {
            return badRequest(boundForm.errorsAsJson());
        }
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

        if (user.isValidated()) {
            session().clear();
            session("email", user.email);
        } else {
            flash(ERROR_MESSAGE, "Account is not validated!");
        }

        return redirect(routes.Application.index());
    }

    public Result validateSignIn() {
        Form<User> boundForm = userForm.bindFromRequest();
        if (boundForm.hasErrors()) {
            return badRequest(boundForm.errorsAsJson());
        }
        User user = User.findByEmail(boundForm.bindFromRequest().field(User.EMAIL).value());
        if (user == null) {
            String msg = "{\"password\":\"Email and/or password invalid!\"}";
            return badRequest(msg);
        }
        try {
            if (!PasswordHash.validatePassword(boundForm.bindFromRequest().field(User.PASSWORD).value(), user.password)) {
                throw new IllegalArgumentException();
            }
        } catch (Exception e) {
            String msg = "{\"password\":\"Email and/or password invalid!\"}";
            return badRequest(msg);
        }

        if (!user.isValidated()) {
            String msg = "{\"password\":\"Account is not validated!\"}";
            return badRequest(msg);
        }
        return ok();
    }

    /**
     * Method sends all data from signup form to database and in that way
     * saves new user. This method use isValidEmailAddress method which checks
     * is form of entered email valid.
     *
     * @return
     */
    public Result save() {
        Form<SignUpForm> boundForm = signUpForm.bindFromRequest();

        SignUpForm singUp = boundForm.get();
        if (User.findByEmail(singUp.email) != null) {
            flash(ERROR_MESSAGE, "Account already linked to given email!");
            return badRequest(signup.render(boundForm));
        }

        if (!singUp.password.equals(singUp.confirmPassword)) {
            flash(ERROR_MESSAGE, "Passwords do not match!");
            return badRequest(signup.render(boundForm));
        }

        if (UserValidator.isPasswordValid(boundForm.field("password").value())[0] == UserController.ERROR_MESSAGE) {
            flash(UserValidator.isPasswordValid(boundForm.field("password").value())[0], UserValidator.isPasswordValid(boundForm.field("password").value())[1]);
            return badRequest(signup.render(boundForm));
        }

        User u = User.newUser(singUp);
        u.setToken(UUID.randomUUID().toString());
        u.update();

        // Sending Email To user
        String host = url + "validate/" + u.getToken();
        MailHelper.send(u.email, host);
        return redirect(routes.Application.index());
    }

    @Security.Authenticated(Authenticators.User.class)
    public Result profile(String email, String msg) {
        final User user = User.findByEmail(email);
        if (user == null) {
            return notFound(String.format("User %s does not exist.", email));
        }
        UserNameForm userFormm = new UserNameForm(user);
        return ok(profile.render(userFormm, msg));
    }

    @Security.Authenticated(Authenticators.User.class)
    public Result updateUser() {

        Form<UserNameForm> boundForm = Form.form(UserNameForm.class).bindFromRequest();

        User user = SessionHelper.getCurrentUser();

        if (user == null) {
            return notFound(String.format("User does not exist."));
        }

        if (!user.email.equals(boundForm.bindFromRequest().field("email").value())) {
            return unauthorized("We good we good");
        }

        if (boundForm.hasErrors()) {
            flash("error", "Name can only hold letters!");
            return redirect(routes.UserController.profile(user.email, "Name can only hold letters!"));
        }

        user = User.updateUser(boundForm.get());

        Http.MultipartFormData body = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart filePart = body.getFile("image");

        if (filePart != null) {
            File file = filePart.getFile();
            Image.createAvatar(file);
        }

        user.update();
        return redirect(routes.Application.index());
    }

    @Security.Authenticated(Authenticators.User.class)
    public Result changePassword() {

        Form<PasswordForm> boundForm = Form.form(PasswordForm.class).bindFromRequest();

        User user = SessionHelper.getCurrentUser();

        if (user == null) {
            return notFound(String.format("User does not exist."));
        }
        if (boundForm.hasErrors()) {
            return redirect(routes.UserController.profile(user.email, "View & edit your BitNavigator profile"));
        }
        try {
            if (!(PasswordHash.validatePassword(boundForm.bindFromRequest().field("oldPassword").value(), user.password))) {
                flash("error", "Incorrect password!");
                return redirect(routes.UserController.profile(user.email, "View & edit your BitNavigator profile"));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        if (!((boundForm.bindFromRequest().field("newPassword").value()).equals(boundForm.bindFromRequest().field("confirmPassword").value()))) {
            flash("error", "Password does not match!");
            return redirect(routes.UserController.profile(user.email, "View & edit your BitNavigator profile"));
        }
        try {
            user.password = PasswordHash.createHash(boundForm.bindFromRequest().field("newPassword").value());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }

        user.update();
        return redirect(routes.UserController.profile(user.email, "Password successfully changed"));

    }

    public static class PasswordForm {
        @Constraints.MinLength(value = 8, message = "Password must be minimum 8 characters long")
        @Constraints.MaxLength(value = 25, message = "Password can not be longer than 25 characters")
        @Constraints.Required(message = "Password is required")
        public String oldPassword;
        @Constraints.MinLength(value = 8, message = "Password must be minimum 8 characters long")
        @Constraints.MaxLength(value = 25, message = "Password can not be longer than 25 characters")
        @Constraints.Required(message = "Password is required")
        public String newPassword;
        @Constraints.MinLength(value = 8, message = "Password must be minimum 8 characters long")
        @Constraints.MaxLength(value = 25, message = "Password can not be longer than 25 characters")
        @Constraints.Required(message = "Passwords do not match")
        public String confirmPassword;
    }

    @Security.Authenticated(Authenticators.Admin.class)
    public Result userList() {
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
        if (admin == null || !admin.isAdmin()) {
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
        @Constraints.Pattern(value = "[a-zA-Z]+", message = "First name can only contain alphabetic characters")
        public String firstName;
        @Constraints.Pattern(value = "[a-zA-Z]+", message = "Last name can only contain alphabetic characters")
        public String lastName;
        @Constraints.Pattern(value = "^\\+387[3,6][1-6]\\d{6}", message = "Enter valid number")
        public String mobileNumber;
        //public Image avatar;

        public UserNameForm() {

        }

        public UserNameForm(User u) {
            this.email = u.email;
            this.firstName = u.firstName;
            this.lastName = u.lastName;
            this.mobileNumber = u.phoneNumber;
            //this.avatar = u.avatar;
        }
    }

    public static class SignUpForm extends UserNameForm {
        @Constraints.MinLength(value = 8, message = "Password must be minimum 8 characters long")
        @Constraints.MaxLength(value = 25, message = "Password can not be longer than 25 characters")
        @Constraints.Required(message = "Password is required")
        public String password;
        @Constraints.MinLength(value = 8, message = "Password must be minimum 8 characters long")
        @Constraints.MaxLength(value = 25, message = "Password can not be longer than 25 characters")
        @Constraints.Required(message = "Passwords do not match")
        public String confirmPassword;
    }

    /**
     * This will just validate the form for the AJAX call
     *
     * @return ok if there are no errors or a JSON object representing the errors
     */
    public Result validateForm() {
        //get the form data from the request - do this only once
        Form<SignUpForm> binded = signUpForm.bindFromRequest();
        //if we have errors just return a bad request
        if (binded.hasErrors()) {
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
     *
     * @return ok if there are no errors or a JSON object representing the errors
     */
    public Result validateUserNameForm() {


        Form<UserNameForm> binded = userNameForm.bindFromRequest();
        if(binded.hasErrors()){
            return badRequest(binded.errorsAsJson());
        } else {
            //get the object from the form, for revere take a look at someForm.fill(myObject)
            UserNameForm unf = binded.get();
            flash("success", "user edited");
            return redirect("/");
        }
    }

    public Result checkPass() {
        User u = SessionHelper.getCurrentUser();
        DynamicForm form = Form.form().bindFromRequest();
        String pass = form.data().get("oldPass");
        String tmp = u.password;
        try {
            if (PasswordHash.validatePassword(pass, tmp))
                return ok();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return badRequest();
    }

    public Result emailValidation(String token) {
        try {
            User user = User.findUserByToken(token);
            if (token == null) {
                return redirect("/");
            }
            if (User.validateUser(user)) {
                session().clear();
                session("email", user.email);
                return redirect("/");
            } else {
                return redirect("/");
            }
        } catch (Exception e){
            return redirect("/login");
        }
    }

    public Result contactUs() {

        return ok(contact.render(new Form<Contact>(Contact.class)));
    }

    public Promise<Result> sendMail() {

        //Getting recaptcha values
        final DynamicForm temp = DynamicForm.form().bindFromRequest();

        Promise<Result> promiseHolder = WS
                .url("https://www.google.com/recaptcha/api/siteverify")
                .setContentType("application/x-www-form-urlencoded")
                .post(String.format(
                        "secret=%s&response=%s",
                        //getting API key from the config file
                        "6LeK6w0TAAAAAMHruSHZNybnEC5wdb0j-vQACDys",
                        temp.get("g-recaptcha-response")))
                .map(new Function<WSResponse, Result>() {
                    public Result apply(WSResponse response) {
                        Logger.info(response.toString());
                        JsonNode json = response.asJson();
                        Logger.debug(json.toString());
                        Form<Contact> contactForm = Form.form(Contact.class)
                                .bindFromRequest();

                        if (json.findValue("success").asBoolean() == true
                                && !contactForm.hasErrors()) {
                            Contact newMessage = contactForm.get();
                            String name = newMessage.name;
                            String email = newMessage.email;
                            String message = newMessage.message;

                            if (message.equals("")) {
                                flash("messageError", "Please fill this field");
                                return redirect("/contact");
                            }
                            flash("success", "Message was sent successfuly!");
                            MailHelper.sendContactMessage(name, email, message);

                            return redirect("/contact");
                        } else {
                            Logger.debug(json.toString());
                            flash("errorMail", "Please verify that you are not a robot!");
                            return redirect("/contact");
                        }
                    }
                });

        return promiseHolder;
    }


    public static class Contact {

        public String name;
        public String email;
        public String message;

        /**
         * Default constructor that sets everything to NN;
         */
        public Contact() {
            this.name= "NN";
            this.email = "NN";
            this.message = "NN";
        }
        /**
         * Constructor with parameters;
         * @param name
         * @param email
         * @param message
         */
        public Contact(String name, String email, String message) {
            super();
            this.name = name;
            this.email = email;
            this.message = message;
        }
    }



}
