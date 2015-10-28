package controllers;

import models.Place;
import models.User;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import utillities.PasswordHash;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by ognjen.cetkovic on 19/10/15.
 */
public class RestController extends Controller {

    public Result getListOfPLaces() {

        List<PlaceJSON> places = new ArrayList<>();
        for (Place place : Place.findAll()) {
            places.add(new PlaceJSON(place));
        }

        return ok(Json.toJson(places));
    }

    public Result checkUser(){
        DynamicForm form = Form.form().bindFromRequest();
        String email = form.data().get("email");
        String password = form.data().get("password");
        System.out.println("Password = " + password + "email" + email);
        if(email == null || password == null){
            return badRequest();
        }else {
            User u = User.findByEmail(email);
            System.out.println(u.toString());
            if(u == null){
                return badRequest();
            }else{
                try {
                    if (PasswordHash.validatePassword(password, u.password)) {
                        System.out.println("is true" + PasswordHash.validatePassword(password, u.password));
                        UserJSON user = new UserJSON(u);
                        return ok(Json.toJson(user));
                    } else {
                        return badRequest();
                    }
                }catch (Exception e){}
            }
        }
        return badRequest();
    }

    public Result checkRegistration(){
        DynamicForm form = Form.form().bindFromRequest();
        String name = form.data().get("firstName");
        String surname = form.data().get("lastName");
        String email = form.data().get("email");
        String password = form.data().get("password");
        String pass = null;
        try {
            pass = PasswordHash.createHash(password);
        }catch (Exception e){}
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

    private class UserJSON{
        public Integer id;
        public String firstName;
        public String lastName;
        public String email;
        public String password;

        public UserJSON(User user){
            this.id = user.id;
            this.firstName = user.firstName;
            this.lastName = user.lastName;
            this.email = user.email;
            this.password = user.password;
        }
    }


    private class PlaceJSON {

        public Integer id;
        public String title;
        public String description;
        public Double longitude;
        public Double latitude;
        public String address;
        public Calendar placeCreated;
        public Integer numOfViews;
        public Integer numOfReservations;
        public String service;
        //public User user;
        //public Service service;

        public PlaceJSON(Place place) {
            this.id = place.id;
            this.title = place.title;
            this.description = place.description;
            this.longitude = place.longitude;
            this.latitude = place.latitude;
            this.address = place.address;
            this.placeCreated = place.placeCreated;
            this.numOfViews = place.numOfViews;
            this.numOfReservations = place.numOfReservations;
            this.service = place.service.serviceType;
            //this.service = place.service;
            //this.user = place.user;
        }
    }

}
