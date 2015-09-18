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
 *  Edited by Tomislav on 17/9/2015.
 */
public class PlaceTest {

    @Before
    public void configureDatabase() {
        fakeApplication(inMemoryDatabase());
    }

    @Test
    public void testSavingInDatabase() {

        User u = new User();

        u.email = "asdas@gmail.com";
        u.password = "1234asdfg";
        u.save();
        u = User.findByEmail(u.email);

        Service s = new Service();

        s.serviceType = "smthing";
        s.save();
        s = Service.findByType(s.serviceType);

        Place p = new Place();

        p.title = "Yu Caffe";
        p.description = "Yu caffe Ilidza";
        p.user = u;
        p.service = s;

        p.save();

        Place place = Place.findByTitle("Yu Caffe");
        assertNotNull(place);
    }

    @Test
    public void testNonexistantPlace() {
        Place p = Place.findByTitle("ouhafuzadfu");
        assertNull(p);
    }

    @Test
    public void testGetAllPlaces() {
        List<Place> list = Place.findAll();

        assertNotNull(list);
    }

    @Test
    public void testUserThatDoesNotExist() {
        Place p = Place.findById(12139281);

        assertNull(p);
    }
    @Test
    public void testPlaceExists () {
        User u = new User();
        u.id = 32;
        u.email = "ghgdyug@gyceg.com";
        u.password = "ygyfbctyf2";
        u.save();

        Service s = new Service();
        s.id = 32;
        s.serviceType = "ybdgsyhgfc";
        s.save();

        Place c = new Place();
        c.address = "tyfbtycfyetcbfy";
        c.id = 32;
        c.service = s;
        c.user = u;
        c.description = "yfebyfytfcwe";
        c.save();
        Place place = Place.findById(32);
        assertNotNull(place);
    }

}