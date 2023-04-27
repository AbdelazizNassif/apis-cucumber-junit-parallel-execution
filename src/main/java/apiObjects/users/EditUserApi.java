package apiObjects.users;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONObject;


import static filesReaders.ReadFromFiles.getJsonStringValueByKey;
import static filesReaders.ReadFromFiles.getPropertyByKey;

public class EditUserApi {

    RequestSpecification editUserApiDriver;
    String editUserApiEndpoint = "users/%s" ;

    public EditUserApi(RequestSpecification postUserApiDriver) {
        this.editUserApiDriver = postUserApiDriver;
    }

    public Response editUser_validToken (String userId)
    {
        String userDataJsonFile = "userTestData.json" ;
        JSONObject userJsonBody = new JSONObject();
        userJsonBody.put("name" , getJsonStringValueByKey(userDataJsonFile , "newUsername"));
        userJsonBody.put("email" , String.format(getJsonStringValueByKey(userDataJsonFile, "newEmail"), System.currentTimeMillis()) );
        userJsonBody.put("status" , getJsonStringValueByKey(userDataJsonFile, "inactiveStatus"));

        return editUserApiDriver
                .contentType(ContentType.JSON)
                .header("Authorization" , "Bearer " + getPropertyByKey("environment.properties", "ACCESS_TOKEN"))
                .body(userJsonBody).log().all()
                .when()
                .patch(String.format(editUserApiEndpoint, userId));
    }
    public Response editUser_missingAuth (String userId)
    {
        String userDataJsonFile = "userTestData.json" ;
        JSONObject userJsonBody = new JSONObject();
        userJsonBody.put("name" , getJsonStringValueByKey(userDataJsonFile , "newUsername"));
        userJsonBody.put("email" , String.format(getJsonStringValueByKey(userDataJsonFile, "newEmail"), System.currentTimeMillis()) );
        userJsonBody.put("status" , getJsonStringValueByKey(userDataJsonFile, "inactiveStatus"));

        return editUserApiDriver
                .contentType(ContentType.JSON)
                .body(userJsonBody).log().all()
                .when()
                .patch(String.format(editUserApiEndpoint, userId));
    }
    public Response editUser_invalidToken (String userId)
    {
        String userDataJsonFile = "userTestData.json" ;
        JSONObject userJsonBody = new JSONObject();
        userJsonBody.put("name" , getJsonStringValueByKey(userDataJsonFile , "newUsername"));
        userJsonBody.put("email" , String.format(getJsonStringValueByKey(userDataJsonFile, "newEmail"), System.currentTimeMillis()) );
        userJsonBody.put("status" , getJsonStringValueByKey(userDataJsonFile, "inactiveStatus"));

        return editUserApiDriver
                .contentType(ContentType.JSON)
                .header("Authorization" , "Bearer invalidToken" )
                .body(userJsonBody).log().all()
                .when()
                .patch(String.format(editUserApiEndpoint, userId));
    }
}
