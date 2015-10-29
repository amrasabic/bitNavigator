package controllers;

import models.Place;
import models.User;
import models.WorkingHours;
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

    public Result getListOfHours() {

        List<HoursJSON> hours = new ArrayList<>();
        for (WorkingHours hour : WorkingHours.findAll()) {
            hours.add(new HoursJSON(hour));
        }

        return ok(Json.toJson(hours));
    }


    private class HoursJSON {
        public Integer id;
        public Integer place_id;
        public Integer open1;
        public Integer close1;
        public Integer open2;
        public Integer close2;
        public Integer open3;
        public Integer close3;
        public Integer open4;
        public Integer close4;
        public Integer open5;
        public Integer close5;
        public Integer open6;
        public Integer close6;
        public Integer open7;
        public Integer close7;

        public HoursJSON(WorkingHours hour) {
            this.id = hour.id;
            this.place_id = hour.place.id;
            if(hour.open1 == null){
                this.open1 = -1;
            }else {
                this.open1 = hour.open1;
            }
            if(hour.close1 == null){
                this.close1 = -1;
            }else{
                this.close1 = hour.close1;
            }
            if(hour.open2 == null){
                this.open2 = -1;
            }else {
                this.open2 = hour.open2;
            }
            if(hour.close2 == null){
                this.close2 = -1;
            }else{
                this.close2 = hour.close2;
            }
            if(hour.open3 == null){
                this.open3 = -1;
            }else {
                this.open3 = hour.open3;
            }
            if(hour.close3 == null){
                this.close3 = -1;
            }else{
                this.close3 = hour.close3;
            }
            if(hour.open4 == null){
                this.open4 = -1;
            }else {
                this.open4 = hour.open4;
            }
            if(hour.close4 == null){
                this.close4 = -1;
            }else{
                this.close4 = hour.close4;
            }
            if(hour.open5 == null){
                this.open5 = -1;
            }else {
                this.open5 = hour.open5;
            }
            if(hour.close5 == null){
                this.close5 = -1;
            }else{
                this.close5 = hour.close5;
            }
            if(hour.open6 == null){
                this.open6 = -1;
            }else {
                this.open6 = hour.open6;
            }
            if(hour.close6 == null){
                this.close6 = -1;
            }else{
                this.close6 = hour.close6;
            }
            if(hour.open7 == null){
                this.open7 = -1;
            }else {
                this.open7 = hour.open7;
            }
            if(hour.close7 == null){
                this.close7 = -1;
            }else{
                this.close7 = hour.close7;
            }
        }
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
