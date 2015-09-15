package models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
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


import models.Image;
import models.Place;

/**
 * Created by kristina.pupavac on 15/09/15.
 * Image (JUnit) tests that can call all parts of a play app.
 */
public class ImageTest {

    @Before
    public void setup() {
        fakeApplication(inMemoryDatabase());
    }

    @Test
    public void testCreateImage() {
        Image image = new Image();
        image.name = "Picture";
        image.path = "test";
        image.id = 5;

        assertNotNull(image);
    }

    @Test
    public void testImageThatDoesNotExist() {
        Place place = new Place();
        place.address = "test 1";
        place.title = "Test Name";
        place.id = 20;
        List<Image> list = Image.findByPlace(place);
        assertNotNull(list);
    }

}