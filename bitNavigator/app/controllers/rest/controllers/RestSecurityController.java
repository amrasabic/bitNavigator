package controllers.rest.controllers;

import models.RestToken;
import play.mvc.Controller;
import utillities.RestSecurity;

/**
 * Created by Sehic on 12.11.2015.
 */
public class RestSecurityController extends Controller implements RestSecurity {

    @Override
    public boolean isAuthorized(String token) {
        RestToken restToken = RestToken.findRestToken(token);
        if (restToken == null) {
            return false;
        }
        return true;
    }
}
