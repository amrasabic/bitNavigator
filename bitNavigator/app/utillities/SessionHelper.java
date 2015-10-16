package utillities;

import models.Message;
import models.Reservation;
import models.Status;
import models.User;
import play.mvc.Http;

public class SessionHelper {

    private static User getUserFromSession() {
        Http.Context ctx = Http.Context.current();
        String email = ctx.session().get("email");
        if (email != null && !email.isEmpty()) {
            User user = User.findByEmail(email);
            return user;
        }
        return null;
    }

    public static User getCurrentUser() {
        return getUserFromSession();
    }

    public static String getCurrentUsersEmail() {
        User user = getUserFromSession();
        if (user == null)
            return "Anonymous";
        return user.email;
    }

    public static boolean isAdmin() {
        User user = getUserFromSession();
        if (user == null)
            return false;
        return user.isAdmin();
    }

    public static boolean isAuthenticated() {
        return getUserFromSession() != null;
    }

    public static int numberOfNotifications() {
        return Reservation.getAllReservationsOnUsersPlaces(Status.findById(Status.WAITING)).size() + Message.getNewMessages(getCurrentUser()).size();
    }


}