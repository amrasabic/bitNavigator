package utillities;

import com.avaje.ebean.Ebean;
import models.*;
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

    public static boolean hasValidatedPhoneNumber(){
        for (PhoneNumber phoneNumber : PhoneNumber.findByUser(getCurrentUser())){
            if(phoneNumber.isValidated()){
                return true;
            }
        }
        return false;
    }


    public static boolean checkIp(Place place, String clientIP){
        //String clientIP = request().remoteAddress();
        for(int i = 0; i<place.clientIPs.size(); i++){
            if(place.clientIPs.get(i).ipAddress.equals(clientIP)){
                return false;
            }
        }

        ClientIP ipAddress = new ClientIP();
        ipAddress.ipAddress = clientIP;
        ipAddress.place=place;
        Ebean.save(ipAddress);
        place.clientIPs.add(ipAddress);
        place.update();
        return true;
    }

    public static boolean isOwner(Place place) {
        if (isAuthenticated()) {
            return place.user.equals(getCurrentUser());
        }
        return false;
    }

}