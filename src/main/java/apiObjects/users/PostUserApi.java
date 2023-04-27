package apiObjects.users;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONObject;


import static filesReaders.ReadFromFiles.getJsonStringValueByKey;
import static filesReaders.ReadFromFiles.getPropertyByKey;

public class PostUserApi {

    RequestSpecification postUserApiDriver;
    String postUserApiEndpoint = "users" ;

    public PostUserApi(RequestSpecification postUserApiDriver) {
        this.postUserApiDriver = postUserApiDriver;
    }

//    public Response createNewUser_validTokenAndValidEmail ()
//    {
//        String userDataJsonFile = "userTestData.json" ;
//        System.out.println("**" + getJsonStringValueByKey(userDataJsonFile , "username"));
//        JSONObject userJsonBody = new JSONObject();
//        userJsonBody.put("name" , getJsonStringValueByKey(userDataJsonFile , "username"));
//        userJsonBody.put("gender" , getJsonStringValueByKey(userDataJsonFile , "maleGender"));
//        userJsonBody.put("email" , String.format(getJsonStringValueByKey(userDataJsonFile, "email"), System.currentTimeMillis()) );
//        userJsonBody.put("status" , getJsonStringValueByKey(userDataJsonFile, "activeStatus"));
//
//        return postUserApiDriver
//                .contentType(ContentType.JSON)
//                .header("Authorization" , "Bearer " + getPropertyByKey("environment.properties", "ACCESS_TOKEN"))
//                .body(userJsonBody).log().all()
//                .when()
//                .post(postUserApiEndpoint);
//    }
    public Response createNewUser_validTokenAndValidEmail (String email)
    {
        String userDataJsonFile = "userTestData.json" ;
        System.out.println("**" + getJsonStringValueByKey(userDataJsonFile , "username"));
        JSONObject userJsonBody = new JSONObject();
        userJsonBody.put("name" , getJsonStringValueByKey(userDataJsonFile , "username"));
        userJsonBody.put("gender" , getJsonStringValueByKey(userDataJsonFile , "maleGender"));
        userJsonBody.put("email" , email );
        userJsonBody.put("status" , getJsonStringValueByKey(userDataJsonFile, "activeStatus"));

        return postUserApiDriver
                .contentType(ContentType.JSON)
                .header("Authorization" , "Bearer " + getPropertyByKey("environment.properties", "ACCESS_TOKEN"))
                .body(userJsonBody).log().all()
                .when()
                .post(postUserApiEndpoint);
    }
    public Response createNewUser_missingAuthenticationAndValidEmail()
    {
        JSONObject userJsonBody = new JSONObject();
        userJsonBody.put("name" , "user name");
        userJsonBody.put("gender" , "male");
        userJsonBody.put("email" , String.format("email.%s@example.com", System.currentTimeMillis()) );
        userJsonBody.put("status" , "active");


        return postUserApiDriver
                .contentType(ContentType.JSON)
                .body(userJsonBody).log().all()
                .when()
                .post(postUserApiEndpoint);
    }
    public Response createNewUser_invalidTokenAndValidEmail ()
    {
        JSONObject userJsonBody = new JSONObject();
        userJsonBody.put("name" , "user name");
        userJsonBody.put("gender" , "male");
        userJsonBody.put("email" , String.format("email.%s@example.com", System.currentTimeMillis()) );
        userJsonBody.put("status" , "active");


        return postUserApiDriver
                .contentType(ContentType.JSON)
                .header("Authorization" , "Bearer invalidToken" + System.currentTimeMillis())
                .body(userJsonBody).log().all()
                .when()
                .post(postUserApiEndpoint);
    }
    public Response createNewUser_validTokenAndInvalidEmail ()
    {
        JSONObject userJsonBody = new JSONObject();
        userJsonBody.put("name" , "user name");
        userJsonBody.put("gender" , "male");
        userJsonBody.put("email" , String.format("email.%s", System.currentTimeMillis()) );
        userJsonBody.put("status" , "active");


        return postUserApiDriver
                .contentType(ContentType.JSON)
                .header("Authorization" , "Bearer " + getPropertyByKey("environment.properties", "ACCESS_TOKEN"))
                .body(userJsonBody).log().all()
                .when()
                .post(postUserApiEndpoint);
    }

}
