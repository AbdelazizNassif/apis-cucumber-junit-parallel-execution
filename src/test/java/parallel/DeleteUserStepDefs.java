package parallel;

import apiObjects.users.DeleteUserApi;
import apiObjects.users.PostUserApi;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.Matchers;
import org.junit.Assert;
import utils.Utils;

import static filesReaders.ReadFromFiles.getJsonStringValueByKey;
import static filesReaders.ReadFromFiles.getPropertyByKey;

public class DeleteUserStepDefs {
    ThreadLocal<String> userId = new ThreadLocal<>();
    static volatile String userDataJsonFile = "userTestData.json" ;
    ThreadLocal<RequestSpecification> request = new ThreadLocal<>();
    ThreadLocal<DeleteUserApi> deleteUserApi = new ThreadLocal<>();
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
        deleteUserApi .remove();
        response .remove();
    }
    @Given("a new user is just created to be deleted")
    public void a_new_user_is_just_created_to_be_deleted() {
        // Write code here that turns the phrase above into concrete actions
        RequestSpecification request = RestAssured.given()
                .baseUri(getPropertyByKey("environment.properties", "APP_URL"));
        PostUserApi postUserApiTests = new PostUserApi(request);
        Response response = postUserApiTests.createNewUser_validTokenAndValidEmail(
                String.format(getJsonStringValueByKey(userDataJsonFile, "email"), Utils.generateRandomString(5))
        );
        response.then().log().all();
        JsonPath jp = response.jsonPath();
        userId .set(   jp.getString("id")  ) ;
    }

    @Given("I have valid authentication token for deleting existing user")
    public void i_have_valid_authentication_token_for_deleting_existing_user() {
        // Write code here that turns the phrase above into concrete actions
        request .set(  RestAssured.given()
                .baseUri(getPropertyByKey("environment.properties", "APP_URL")) ) ;

        deleteUserApi .set( new DeleteUserApi(request.get()));

    }

    @When("I delete existing user")
    public void i_delete_existing_user() {
        // Write code here that turns the phrase above into concrete actions
        response.set(  deleteUserApi.get().deleteUser_validToken(userId.get()));
    }

    @Then("The user should be removed from the system")
    public void the_user_should_be_removed_from_the_system() {
        // Write code here that turns the phrase above into concrete actions
        System.out.println("Delete response is :");
        response.get().then().log().all()
                .statusCode(204);
        Assert.assertEquals( "Response should be empty", "", response.get().asString());
    }

    @Given("I did not add authentication token for deleting existing user")
    public void i_did_not_add_authentication_token_for_deleting_existing_user() {
        // Write code here that turns the phrase above into concrete actions
        request .set( RestAssured.given()
                .baseUri(getPropertyByKey("environment.properties", "APP_URL")) ) ;

        deleteUserApi .set( new DeleteUserApi(request.get()) ) ;
    }

    @When("I delete existing while being unauthenticated")
    public void i_delete_existing_while_being_unauthenticated() {
        // Write code here that turns the phrase above into concrete actions
        response .set( deleteUserApi.get().deleteUser_missingAuth(userId.get()) ) ;
    }

    @Then("I should receive response that authentication is required for deleting users")
    public void i_should_receive_response_that_authentication_is_required_for_deleting_users() {
        // Write code here that turns the phrase above into concrete actions
        response.get().then()
                .statusCode(404)
                .header("Content-Type", Matchers.containsStringIgnoringCase("application/json;"))
                .body("message" , Matchers.equalTo("Resource not found"));
    }

}
