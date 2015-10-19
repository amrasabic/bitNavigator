package models;

import com.avaje.ebean.Model;
import play.data.validation.Constraints;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.List;

@Entity
public class FAQ extends Model {

    @Id
    public Integer id;
    @Constraints.MaxLength(255)
    @Constraints.MinLength(value = 0, message = "This field can't be empty.!")
    @Constraints.Required(message = "Please input question content.")
    public String question;
    @Constraints.Required(message = "Please type answer content.")
    @Column(columnDefinition = "TEXT")
    public String answer;


    //Finder for class faq
    private static Finder<String, FAQ> finder = new Finder<>(FAQ.class);

    public FAQ(){

    }

    /**
     * Constructor for faq
     * @param question
     * @param answer
     */
    public FAQ(String question, String answer){
        this.question = question;
        this.answer = answer;
    }

    /**
     * This method creates new faq
     * @param question
     * @param answer
     * @return the if of the faq
     */
    public static Integer createFAQ(String question, String answer){
        FAQ newFAQ = new FAQ(question, answer);
        newFAQ.save();
        return newFAQ.id;
    }

    /**
     * This method is used to find faq by question
     * @return faq by name
     */
    public static FAQ getFAQByQuestion(String question){
        FAQ faq = FAQ.finder.where().eq("question", question).findUnique();
        return faq;
    }

    /**
     * This method is used to find faq by id
     * @return faq by id
     */
    public static FAQ getFAQByID(Integer id){
        FAQ faq = FAQ.finder.where().eq("id", id).findUnique();
        return faq;
    }

    /**
     * This method is used to find all FAQs in databse
     * @return List of all FAQs
     */
    public static List<FAQ> findAll(){
        List<FAQ> faqs = finder.orderBy("question asc").findList();
        return faqs;
    }

}
