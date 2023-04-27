package apiObjects.users;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static filesReaders.ReadFromFiles.getPropertyByKey;

public class GetUserApi {

    RequestSpecification getUserApiDriver;
    String getUserApiEndpoint = "users/%s" ;

    public GetUserApi(RequestSpecification postUserApiDriver) {
        this.getUserApiDriver = postUserApiDriver;
    }

    public Response getNewlyCreatedUser_validToken(String userId)
    {
        return getUserApiDriver
                .contentType(ContentType.JSON)
                .header("Authorization" , "Bearer " + getPropertyByKey("environment.properties", "ACCESS_TOKEN"))
                .log().all()
                .when()
                .get(String.format(getUserApiEndpoint, userId));
    }

    public Response getNewlyCreatedUser_missingTokenHeader (String userId)
    {
        return getUserApiDriver
                .contentType(ContentType.JSON)
                .log().all()
                .when()
                .get(String.format(getUserApiEndpoint, userId));
    }
    public Response getNewlyCreatedUser_invalidTokenAndValidUserId (String userId)
    {
        return getUserApiDriver
                .contentType(ContentType.JSON)
                .header("Authorization" , "Bearer InvalidToken" )
                .log().all()
                .when()
                .get(String.format(getUserApiEndpoint, userId));
    }
}
