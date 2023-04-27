package apiObjects.users;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import static filesReaders.ReadFromFiles.getPropertyByKey;

public class DeleteUserApi {

    RequestSpecification deleteUserApiDriver;
    String deleteUserApiEndpoint = "users/%s" ;

    public DeleteUserApi(RequestSpecification postUserApiDriver) {
        this.deleteUserApiDriver = postUserApiDriver;
    }

    public Response deleteUser_validToken (String userId)
    {
        System.out.println("delete request is: ^^^^^^^");
        return deleteUserApiDriver
                .contentType(ContentType.JSON)
                .header("Authorization" , "Bearer " + getPropertyByKey("environment.properties", "ACCESS_TOKEN"))
                .log().all()
                .when()
                .delete(String.format(deleteUserApiEndpoint, userId));
    }
    public Response deleteUser_missingAuth (String userId)
    {
        return deleteUserApiDriver
                .contentType(ContentType.JSON)
                .when()
                .delete(String.format(deleteUserApiEndpoint, userId));
    }
    public Response deleteUser_invalidToken (String userId)
    {
        return deleteUserApiDriver
                .contentType(ContentType.JSON)
                .header("Authorization" , "Bearer invalidToken")

                .when()
                .delete(String.format(deleteUserApiEndpoint, userId));
    }
}
