package controllers.rest.controllers;

import models.Image;
import models.User;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import utillities.PasswordHash;

/**
 * Created by hajrudin.sehic on 06/11/15.
 */
public class UserRestController extends Controller {

    private class UserJSON {
        public Integer id;
        public String firstName;
        public String lastName;
        public String email;
        public String password;
        public String avatar;

        public UserJSON(User user) {
            this.id = user.id;
            this.firstName = user.firstName;
            this.lastName = user.lastName;
            this.email = user.email;
            this.password = user.password;
            if (Image.findByUser(user) == null) {
                this.avatar = "";
            } else {
                this.avatar = Image.findByUser(user).public_id;
            }
        }
    }

    /**
     * This method checks user inputs for login recived from android request
     * @return if everything is valid returns user data in json format or if not returns badRequest
     */
    public Result checkUser() {
        DynamicForm form = Form.form().bindFromRequest();
        String email = form.data().get("email");
        String password = form.data().get("password");
        if (email == null || password == null) {
            return badRequest();
        } else {
            User u = User.findByEmail(email);
            if (u == null) {
                return badRequest();
            } else {
                try {
                    if (PasswordHash.validatePassword(password, u.password)) {
                        UserJSON user = new UserJSON(u);
                        return ok(Json.toJson(user));
                    } else {
                        return badRequest();
                    }
                } catch (Exception e) {
                    e.getMessage();
                }
            }
        }
        return badRequest();
    }

    /**
     * This method checks user inputs for registration from android app request
     * and if everything is valid save user to database
     * @return ok request if registration is saved to database else returns badRequest
     */
    public Result checkRegistration(){
        DynamicForm form = Form.form().bindFromRequest();
        String name = form.data().get("firstName");
        String surname = form.data().get("lastName");
        String email = form.data().get("email");
        String password = form.data().get("password");
        String pass = null;
        try {
            pass = PasswordHash.createHash(password);
        }catch (Exception e){
            e.getMessage();
        }
        if(User.findByEmail(email) == null){
            return badRequest();
        }
        User user = new User();
        user.email = email;
        user.firstName = name;
        user.lastName = surname;
        if(pass != null) {
            user.password = pass;
            user.setValidated(true);
            user.save();
            return ok();
        }else{
            return badRequest();
        }
    }
}
