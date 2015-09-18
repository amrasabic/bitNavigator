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

public class IntegrationTest {


    @Before
    public void setUp() {
        fakeApplication(inMemoryDatabase());
    }

    @Test
    public void test() {
        running(testServer(3333, fakeApplication(inMemoryDatabase())),
                HTMLUNIT, new Callback<TestBrowser>() {
                    public void invoke(TestBrowser browser) {
                        browser.goTo("http://localhost:3333");
                        assertTrue(browser.pageSource().contains("bitNavigator"));
                    }
                });
    }

    @Test
    public void testRegistration() {
        running(testServer(3333, fakeApplication(inMemoryDatabase())),
                HTMLUNIT, new Callback<TestBrowser>() {
                    public void invoke(TestBrowser browser) {
                        browser.goTo("http://localhost:3333/service/add");
                        browser.fill("#inputDefault").with("Something");
                        browser.submit("#btn-add-service");
                    }
                });
    }
    @Test
    public void testRouteToHomepage() {
        running(testServer(3333, fakeApplication(inMemoryDatabase())),
                HTMLUNIT, new Callback<TestBrowser>() {
                    public void invoke(TestBrowser browser) {
                        browser.goTo("http://localhost:3333");
                        browser.click("#home-button");
                        assertTrue(browser.pageSource().contains("bitNavigator"));
                    }
                });
    }

    @Test
    public void testSignIn() {
        running(testServer(3333, fakeApplication(inMemoryDatabase())),
                HTMLUNIT, new Callback<TestBrowser>() {
                    public void invoke(TestBrowser browser) {
                        browser.goTo("http://localhost:3333/");
                        browser.fill("#email-signin").with("semir.sahman@bitcamp.ba");
                        browser.fill("#password-signin").with("1234567a");
                        browser.submit("btn-sign-in");
                        assertTrue(browser.pageSource().contains("bitNavigator"));
                    }
                });
    }

    @Test
    public void testRouteToSignUp() {
        running(testServer(3333, fakeApplication(inMemoryDatabase())),
                HTMLUNIT, new Callback<TestBrowser>() {
                    public void invoke(TestBrowser browser) {
                        browser.goTo("http://localhost:3333");
                        browser.submit("btn-sign-up-menu");
                        assertTrue(browser.pageSource().contains("Sign up"));
                    }
                });
    }

    @Test
    public void testSignUp() {
        running(testServer(3333, fakeApplication(inMemoryDatabase())),
                new HtmlUnitDriver(), new Callback<TestBrowser>() {
                    public void invoke(TestBrowser browser) {
                       browser.goTo("http://localhost:3333/user/signup");
                       browser.fill("#firstName-signup").with("Leon");
                        browser.fill("#lastName-signup").with("Benko");
                        browser.fill("#email-signup").with("leon.benko@gmail.com");
                        browser.fill("#password-signup").with("nekasifra1");
                        browser.fill("#confirmPassword-signup").with("nekasifra1");
                        browser.submit("#btn-sign-up");
                        assertTrue(browser.pageSource().contains("bitNavigator"));
                    }
                });
    }

    @Test
    public void testSignOut() {
        running(testServer(3333, fakeApplication(inMemoryDatabase())),
                HTMLUNIT, new Callback<TestBrowser>() {
                    public void invoke(TestBrowser browser) {
                        browser.goTo("http://localhost:3333");
                        browser.submit("#logout");
                        assertTrue(browser.pageSource().contains("bitNavigator"));
                    }
                });
    }

    @Test
    public void testSearch() {
        running(testServer(3333, fakeApplication(inMemoryDatabase())),
                HTMLUNIT, new Callback<TestBrowser>() {
                    public void invoke(TestBrowser browser) {
                        browser.goTo("http://localhost:3333");
                        browser.fill("#srch-term").with("Kod Hasiba");
                        browser.submit("#button-search");
                        assertTrue(browser.pageSource().contains("Place list"));
                    }
                });
    }
/*
    @Test
    public void testRouteToProfile() {
        running(testServer(3333, fakeApplication(inMemoryDatabase())),
                HTMLUNIT, new Callback<TestBrowser>() {
                    public void invoke(TestBrowser browser) {
                        browser.goTo("http://localhost:3333");
                        browser.submit("#go-to-profile");
                        assertTrue(browser.pageSource().contains("does not exist"));
                    }
                });
    }
////////////
    @Test
    public void testRouteToAddPlace() {
        running(testServer(3333, fakeApplication(inMemoryDatabase())),
                new HtmlUnitDriver(), new Callback<TestBrowser>() {
                    public void invoke(TestBrowser browser) {
                        browser.goTo("http://localhost:3333");
                        browser.submit("#add-place");
                        assertTrue(browser.pageSource().contains("Add place"));
                    }
                });
    }
    ////////////////////////////
    @Test
    public void testRouteToAdmin() {
        running(testServer(3333, fakeApplication(inMemoryDatabase())),
                new HtmlUnitDriver(), new Callback<TestBrowser>() {
                    public void invoke(TestBrowser browser) {
                        browser.goTo("http://localhost:3333");
                        browser.submit("#admin");
                        assertTrue(browser.pageSource().contains("Admin"));
                    }
                });
    }

    @Test
    public void testRouteToPlaceList() {
        running(testServer(3333, fakeApplication(inMemoryDatabase())),
                new HtmlUnitDriver(), new Callback<TestBrowser>() {
                    public void invoke(TestBrowser browser) {
                        browser.goTo("http://localhost:3333");
                        browser.submit("#button-search");
                        assertTrue(browser.pageSource().contains("Add place"));
                    }
                });
    }
*/
}
