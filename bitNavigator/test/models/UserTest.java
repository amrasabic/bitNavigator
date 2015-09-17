package models;
import com.google.common.annotations.VisibleForTesting;
import java.util.*;

import com.fasterxml.jackson.databind.JsonNode;
import models.User;
import org.junit.*;

import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import play.Logger;
import play.Play;
import play.mvc.*;
import play.test.*;
import play.data.DynamicForm;
import play.data.validation.ValidationError;
import play.data.validation.Constraints.RequiredValidator;
import play.i18n.Lang;
import play.libs.F;
import play.libs.F.*;
import play.twirl.api.Content;

import static org.junit.Assert.assertNull;
import static play.test.Helpers.*;
import static org.junit.Assert.*;

/**
 * Created by Semir on 15.09.2015..
 */
public class UserTest {

    @Before
    public void configureDatabase() {
        fakeApplication(inMemoryDatabase());
    }

    @Test
    public void testSaveUser() {
        User u = new User();
        u.email = "asdadas@bitcamp.ba";
        u.firstName = "Mase";
        u.lastName = "Zna Se";
        u.password = "PitajKonobara";

        u.save();
    }

    @Test
    public void testNonexistantUser() {
        User u = User.findByEmail("ksajdaij@bitcamp.ba");

        assertNull(u);
    }

    @Test
    public void testSavingAndLoading() {
        User u = new User();
        u.email = "asdadas@bitcamp.ba";
        u.firstName = "Mase";
        u.lastName = "Zna Se";
        u.password = "PitajKonobara";

        u.save();

        u = User.findByEmail("asdadas@bitcamp.ba");

        assertNotNull(u);
    }


    @Test
    public void testIsNotAdmin() {
        User u = new User();
        u.email = "asdadas@bitcamp.ba";
        u.firstName = "Mase";
        u.lastName = "Zna Se";
        u.password = "PitajKonobara";
        u.save();
        u = User.findByEmail("asdadas@bitcamp.ba");
        assertNotNull(u);
        assertEquals(u.firstName, "Mase");
        assertEquals(u.admin, false);
    }

    @Test
    public void testIsAdmin() {
        User u = new User();
        u.email = "asdadas@bitcamp.ba";
        u.firstName = "Mase";
        u.lastName = "Zna Se";
        u.password = "PitajKonobara";
        u.save();
        u = User.findByEmail("asdadas@bitcamp.ba");
        assertNotNull(u);
        u.setAdmin(true);
        assertEquals(u.admin, true);
        assertEquals(u.email, "asdadas@bitcamp.ba");
    }


    @Test
    public void checkingDatabase () {
        List<User> lists = User.findAll();

        assertNotNull(lists);
    }
    @Test
    public void userexists() {
        User u = new User();
        u.email = "tyuigug22@uhneyg.ba";
        u.firstName = "Jaka";
        u.lastName = "Blazic";
        u.password = "yfyseftcf235";
        u.save();

        u = User.findByEmail("tyuigug22@uhneyg.ba");
        assertNotNull(u);
    }
}