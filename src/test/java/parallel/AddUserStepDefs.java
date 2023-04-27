package parallel;

import apiObjects.users.PostUserApi;
import io.cucumber.java.*;
import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.Matchers;

import static filesReaders.ReadFromFiles.getJsonStringValueByKey;
import static filesReaders.ReadFromFiles.getPropertyByKey;

public class AddUserStepDefs {

    volatile String userDataJsonFile = "userTestData.json" ;
    RequestSpecification request = null ;
    RequestSpecification redundantEmailRequest = null ;
    PostUserApi postUserApiTests = null ;
    Response response = null ;
    String redundantEmail;

    @Before
    public void beforeAnnotation ()
    {
        System.out.println("I am Before Annotation");
    }
    @After
    public void afterAnnotation ()
    {
        System.out.println("I am After Annotation");
        request = null ;
        postUserApiTests = null ;
        response = null ;
    }
    // first scenario
    @Given("I have valid authentication token for creating new user")
    public void i_have_valid_authentication_token() {
        // Write code here that turns the phrase above into concrete actions
        System.out.println("i_have_valid_authentication_token");
        request = RestAssured.given()
                .baseUri(getPropertyByKey("environment.properties", "APP_URL"));
        postUserApiTests = new PostUserApi(request);
    }
    @When("I add new user with unique email")
    public void i_add_new_user_with_unique_email() {
        // Write code here that turns the phrase above into concrete actions
        System.out.println("i_add_new_user_with_unique_email");
        response = postUserApiTests.createNewUser_validTokenAndValidEmail(
                String.format(getJsonStringValueByKey(userDataJsonFile, "email"), System.currentTimeMillis())
        );
    }
    @Then("A new user is added to the system")
    public void a_new_user_is_added_to_the_system() {
        // Write code here that turns the phrase above into concrete actions
        System.out.println("a_new_user_is_added_to_the_system");
        response.then().log().all()
                .statusCode(201)
                .header("Content-Type", Matchers.containsStringIgnoringCase("application/json;"))
                .body("id", Matchers.notNullValue())
                .body("name" , Matchers.equalTo(getJsonStringValueByKey(userDataJsonFile , "username")))
                .body("email", Matchers.containsStringIgnoringCase(getJsonStringValueByKey(userDataJsonFile , "emailDomain")))
                .body("gender" , Matchers.equalTo(getJsonStringValueByKey(userDataJsonFile , "maleGender")))
                .body("status" , Matchers.equalToIgnoringCase(getJsonStringValueByKey(userDataJsonFile, "activeStatus")));
    }

    // second scenario
//    @Given ("I have valid authentication token")
    @Given("There is a user in the system with certain email")
    public void there_is_a_user_in_the_system_with_certain_email() {
        // Write code here that turns the phrase above into concrete actions
        System.out.println("there_is_a_user_in_the_system_with_certain_email");
        redundantEmail = String.format("dummy.%s@example.com", System.currentTimeMillis());
        postUserApiTests.createNewUser_validTokenAndValidEmail(redundantEmail);
    }
    @When("I add new user with redundant email")
    public void i_add_new_user_with_redundant_email() {
        // Write code here that turns the phrase above into concrete actions
        System.out.println("i_add_new_user_with_redundant_email");
        redundantEmailRequest = RestAssured.given()
                .baseUri(getPropertyByKey("environment.properties", "APP_URL"));
        postUserApiTests = new PostUserApi(redundantEmailRequest);
    }
    @Then("A new User is not added and I should receive response that email is taken")
    public void a_new_user_is_not_added_and_i_should_receive_response_that_email_is_taken() {
        // Write code here that turns the phrase above into concrete actions
        System.out.println("a_new_user_is_not_added_and_i_should_receive_response_that_email_is_taken");
        response = postUserApiTests.createNewUser_validTokenAndValidEmail(redundantEmail);
        System.out.println("RESPONSE IS *****************");
        response.then().log().all()
                .statusCode(422)
                .header("Content-Type", Matchers.containsStringIgnoringCase("application/json;"))
                .body("field[0]", Matchers.equalTo("email"))
                .body("message[0]", Matchers.equalTo("has already been taken"));
    }
    // third scenario
    @Given("I did not add authentication token for creating new user")
    public void i_did_not_have_valid_authentication_token() {
        // Write code here that turns the phrase above into concrete actions
        System.out.println("i_did_not_have_valid_authentication_token");
        request = RestAssured.given()
                .baseUri(getPropertyByKey("environment.properties", "APP_URL"));
        postUserApiTests = new PostUserApi(request);
    }
    @When("I try to add new user with unique email")
    public void i_try_to_add_new_user_with_unique_email() {
        // Write code here that turns the phrase above into concrete actions
        System.out.println("i_try_to_add_new_user_with_unique_email");
        response = postUserApiTests.createNewUser_missingAuthenticationAndValidEmail();
    }

    @Then("A new User is not added and I should receive response that authentication is required")
    public void a_new_user_is_not_added_and_i_should_receive_response_that_authentication_is_required() {
        // Write code here that turns the phrase above into concrete actions
        System.out.println("a_new_user_is_not_added_and_i_should_receive_response_that_authentication_is_required");
        response.then()
                .statusCode(401)
                .header("Content-Type", Matchers.containsStringIgnoringCase("application/json;"))
                .body("message" , Matchers.equalTo("Authentication failed"));
    }


    // fourth scenario
    @Given("I have invalid authentication token for creating new user")
    public void i_have_invalid_authentication_token() {
        // Write code here that turns the phrase above into concrete actions
        System.out.println("i_have_invalid_authentication_token");
        request = RestAssured.given()
                .baseUri(getPropertyByKey("environment.properties", "APP_URL"));
        postUserApiTests = new PostUserApi(request);
    }
    @When("I try to add new user with unique email while having invalid token")
    public void i_try_to_add_new_user_with_unique_email_while_having_invalid_token() {
        response = postUserApiTests.createNewUser_invalidTokenAndValidEmail();
    }
    @Then("A new User is not added and I should receive response that token is invalid")
    public void a_new_user_is_not_added_and_i_should_receive_response_that_token_is_invalid() {
        // Write code here that turns the phrase above into concrete actions
        System.out.println("a_new_user_is_not_added_and_i_should_receive_response_that_token_is_invalid");
        response.then()
                .statusLine(Matchers.equalTo("HTTP/1.1 401 Unauthorized"))
                .statusCode(401)
                .header("Content-Type", Matchers.containsStringIgnoringCase("application/json;"))
                .body("message" , Matchers.equalTo("Invalid token"));
    }

    // fifth scenario
//    @Given("I have valid authentication token")
    @When("I try to add new user with invalid email format")
    public void i_try_to_add_new_user_with_invalid_email_format() {
        // Write code here that turns the phrase above into concrete actions
        System.out.println("i_try_to_add_new_user_with_invalid_email_format");
        response = postUserApiTests.createNewUser_validTokenAndInvalidEmail();
    }
    @Then("A new User is not added and I should receive response that email format is not correct")
    public void a_new_user_is_not_added_and_i_should_receive_response_that_email_format_is_not_correct() {
        // Write code here that turns the phrase above into concrete actions
        System.out.println("a_new_user_is_not_added_and_i_should_receive_response_that_email_format_is_not_correct");

        response.then()
                .statusLine(Matchers.equalTo("HTTP/1.1 422 Unprocessable Entity"))
                .statusCode(422)
                .header("Content-Type", Matchers.containsStringIgnoringCase("application/json;"))
                .body("field[0]" , Matchers.equalTo("email"))
                .body("message[0]" , Matchers.equalTo("is invalid"));
    }

}
