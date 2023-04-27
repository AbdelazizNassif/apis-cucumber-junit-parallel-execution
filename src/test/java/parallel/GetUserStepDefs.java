package parallel;

import apiObjects.users.GetUserApi;
import apiObjects.users.PostUserApi;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.Matchers;
import utils.Utils;

import java.nio.charset.Charset;
import java.util.Date;
import java.util.Random;

import static filesReaders.ReadFromFiles.getJsonStringValueByKey;
import static filesReaders.ReadFromFiles.getPropertyByKey;

public class GetUserStepDefs {

    volatile ThreadLocal<String> userId = new ThreadLocal<>();
    volatile String userDataJsonFile = "userTestData.json" ;
    ThreadLocal<RequestSpecification> request = new ThreadLocal<>();
    ThreadLocal<GetUserApi> getUserApi = new ThreadLocal<>();
    ThreadLocal<Response> response = new ThreadLocal<>();

    @Before
    public void beforeAnnotation ()
    {
        System.out.println("I am Before Annotation");
    }
    @After
    public void afterAnnotation ()
    {
        System.out.println("I am After Annotation");
        request .remove();
        getUserApi .remove();
        response .remove();
    }

    @Given("I have valid authentication token for getting user")
    public void i_have_valid_authentication_token_for_getting_user() {
        // Write code here that turns the phrase above into concrete actions
        request .set(  RestAssured.given()
                .baseUri(getPropertyByKey("environment.properties", "APP_URL")) ) ;

        getUserApi .set(  new GetUserApi(request.get()) ) ;
    }
    @Given("New user is just created and had valid user id")
    public void new_user_is_just_created_and_had_valid_user_id() {
        // Write code here that turns the phrase above into concrete actions
        RequestSpecification request = RestAssured.given()
                .baseUri(getPropertyByKey("environment.properties", "APP_URL"));

        PostUserApi postUserApiTests = new PostUserApi(request);
        Response response = postUserApiTests.createNewUser_validTokenAndValidEmail(
                String.format(getJsonStringValueByKey(userDataJsonFile, "email"), Utils.generateRandomString(8))
        );

        JsonPath jp = response.jsonPath();
        userId .set( jp.getString("id")  ) ;
    }
    @When("I try to get the newly created user")
    public void i_try_to_get_the_newly_created_user() {
        // Write code here that turns the phrase above into concrete actions
        response .set(  getUserApi.get().getNewlyCreatedUser_validToken(userId.get()) );
    }
    @Then("I should see the same created user data")
    public void i_should_see_the_same_created_user_data() {
        // Write code here that turns the phrase above into concrete actions
        response.get().then()
                .statusCode(200)
                .header("Content-Type", Matchers.containsStringIgnoringCase("application/json;"))
                .body("id", Matchers.notNullValue())
                .body("name" , Matchers.equalTo(getJsonStringValueByKey(userDataJsonFile , "username")))
                .body("email", Matchers.containsStringIgnoringCase(getJsonStringValueByKey(userDataJsonFile , "emailDomain")))
                .body("gender" , Matchers.equalTo(getJsonStringValueByKey(userDataJsonFile , "maleGender")))
                .body("status" , Matchers.equalToIgnoringCase(getJsonStringValueByKey(userDataJsonFile, "activeStatus")));
    }

    // second scenario
//    @Given("I have valid authentication token for getting user")
    @When("I try to get user with invalid user id")
    public void i_try_to_get_user_with_invalid_user_id() {
        // Write code here that turns the phrase above into concrete actions
        String invalidUserId = new Date().getTime() + "" ;
        response .set( getUserApi.get().getNewlyCreatedUser_validToken(invalidUserId));

    }
    @Then("I should receive response that this user is not found")
    public void i_should_receive_response_that_this_user_is_not_found() {
        // Write code here that turns the phrase above into concrete actions
        response.get().then()
                .statusCode(404)
                .header("Content-Type", Matchers.containsStringIgnoringCase("application/json;"))
                .body("message" , Matchers.equalTo("Resource not found"));
    }

    // third scenario
    @Given("I did not add authentication token for getting user")
    public void i_did_not_add_authentication_token_for_getting_user() {
        // Write code here that turns the phrase above into concrete actions
        request .set(  RestAssured.given()
                .baseUri(getPropertyByKey("environment.properties", "APP_URL") )) ;

        getUserApi .set(  new GetUserApi(request.get()) );
    }
    @When("I try to get user with valid user id")
    public void i_try_to_get_user_with_valid_user_id() {
        // Write code here that turns the phrase above into concrete actions
        response .set(getUserApi.get().getNewlyCreatedUser_missingTokenHeader(userId.get()) ) ;

    }
    @Then("I should receive response that authentication is required")
    public void i_should_receive_response_that_authentication_is_required() {
        // Write code here that turns the phrase above into concrete actions
        response.get().then()
                .statusCode(404)
                .header("Content-Type", Matchers.containsStringIgnoringCase("application/json;"))
                .body("message" , Matchers.equalTo("Resource not found"));
    }

    // fourth scenario
    @Given("I have invalid authentication token for getting user")
    public void i_have_invalid_authentication_token_for_getting_user() {
        // Write code here that turns the phrase above into concrete actions
        request .set(  RestAssured.given()
                .baseUri(getPropertyByKey("environment.properties", "APP_URL")) ) ;

        getUserApi .set( new GetUserApi(request.get()) ) ;

    }
    @When("I try to get user with unique email while having invalid token")
    public void i_try_to_get_user_with_unique_email_while_having_invalid_token() {
        // Write code here that turns the phrase above into concrete actions
        response .set(  getUserApi.get().getNewlyCreatedUser_invalidTokenAndValidUserId(userId.get()));
    }
    @Then("I should receive response that token is invalid")
    public void i_should_receive_response_that_token_is_invalid() {
        // Write code here that turns the phrase above into concrete actions
        response.get().then()
                .statusLine(Matchers.equalTo("HTTP/1.1 401 Unauthorized"))
                .statusCode(401)
                .header("Content-Type", Matchers.containsStringIgnoringCase("application/json;"))
                .body("message" , Matchers.equalTo("Invalid token"));
    }

}
