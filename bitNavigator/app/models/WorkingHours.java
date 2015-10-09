package models;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.databind.JsonNode;
import play.Logger;
import play.libs.Json;
import utillities.SessionHelper;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ognjen.cetkovic on 30/09/15.
 */
@Entity
public class WorkingHours extends Model {

    public static final String[] DAYS_OF_WEEK = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

    @Id
    public int id;
    @OneToOne (cascade = CascadeType.PERSIST)
    public Place place;
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

    public static Finder<Integer, WorkingHours> finder = new Finder<>(WorkingHours.class);

    public static WorkingHours findByPlace(Place place) {
        return finder.where().eq("place", place).findUnique();
    }

    public Map<Integer, List<Integer>> getWorkingHours() {

        Map<Integer, List<Integer>> workingHours = new HashMap<>();

        for (int i = 1; i <= 7; i++) {
            workingHours.put(i, getWorkingHours(i));
        }

        return workingHours;
    }

    public List<Integer> getWorkingHours(int day) {
        List<Integer> workingHours = new ArrayList<>();
        switch (day){
            case 1:
                workingHours.add(open1);
                workingHours.add(close1);
                return workingHours;
            case 2:
                workingHours.add(open2);
                workingHours.add(close2);
                return workingHours;
            case 3:
                workingHours.add(open3);
                workingHours.add(close3);
                return workingHours;
            case 4:
                workingHours.add(open4);
                workingHours.add(close4);
                return workingHours;
            case 5:
                workingHours.add(open5);
                workingHours.add(close5);
                return workingHours;
            case 6:
                workingHours.add(open6);
                workingHours.add(close6);
                return workingHours;
            case 7:
                workingHours.add(open7);
                workingHours.add(close7);
                return workingHours;
            default:
                throw new IllegalArgumentException();
        }
    }

    public static Integer getOpeningTime(Place place, int day) {
        List<Integer> workingHours = WorkingHours.findByPlace(place).getWorkingHours(day);
        if (workingHours.get(0) == null) {
            return null;
        }
        return workingHours.get(0);
    }

    public static Integer getClosingTime(Place place, int day) {
        List<Integer> workingHours = WorkingHours.findByPlace(place).getWorkingHours(day);
        if (workingHours.get(1) == null) {
            return null;
        }
        return workingHours.get(1);
    }

    public static List<String> getFormatedOpeningTimes(Place place) {
        List<String> workingHours = new ArrayList<>();
        for (int i = 1; i <= 7; i++) {
            Integer tmp = getOpeningTime(place, i);
            if (tmp != null) {
                workingHours.add(String.format("%02d:%02d", tmp / 60, tmp % 60));
            } else {
                workingHours.add("not working");
            }
        }
        return workingHours;
    }

    public static List<String> getFormatedClosingTimes(Place place) {
        List<String> workingHours = new ArrayList<>();
        for (int i = 1; i <= 7; i++) {
            Integer tmp = getClosingTime(place, i);
            if (tmp != null) {
                workingHours.add(String.format("%02d:%02d", tmp / 60, tmp % 60));
            } else {
                workingHours.add("not working");
            }
        }
        return workingHours;
    }

    public static String getWorkingHoursAsJSON(Place place) {
        List<WorkingHoursJSON> workingHours = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            if (getFormatedOpeningTimes(place).get(i).equals("not working")) {
                workingHours.add(new WorkingHoursJSON());
            } else {
                workingHours.add(new WorkingHoursJSON(getFormatedOpeningTimes(place).get(i), getFormatedClosingTimes(place).get(i)));
            }
        }
        String s = Json.toJson(workingHours).toString();
        s = s.replaceAll("\"", "*");
        Logger.info(s);
        return s;
    }

    public static class WorkingHoursJSON extends Model{
        public boolean isActive;
        public String timeFrom;
        public String timeTill;

        public WorkingHoursJSON() {
            this.isActive = false;
        }

        public WorkingHoursJSON(String from, String to) {
            this.isActive = true;
            this.timeFrom = from;
            this.timeTill = to;
        }

        @Override
        public String toString() {
            return isActive + " - " + timeFrom + " - " + timeTill;
        }
    }
}