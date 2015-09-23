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
    @Test
    public void testFindByPlace() {

        Place p = new Place();
        p.title = "ybftyfxwtf";
        p.description = "ybfkihutctygfd";

        p.save();

        Comment c = new Comment();
        c.commentContent = "yfgrbycfbtydfyfcgeygcuygd";
        c.commentCreated = Calendar.getInstance();
        c.rate = 5;

        c.place = p;
        c.save();

        List<Comment> lists = Comment.findByPlace(p);
        assertNotNull(lists);
    }
    /*
    @Test
    public void testFindById (){
        Comment c = new Comment();
        c.commentContent = "yfgyugyufg";
        c.commentCreated = Calendar.getInstance();

        Comment com = Comment.findById(5);
        assertNotNull(com);
    }
<<<<<<< HEAD
//    @Test
//    public void findByEmail() {
//        User u = new User();
//        u.firstName = "Hasib";
//        u.lastName = "Goodwill";
//        u.email = "hasib@jaaazzxqyq.com";
//        u.save();
//
//        Comment c = new Comment();
//        c.commentContent = "yuufytfgeuigyuf";
//        c.commentCreated = Calendar.getInstance();
//        c.rate = 4;
//        c.user = u;
//
//        c.save();
////
////        Comment comment = Comment.findByUsersEmail("hasib@jaaazzxyq.com");
////        assertNotNull(comment);
//    }
=======
    /*
    @Test
    public void findByEmail() {
        User u = new User();
        u.firstName = "Hasib";
        u.lastName = "Goodwill";
        u.email = "hasib@jaaazzxqyq.com";
        u.save();

        Comment c = new Comment();
        c.commentContent = "yuufytfgeuigyuf";
        c.commentCreated = Calendar.getInstance();
        c.rate = 4;
        c.user = u;

        c.save();

        Comment comment = Comment.findByUsersEmail("hasib@jaaazzxyq.com");
        assertNotNull(comment);
    }
    */
>>>>>>> f8b6a6c833f53f24e08aa0d52bf5f85fee9d74e9
}