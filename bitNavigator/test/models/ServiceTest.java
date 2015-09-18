package models;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import models.Service;
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
 * Created by Tomislav on 17.9.2015.
 */

public class ServiceTest {

    @Before
    public void configureDatabase() {
        fakeApplication(inMemoryDatabase());
    }

    @Test
    public void testSaveService() {
        Service service = new Service();
        service.serviceType = "Something";
        service.save();
    }

    @Test
    public void testFindByType(){
        Service s = new Service();
        s.serviceType = "Nothing";
        s.save();

        s = Service.findByType("Nothing");
        assertNotNull(s);
    }

    @Test
    public void testFindNonexistingService() {
        Service s = Service.findByType("tfvtbhg");
        assertNull(s);
    }
    @Test
    public void testFindAll () {
        List<Service> lists = Service.findAll();

        assertNotNull(lists);
    }
    @Test
    public void nonExistingType () {
        Service s = Service.findByType("tyvdghwgbfxyfsgybg");
        assertNull(s);
    }
}