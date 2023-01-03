package com.herokuapp.booker.restful.userinfo;

import com.herokuapp.booker.restful.testbase.TestBase;
import com.herokuapp.booker.restful.utils.TestUtils;
import io.restassured.response.ValidatableResponse;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.Title;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;

import static org.hamcrest.Matchers.hasValue;

@RunWith(SerenityRunner.class)
public class UserCRUDTest extends TestBase {
    static String username = "admin";
    static String password= "password123";
    static String firstname = "Snow" + TestUtils.getRandomValue();
    static String lastname = "White";
    static int totalprice = 445;
    static boolean depositpaid = true;
    static HashMap<String,String> bookingdates;
    static String additionalneeds = "breakfast";
    static int bookingId;
    static String token;



    @Steps
    UserSteps userSteps;

    @Title("T1 - This will create authorization code")
    @Test
    public void test001() {
        ValidatableResponse response = userSteps.authCode(username, password).statusCode(200);
        token = response.log().all().extract().path("token");
    }

    @Title("T2 - This will create a new booking")
    @Test
    public void test002() {
        HashMap<String,String> bookingdates = new HashMap<>();
        bookingdates.put("checkin", "2022-12-01");
        bookingdates.put("checkout", "2022-12-15");

        ValidatableResponse response = userSteps.createBooking(firstname,lastname,totalprice,depositpaid,bookingdates,additionalneeds,token).statusCode(200);
        bookingId = response.log().all().extract().path("bookingid");
        System.out.println(bookingId);

    }

    @Title("T3 - Verifying if the booking was added to the list")
    @Test
    public void test003() {
        HashMap<String,String> bookingdates = new HashMap<>();
        bookingdates.put("checkin", "2022-12-01");
        bookingdates.put("checkout", "2022-12-15");

        HashMap<String, Object> userMap = userSteps.findBookingById(bookingId);
        Assert.assertThat(userMap, hasValue(firstname));

    }

    @Title("T4 - Update the booking details and verify the updated information")
     @Test
    public void test004() {
        firstname = firstname+ "_updated";

        HashMap<String,String> bookingdates = new HashMap<>();
        bookingdates.put("checkin", "2022-12-01");
        bookingdates.put("checkout", "2022-12-15");

        userSteps.updateBookingById(firstname,lastname,totalprice,depositpaid,bookingdates,additionalneeds,bookingId);
        //verifying if the information has been updated
        HashMap<String, Object> userMap = userSteps.findBookingById(bookingId);
        Assert.assertThat(userMap, hasValue(firstname));

    }

    @Test
    public void test005() {
        userSteps.deleteBookingById(bookingId).statusCode(201);
        userSteps.getBookingById(bookingId).statusCode(404);

    }
}
