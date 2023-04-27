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

import static filesReaders.ReadFromFiles.getJsonStringValueByKey;
import static filesReaders.ReadFromFiles.getPropertyByKey;

public class DeleteUserStepDefs {
    String userId ;
    static volatile String userDataJsonFile = "userTestData.json" ;
    RequestSpecification request = null ;
    DeleteUserApi deleteUserApi ;
    Response response = null ;

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
        deleteUserApi = null ;
        response = null ;
    }
    @Given("a new user is just created to be deleted")
    public void a_new_user_is_just_created_to_be_deleted() {
        // Write code here that turns the phrase above into concrete actions
        RequestSpecification request = RestAssured.given()
                .baseUri(getPropertyByKey("environment.properties", "APP_URL"));
        PostUserApi postUserApiTests = new PostUserApi(request);
        Response response = postUserApiTests.createNewUser_validTokenAndValidEmail(
                String.format(getJsonStringValueByKey(userDataJsonFile, "email"), System.currentTimeMillis())
        );
        response.then().log().all();
        JsonPath jp = response.jsonPath();
        userId =  jp.getString("id") ;
    }

    @Given("I have valid authentication token for deleting existing user")
    public void i_have_valid_authentication_token_for_deleting_existing_user() {
        // Write code here that turns the phrase above into concrete actions
        request = RestAssured.given()
                .baseUri(getPropertyByKey("environment.properties", "APP_URL"));

        deleteUserApi = new DeleteUserApi(request);

    }

    @When("I delete existing user")
    public void i_delete_existing_user() {
        // Write code here that turns the phrase above into concrete actions
        response = deleteUserApi.deleteUser_validToken(userId);
    }

    @Then("The user should be removed from the system")
    public void the_user_should_be_removed_from_the_system() {
        // Write code here that turns the phrase above into concrete actions
        System.out.println("Delete response is :");
        response.then().log().all()
                .statusCode(204);
        Assert.assertEquals( "Response should be empty", "", response.asString());
    }

    @Given("I did not add authentication token for deleting existing user")
    public void i_did_not_add_authentication_token_for_deleting_existing_user() {
        // Write code here that turns the phrase above into concrete actions
        request = RestAssured.given()
                .baseUri(getPropertyByKey("environment.properties", "APP_URL"));

        deleteUserApi = new DeleteUserApi(request);
    }

    @When("I delete existing while being unauthenticated")
    public void i_delete_existing_while_being_unauthenticated() {
        // Write code here that turns the phrase above into concrete actions
        response = deleteUserApi.deleteUser_missingAuth(userId);
    }

    @Then("I should receive response that authentication is required for deleting users")
    public void i_should_receive_response_that_authentication_is_required_for_deleting_users() {
        // Write code here that turns the phrase above into concrete actions
        response.then()
                .statusCode(404)
                .header("Content-Type", Matchers.containsStringIgnoringCase("application/json;"))
                .body("message" , Matchers.equalTo("Resource not found"));
    }

}
