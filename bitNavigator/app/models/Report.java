package models;

import com.avaje.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
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

    public static List<Report> findByComment(Comment comment) {
        return finder.where().eq("comment", comment).findList();
    }

    public static Report findByUsersEmail(String email) {
        return finder.where().eq("user", User.findByEmail(email)).findUnique();
    }

    public static boolean isReportedByUser(List<Report> reports, User user) {
        for (Report report : reports) {
            if (report.user.equals(user)) {
                return true;
            }
        }
        return false;
    }
}
