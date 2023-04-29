package pojoTests;

import apiObjects.users.PostUserApi;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import models.User;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import org.junit.Test;
import utils.Utils;

import static filesReaders.ReadFromFiles.getJsonStringValueByKey;
import static filesReaders.ReadFromFiles.getPropertyByKey;

public class CreateUserTests {
    volatile String userDataJsonFile = "userTestData.json" ;
    RequestSpecification request ;
    PostUserApi postUserApiTests ;
    User user;

    @Test
    public void testCreateUserUsingSerializationAndDeserializeation ()
    {
        request  =  RestAssured.given()
                .baseUri(getPropertyByKey("environment.properties", "APP_URL"))  ;
        postUserApiTests = (  new PostUserApi(request) ) ;
        user =  postUserApiTests.createNewUser_serilaizedBody_validTokenAndValidEmail(
                String.format(getJsonStringValueByKey(userDataJsonFile, "email"), Utils.generateRandomString(7)) );

        assertThat(user.getId(), notNullValue());
        assertThat(user.getName() , equalTo(getJsonStringValueByKey(userDataJsonFile , "username")));
        assertThat(user.getEmailVar(), containsStringIgnoringCase(getJsonStringValueByKey(userDataJsonFile , "emailDomain")));
        assertThat(user.getGender() , equalTo(getJsonStringValueByKey(userDataJsonFile , "maleGender")));
        assertThat(user.getStatus() , equalToIgnoringCase(getJsonStringValueByKey(userDataJsonFile, "activeStatus")));
    }
}
