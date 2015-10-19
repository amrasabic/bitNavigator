package controllers;

import models.Place;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

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
            //this.service = place.service;
            //this.user = place.user;
        }
    }

}
