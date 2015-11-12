package controllers.rest.controllers;

import models.Image;
import models.Place;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by hajrudin.sehic on 06/11/15.
 */
public class PlaceRestController extends RestSecurityController{

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
        public String image;
        public Integer user_id;
        public Boolean isReservable;
        public Double rating;

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
            this.rating = place.getRating();
            this.isReservable = place.service.isReservable;
            if (Image.findByPlace(place).size() >= 1) {
                this.image = Image.findByPlace(place).get(0).public_id;
            } else {
                this.image = "";
            }
            this.user_id = place.user.id;
            this.isReservable = place.service.isReservable;
            if(place.getRating() != null){
                this.rating = place.getRating();
            }else {
                this.rating = 0.0;
            }
        }
    }


    /**
     * This method finds all places from our database on request from android app
     * @return list of all places in json format
     */
    public Result getListOfPlaces(String token) {

        if(!isAuthorized(token)){
            return badRequest();
        }

        List<PlaceJSON> places = new ArrayList<>();
        for (Place place : Place.findAll()) {
            places.add(new PlaceJSON(place));
        }
        return ok(Json.toJson(places));
    }

}
