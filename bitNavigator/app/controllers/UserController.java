package controllers;

import models.Image;
import models.Place;
import models.User;
import org.apache.commons.io.FileUtils;
import play.Play;
import play.data.Form;
import play.data.validation.Constraints;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Http;

import play.mvc.Security;
import utillities.Authenticators;
import views.html.index;
import views.html.user.*;
import views.html.admin.*;
import play.Logger;
import utillities.PasswordHash;

import javax.validation.constraints.Pattern;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
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
    private static Image image;

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
            List<Place> places = Place.findAll();

            return redirect(routes.Application.index());
        }
        try {
            if (!PasswordHash.validatePassword(boundForm.bindFromRequest().field(User.PASSWORD).value(), user.password)) {
                throw new IllegalArgumentException();
            }
        } catch (Exception e) {
            flash(ERROR_MESSAGE, "Email or password invalid!");
            List<Place> places = Place.findAll();

            return redirect(routes.Application.index());
        }
        session().clear();
        session("email", user.email);

        List<Place> places = Place.findAll();
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
        List<Place> places = Place.findAll();
        return redirect(routes.Application.index());
    }

    @Security.Authenticated(Authenticators.User.class)
    public Result profile (String email) {
        final User user = User.findByEmail(email);
        if(user == null)
        {
            return notFound(String.format("User %s does not exist.", email));
        }
        return ok(profile.render(user));

    }

    @Security.Authenticated(Authenticators.User.class)
    public Result updateUser(String email) {

        Form<UserNameForm> boundForm = Form.form(UserNameForm.class).bindFromRequest();

        User user = User.findByEmail(boundForm.bindFromRequest().field("email").value());

        if(user == null) {
            return notFound(String.format("User %s does not exist.", email));
        }

        if (!email.equals(user.email)) {
            User tmp = User.findByEmail(email);
            if (tmp == null || !tmp.admin) {
                return unauthorized("Permission denied!");
            }
        }

        if(boundForm.hasErrors()) {
            flash("error", "Name can only hold letters!");
            return badRequest(profile.render(user));
        }
        Logger.info(boundForm.bindFromRequest().field("mobileNumber").value());
        user.firstName = boundForm.bindFromRequest().field("firstName").value();
        user.lastName = boundForm.bindFromRequest().field("lastName").value();
        user.phoneNumber = boundForm.bindFromRequest().field("mobileNumber").value();

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1);
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
        String formatted = format1.format(cal.getTime());

        Http.MultipartFormData body = request().body().asMultipartFormData();
        List<Http.MultipartFormData.FilePart> pictures = body.getFiles();

        if (pictures != null) {
            for (Http.MultipartFormData.FilePart picture : pictures) {
                String name = user.firstName + formatted;
                File file = picture.getFile();
                String path = Play.application().path() + "/public/images/profileImages/" + user.firstName + "/" + name;

                Logger.info(path);
                try {
                    FileUtils.moveFile(file, new File(path));
                    image = new Image();
                    image.name = name;
                    path ="/images/profileImages/" + user.firstName + "/" + name;
                    image.path = path;

                    image.save();
                    user.image = image;
                    user.update();
                } catch (IOException ex) {
                    Logger.info("Could not move file. " + ex.getMessage());
                    flash("error", "Could not move file.");
                }
            }
            return redirect(routes.Application.index());
        } else {
            flash("error", "Files not present.");
            return badRequest("Picture missing.");
        }
    }

    @Security.Authenticated(Authenticators.Admin.class)
    public Result userList(){
        List<User> users = User.findAll();
        return ok(userlist.render(users));
    }

    @Security.Authenticated(Authenticators.Admin.class)
    public Result delete(String email) {
        User user = User.findByEmail(email);
        if (user == null) {
            Logger.info("dsadasd     "+ email);
            return notFound(String.format("User %s does not exists.", email));
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
        @Constraints.Pattern ("[a-zA-Z]+")
        public String firstName;
        @Constraints.Pattern ("[a-zA-Z]+")
        public String lastName;
        //@Constraints.Pattern ("^\\+[0-9]{1,3}\\.[0-9]{4,14}(?:x.+)?$")
        @Constraints.Pattern ("^\\+387[3,6][1-6]\\d{6}")
        public String mobileNumber;
    }

    public static class SignUpForm  extends UserNameForm{
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
