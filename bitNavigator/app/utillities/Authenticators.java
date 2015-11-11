package utillities;

import controllers.routes;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;
import play.mvc.Security;

//This class represents authenticators for User and Admin
public class Authenticators {

    //Inner static class, extends Security.Authenticator. Represents authenticator for admin
    public static class Admin extends Security.Authenticator {

        @Override
        public String getUsername(Http.Context context) {
            models.User user = SessionHelper.getCurrentUser();
            if (user == null)
                return null;

            if (user.isAdmin())
                return user.email;
            return null;
        }

        @Override
        public Result onUnauthorized(Http.Context context) {
            return Results.redirect(routes.UserController.signUp());
        }
    }

    //Inner static class, extends Security.Authenticator. Represents authenticator for user
    public static class User extends Security.Authenticator {

        @Override
        public String getUsername(Http.Context context) {
            models.User user = SessionHelper.getCurrentUser();
            if (user != null)
                return user.email;
            return null;
        }

        @Override
        public Result onUnauthorized(Http.Context context) {
            return redirect(routes.UserController.signUp());
        }
    }
}