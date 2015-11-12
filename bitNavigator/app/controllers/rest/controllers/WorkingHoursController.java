package controllers.rest.controllers;

import models.WorkingHours;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hajrudin.sehic on 06/11/15.
 */
public class WorkingHoursController extends RestSecurityController {

    /**
     * This method finds working hours of all places from our database on request from android app
     *
     * @return list working hours of all places in json format
     */
    public Result getListOfHours(String token) {

        if(!isAuthorized(token)){
            return badRequest();
        }

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
            this.open1 = getFormatedHour(hour.open1);
            this.close1 = getFormatedHour(hour.close1);
            this.open2 = getFormatedHour(hour.open2);
            this.close2 = getFormatedHour(hour.close2);
            this.open3 = getFormatedHour(hour.open3);
            this.close3 = getFormatedHour(hour.close3);
            this.open4 = getFormatedHour(hour.open4);
            this.close4 = getFormatedHour(hour.close4);
            this.open5 = getFormatedHour(hour.open5);
            this.close5 = getFormatedHour(hour.close5);
            this.open6 = getFormatedHour(hour.open6);
            this.close6 = getFormatedHour(hour.close6);
            this.open7 = getFormatedHour(hour.open7);
            this.close7 = getFormatedHour(hour.close7);
        }

        private Integer getFormatedHour(Integer hour) {
            if (hour == null) {
                return -1;
            } else {
                return hour;
            }
        }
    }
}
