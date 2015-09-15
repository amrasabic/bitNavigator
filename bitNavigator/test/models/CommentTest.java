package models;
import java.util.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.annotations.VisibleForTesting;
import models.Comment;
import models.Place;
import models.User;
import org.junit.*;

import play.mvc.*;
import play.test.*;
import play.data.DynamicForm;
import play.data.validation.ValidationError;
import play.data.validation.Constraints.RequiredValidator;
import play.i18n.Lang;
import play.libs.F;
import play.libs.F.*;
import play.twirl.api.Content;

import static play.test.Helpers.*;
import static org.junit.Assert.*;

/**
 * Created by Tomislav on 9/15/2015.
 *
 */
public class CommentTest {
    @Before
    public void configureDatabase() {
        fakeApplication(inMemoryDatabase());
    }


    @Test
    public void checkComments() {
        Comment c = new Comment();
        c.commentContent = "gthhtfgtgfgutuytfhyfy";
        c.commentCreated = Calendar.getInstance();
        c.rate = 5;


        c.save();
    }

    @Test
    public void testDatabase() {
        List<Place> list = Place.findAll();

        assertNotNull(list);
    }

}