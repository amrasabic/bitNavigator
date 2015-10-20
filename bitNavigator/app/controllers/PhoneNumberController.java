package controllers;

import models.PhoneNumber;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import utillities.Authenticators;
import utillities.SessionHelper;

/**
 * Created by ognje on 19-Oct-15.
 */
public class PhoneNumberController extends Controller{

    private static final Form<PhoneNumber> phoneNumberForm = Form.form(PhoneNumber.class);

    @Security.Authenticated (Authenticators.User.class)
    public Result addPhoneNumber() {

        Form<PhoneNumber> boundForm = phoneNumberForm.bindFromRequest();
        if (boundForm.hasErrors()) {
            return redirect(routes.UserController.profile(SessionHelper.getCurrentUsersEmail(), "Invalid phone number"));
        }

        PhoneNumber phoneNumber = boundForm.get();

        if (PhoneNumber.findByNumberAndUser(phoneNumber.getNumber(), SessionHelper.getCurrentUser()) != null) {
            return redirect(routes.UserController.profile(SessionHelper.getCurrentUsersEmail(), "Phone number already added."));
        }

        phoneNumber.setUser(SessionHelper.getCurrentUser());
        phoneNumber.setToken();
        phoneNumber.setValidated(false);
        phoneNumber.save();
        return redirect(routes.UserController.profile(SessionHelper.getCurrentUsersEmail(), "View & edit your BitNavigator profile"));
    }

    @Security.Authenticated (Authenticators.User.class)
    public Result sendToken() {
        DynamicForm form = Form.form().bindFromRequest();
        String number = form.data().get("number");
        PhoneNumber phoneNumber = PhoneNumber.findByNumberAndUser(number, SessionHelper.getCurrentUser());
        if (phoneNumber == null) {
            return redirect(routes.UserController.profile(SessionHelper.getCurrentUsersEmail(), "Invalid phone number"));
        }
        boolean resend = Boolean.parseBoolean(form.data().get("resend"));
        if (resend || !phoneNumber.isTokenSent()) {
            phoneNumber.setToken();
            phoneNumber.sendToken();
            phoneNumber.setTokenSent(true);
            phoneNumber.update();
        } else {
            return badRequest("Token already sent.");
        }
        return ok("Token is sent.");
    }

    @Security.Authenticated (Authenticators.User.class)
    public Result validatePhoneNumber() {
        DynamicForm form = Form.form().bindFromRequest();
        String number = form.data().get("number");
        String token = form.data().get("token");
        PhoneNumber phoneNumber = PhoneNumber.findByNumberAndUser(number, SessionHelper.getCurrentUser());
        if (phoneNumber == null) {
            return redirect(routes.UserController.profile(SessionHelper.getCurrentUsersEmail(), "Invalid phone number"));
        }
        try {
            if (phoneNumber.getToken() == Integer.parseInt(token)) {
                phoneNumber.setValidated(true);
                phoneNumber.update();
                return redirect(routes.UserController.profile(SessionHelper.getCurrentUsersEmail(), "View & edit your BitNavigator profile"));
            }
        } catch (NumberFormatException e) {
            return badRequest("Invalid token.");
        }

        return badRequest("Token does not match.");
    }

    @Security.Authenticated (Authenticators.User.class)
    public Result delete(int id){
        PhoneNumber phoneNumber = PhoneNumber.findById(id);
        if (phoneNumber == null) {
            return badRequest();
        }
        phoneNumber.delete();
        return ok();
    }

}
