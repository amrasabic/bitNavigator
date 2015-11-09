package models;

import com.avaje.ebean.Model;
import play.Logger;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ognje on 15-Sep-15.
 */
//This class represents Report model
@Entity
public class Report extends Model {

    @Id
    public int id;
    @ManyToOne
    public Comment comment;
    @ManyToOne
    public User user;

    public static Finder<Integer, Report> finder = new Finder<>(Report.class);

    public Report() {

    }

    /**
     * Adds report on comment
     * @param comment comment taht is reported
     * @param user user that reported comment
     */
    public static void addReport(Comment comment, User user) {
        Report report = new Report();
        report.comment = comment;
        report.user = user;
        report.save();
    }

    //Finds all reports
    public static List<Report> findAll() {
        return finder.order("comment").findList();
    }

    /**
     * Finds all reports by comment
     * @param comment comment on which we want to find reports
     * @return list of reports
     */
    public static List<Report> findByComment(Comment comment) {
        return finder.where().eq("comment", comment).findList();
    }

    /**
     * Finds all reports by user
     * @param user user that reported comment
     * @return list of reports by user
     */
    public static List<Report> findByUser(User user) {
        return finder.where().eq("user", user).findList();
    }

    /**
     * Checks if comment is reported by user
     * @param reports list of reports
     * @param user user we want to check
     * @return true if user reported and false if not
     */
    public static boolean isReportedByUser(List<Report> reports, User user) {
        for (Report report : reports) {
            if (report.user.equals(user)) {
                return true;
            }
        }
        return false;
    }

    //Gets all reports
    public static List<ReportHelper> getAllReports() {
        List<Report> reports = findAll();
        Logger.debug(reports.size() + "");
        List<ReportHelper> reportsHelper = new ArrayList<>();
        if (reports.size() > 0) {
            int counter = 1;
            for (int i = 0; i < reports.size() - 1; i++) {
                if (reports.get(i).comment.id == reports.get(i + 1).comment.id) {
                    counter++;
                } else {
                    reportsHelper.add(new ReportHelper(reports.get(i).comment, counter));
                    counter = 1;
                }
            }

            if (reports.size() > 1 && reports.get(reports.size() - 1).comment.id != reports.get(reports.size() - 2 ).comment.id) {
                reportsHelper.add(new ReportHelper(reports.get(reports.size() - 1).comment, counter));
            } else if (reports.size() > 1 && reports.get(reports.size() - 1).comment.id == reports.get(reports.size() - 2 ).comment.id) {
                reportsHelper.add(new ReportHelper(reports.get(reports.size() - 1).comment, counter));
            } else if(reports.size() == 1){
                reportsHelper.add(new ReportHelper(reports.get(0).comment, 1));
            }
        }
        return reportsHelper;
    }

    //toString method for report
    @Override
    public String toString() {
        return comment.toString();
    }

    //inner static class ReportHelper
    public static class ReportHelper {

        public Comment comment;
        public int reportsCount;

        public ReportHelper(Comment comment, int reportsCount) {
            this.comment = comment;
            this.reportsCount = reportsCount;
        }

        //toString method for ReportHelper
        @Override
        public String toString() {
            return comment + " (" + reportsCount + ")";
        }
    }
}
