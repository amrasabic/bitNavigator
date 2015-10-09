package utillities;

import controllers.routes;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;
import play.mvc.Security;

public class Authenticators {

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