package models;

import models.Place;
import models.Service;
import models.User;
import org.junit.Before;
import org.junit.Test;
import play.Logger;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;

/**
 * Created by Amra on 9/15/2015.
 *
 */
public class PlaceTest {

    @Before
    public void configureDatabase() {
        fakeApplication(inMemoryDatabase());
    }

    @Test
    public void testSavingInDatabase() {

        User u = new User();
        u.id = 1;
        u.email = "asdas@gmail.com";
        u.password = "1234asdfg";
        Service s = new Service();
        s.id = 1;
        s.serviceType = "smthing";

        Place p = new Place();
        p.id = 1;
        p.title = "Yu Caffe";
        p.description = "Yu caffe Ilidza";
        p.user = u;
        p.service = s;

        p.findByTitle("Yu Caffe");
        assertNotNull(p);
    }

    @Test
    public void testNonexistantPlace() {
        Place p = Place.findByTitle("ouhafuzadfu");
        assertNull(p);
    }

    @Test
    public void testGetAllPlaces() {
        List<Place> list = Place.findAll();

        Logger.info(list.toString());
        assertNotNull(list);
    }

    @Test
    public void testUserThatDoesNotExist() {
        Place p = Place.findById(12139281);

        assertNull(p);
    }

}