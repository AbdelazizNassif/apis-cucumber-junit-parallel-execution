package parallel;

import apiObjects.users.EditUserApi;
import apiObjects.users.PostUserApi;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.Matchers;
import utils.Utils;

import static filesReaders.ReadFromFiles.getJsonStringValueByKey;
import static filesReaders.ReadFromFiles.getPropertyByKey;

public class EditUserStepDefs {
    String userId ;
    static volatile String userDataJsonFile = "userTestData.json" ;
    RequestSpecification request ;
    EditUserApi editUserApi;
    Response response ;

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
        editUserApi = null ;
        response = null ;
    }

    @Given("I have valid authentication token for editing user")
    public void i_have_valid_authentication_token_for_editing_user() {
        // Write code here that turns the phrase above into concrete actions
        request = RestAssured.given()
                .baseUri(getPropertyByKey("environment.properties", "APP_URL"))  ;
        editUserApi =new EditUserApi(request) ;
    }
    @And("a new user is just created to be edited")
    public void a_new_user_is_just_created_to_be_edited() {
        RequestSpecification request = RestAssured.given()
                .baseUri(getPropertyByKey("environment.properties", "APP_URL"));

        PostUserApi postUserApiTests = new PostUserApi(request);
        Response response = postUserApiTests.createNewUser_validTokenAndValidEmail(
                String.format(getJsonStringValueByKey(userDataJsonFile, "email"), Utils.generateRandomString(4))
        );
        JsonPath jp = response.jsonPath();
        userId = jp.getString("id")   ;
    }
    @When("I update existing user with another unique email")
    public void i_update_existing_user_with_another_unique_email() {
        // Write code here that turns the phrase above into concrete actions
        response = editUserApi.editUser_validToken(userId ) ;
    }

    @Then("The user data should be updated and the new email should be saved")
    public void the_user_data_should_be_updated_and_the_new_email_should_be_saved() {
        // Write code here that turns the phrase above into concrete actions
        response.then().log().all()
                .statusCode(200)
                .header("Content-Type", Matchers.containsStringIgnoringCase("application/json;"))
                .body("id", Matchers.notNullValue())
                .body("name" , Matchers.equalTo(getJsonStringValueByKey(userDataJsonFile , "newUsername")))
                .body("email", Matchers.containsStringIgnoringCase(getJsonStringValueByKey(userDataJsonFile , "newEmailDomain")))
                .body("status" , Matchers.equalToIgnoringCase(getJsonStringValueByKey(userDataJsonFile , "inactiveStatus")));    }

    @Given("I did not add authentication token for editing existing user")
    public void i_did_not_add_authentication_token_for_editing_existing_user() {
        // Write code here that turns the phrase above into concrete actions
        request = RestAssured.given()
                .baseUri(getPropertyByKey("environment.properties", "APP_URL"));

        editUserApi = new EditUserApi(request)  ;

    }
    @When("I update existing user with another unique email while being unauthenticated")
    public void iUpdateExistingUserWithAnotherUniqueEmailWhileBeingUnauthenticated() {
        response = editUserApi.editUser_missingAuth(userId)  ;

    }
    @Then("I should receive response that authentication is required for editing users")
    public void i_should_receive_response_that_authentication_is_required_for_editing_users() {
        // Write code here that turns the phrase above into concrete actions
        response.then()
                .statusCode(404)
                .header("Content-Type", Matchers.containsStringIgnoringCase("application/json;"))
                .body("message" , Matchers.equalTo("Resource not found"));    }

    @Given("I have invalid authentication token for editing existing user")
    public void i_have_invalid_authentication_token_for_editing_existing_user() {
        // Write code here that turns the phrase above into concrete actions
        request =RestAssured.given()
                .baseUri(getPropertyByKey("environment.properties", "APP_URL"))  ;

        editUserApi = new EditUserApi(request) ;

    }

    @When("I update existing user with unique email while having invalid token")
    public void i_update_existing_user_with_unique_email_while_having_invalid_token() {
        // Write code here that turns the phrase above into concrete actions
        response =  editUserApi.editUser_invalidToken(userId ) ;

    }

    @Then("I should receive response that token is invalid for editing existing users")
    public void i_should_receive_response_that_token_is_invalid_for_editing_existing_users() {
        // Write code here that turns the phrase above into concrete actions
        response.then()
                .statusCode(401)
                .header("Content-Type", Matchers.containsStringIgnoringCase("application/json;"))
                .body("message" , Matchers.equalTo("Invalid token")) ;
    }



}
