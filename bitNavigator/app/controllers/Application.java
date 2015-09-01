package controllers;

import models.User;
import play.*;
import play.mvc.*;

import play.data.Form;
import com.avaje.ebean.Ebean;

import views.html.*;

public class Application extends Controller {

    private static final Form<User> userForm =
            Form.form(User.class);


    public Result index() {
        return ok(index.render("Your new application is ready."));
    }

    public Result signIn() {
        return ok(signin.render(userForm));
    }

    public Result save() {
        Form<User> boundForm = userForm.bindFromRequest();
        String username = boundForm.bindFromRequest().field("username").value();
        String password = boundForm.bindFromRequest().field("password").value();
        User user = new User(username, password);


        Ebean.save(user);
        return redirect(routes.Application.signIn());
    }

}
