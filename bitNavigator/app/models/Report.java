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

    public static void addReport(Comment comment, User user) {
        Report report = new Report();
        report.comment = comment;
        report.user = user;
        report.save();
    }

    public static List<Report> findAll() {
        return finder.order("comment").findList();
    }

    public static List<Report> findByComment(Comment comment) {
        return finder.where().eq("comment", comment).findList();
    }

    public static List<Report> findByUser(User user) {
        return finder.where().eq("user", user).findList();
    }

    public static boolean isReportedByUser(List<Report> reports, User user) {
        for (Report report : reports) {
            if (report.user.equals(user)) {
                return true;
            }
        }
        return false;
    }

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

    @Override
    public String toString() {
        return comment.toString();
    }

    public static class ReportHelper {

        public Comment comment;
        public int reportsCount;

        public ReportHelper(Comment comment, int reportsCount) {
            this.comment = comment;
            this.reportsCount = reportsCount;
        }

        @Override
        public String toString() {
            return comment + " (" + reportsCount + ")";
        }
    }
}
