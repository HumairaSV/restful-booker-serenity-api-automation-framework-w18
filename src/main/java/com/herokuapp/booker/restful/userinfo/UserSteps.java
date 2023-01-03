package com.herokuapp.booker.restful.userinfo;

import com.herokuapp.booker.restful.constants.Endpoints;
import com.herokuapp.booker.restful.model.AuthPojo;
import com.herokuapp.booker.restful.model.BookingPojo;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Step;



import java.util.HashMap;

public class UserSteps {



   @Step("Creating auth code with username: {0}, password: {1}")
    public ValidatableResponse authCode(String username, String password){

        AuthPojo authPojo = new AuthPojo();
        authPojo.setUsername(username);
        authPojo.setPassword(password);

        return SerenityRest.given().log().all()
                .contentType(ContentType.JSON)
                .when()
                .body(authPojo)
                .post(Endpoints.AUTH)
                .then();
    }

    @Step("Creating booking with firstname: {0}, lastname: {1}, totalprice: {2}, depositpaid: {3}, bookingdates: {4}, additionalneeds: {5}")
    public ValidatableResponse createBooking(String firstname,String lastname, int totalprice, boolean depositpaid, HashMap<String,String> bookingdates, String additionalneeds, String token){

        BookingPojo bookingPojo = new BookingPojo();
        bookingPojo.setFirstname(firstname);
        bookingPojo.setLastname(lastname);
        bookingPojo.setTotalprice(totalprice);
        bookingPojo.setDepositpaid(depositpaid);
        bookingPojo.setBookingdates(bookingdates);
        bookingPojo.setAdditionalneeds(additionalneeds);

        return SerenityRest.given().log().all()
                .header("Content-Type", "application/json ")
                //.header("Authorization", "Bearer " + token)
                .when()
                .body(bookingPojo)
                .post(Endpoints.CREATE_BOOKING)
                .then();

    }

    @Step("Extracting booking details by bookingId: {0}")
    public HashMap<String, Object> findBookingById(int bookingId){
       return SerenityRest.given().log().all()
               .header("Content-Type", "application/json ")
               //.header("Authorization", "Bearer " + token)
               .pathParam("bookingId", bookingId)
               .when()
               .get(Endpoints.GET_BOOKING_BY_ID)
               .then()
               .statusCode(200)
               .extract()
               .path("");
    }

    @Step("Updating user information with firstname: {0}, lastname: {1}, totalprice: {2}, depositpaid: {3}, bookingdates: {4}, additionalneeds: {5}, bookingId: {6}")
    public ValidatableResponse updateBookingById(String firstname,String lastname, int totalprice, boolean depositpaid,
                                                 HashMap<String,String> bookingdates, String additionalneeds, int bookingId){
        BookingPojo bookingPojo = new BookingPojo();
        bookingPojo.setFirstname(firstname);
        bookingPojo.setLastname(lastname);
        bookingPojo.setTotalprice(totalprice);
        bookingPojo.setDepositpaid(depositpaid);
        bookingPojo.setBookingdates(bookingdates);
        bookingPojo.setAdditionalneeds(additionalneeds);

        return SerenityRest.given().log().all()
                .contentType(ContentType.JSON)
                .auth().preemptive().basic("admin", "password123")
                .pathParam("bookingId", bookingId)
                .when()
                .body(bookingPojo)
                .put(Endpoints.UPDATE_BOOKING_BY_ID)
                .then();
    }

    @Step("Deleting the user by bookingId: {0}")
    public ValidatableResponse deleteBookingById(int bookingId){
        return SerenityRest.given().log().all()
                .contentType(ContentType.JSON)
                .auth().preemptive().basic("admin", "password123")
                .pathParam("bookingId", bookingId)
                .when()
                .delete(Endpoints.DELETE_BOOKING_BY_ID)
                .then();

    }

    @Step("Getting the user by bookingId: {0}")
    public ValidatableResponse getBookingById(int bookingId){

        return SerenityRest.given().log().all()
                .contentType(ContentType.JSON)
                .pathParam("bookingId", bookingId)
                .when()
                .get(Endpoints.GET_BOOKING_BY_ID)
                .then();
    }
}
