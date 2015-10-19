package controllers;


import models.FAQ;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.admin.adminCreateFaq;
import views.html.admin.adminEditFaq;
import views.html.admin.allFaqs;

import java.util.List;


//@Security.Authenticated(CurrentAdmin.class)
public class FAQController extends Controller {

    // Declaring variable
    private static final Form<FAQ> FAQForm = Form.form(FAQ.class);

    public Result adminFAQs() {
        // Creating the list of the FAQs
        List<FAQ> faqs = FAQ.findAll();
        return ok(allFaqs.render(faqs));
    }

    /**
     * Renders page where administrator can create new faq. To create new faq user
     * needs to input question and answer.
     *
     */
    public Result newFAQ() {
        return ok(adminCreateFaq.render(FAQForm));
    }

    /**
     *Renders page where administrator can edit selected faq.
     *
     * @param id - id of the category that user wants to edit.
     */
    public Result editFAQ(Integer id) {
        FAQ faq = FAQ.getFAQByID(id);
        Form<FAQ> fillForm = FAQForm.fill(faq);
        return ok(adminEditFaq.render(fillForm, faq));
    }

    /**
     *Enables admnistators user to edit the selected faq.
     *
     * @param id - id of the faq that administrator wants to edit
     * @return if the edit is successful renders administrator page whew all FAQs are listed, othervise
     *             warning message occurs.
     */
    public Result updateFAQ(Integer id) {
        Form<FAQ> boundForm = FAQForm.bindFromRequest();

        if (boundForm.hasErrors()) {
            return badRequest(adminCreateFaq.render(boundForm));
        } else {
            FAQ faq = FAQ.getFAQByID(id);
            faq.question = boundForm.bindFromRequest().field("question").value();
            faq.answer = boundForm.bindFromRequest().field("answer").value();
            faq.update();
        }
        return redirect(routes.FAQController.adminFAQs());
    }

    /**
     * Deleted selected faq from database.
     *
     * @param id - Id of the faq that user wants do delete.
     * @return Administrator panel page where all FAQS are listed.
     */
    public Result deleteFAQ(Integer id){
        FAQ faq = FAQ.getFAQByID(id);
        faq.delete();
        return redirect(routes.FAQController.adminFAQs());
    }

    /**
     * Enbales administrator user to create new faq.  When creating new faq administrator user needs to input
     * question and answer.
     *
     * @return if creating new faq is successful renders administrator panel where all faq are listed,
     * othervise warning message occurs.
     */

    public Result saveFAQ(){
        Form<FAQ> boundForm = FAQForm.bindFromRequest();

            String question = boundForm.bindFromRequest().field("question").value();
            String answer = boundForm.bindFromRequest().field("answer").value();
            FAQ.createFAQ(question, answer);

        return redirect(routes.FAQController.adminFAQs());
    }


    /**
     * Validates the form when calls it. If the form has errors returns the JSON that represents all
     * errors that occurs.
     * @return JSON object that represents all errors that occurs, otherwise returns ok.
     */

    public Result validateFormFAQ() {
        Form<FAQ> binded = FAQForm.bindFromRequest();
        if (binded.hasErrors()) {
            return badRequest(binded.errorsAsJson());
        } else {
            return ok("Validation successful");
        }
    }
}