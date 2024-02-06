package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;

import java.io.File;
import java.util.concurrent.TimeUnit;

public class GenerateBearerToken {
	String baseURL;
	String authFilePath;
	public static String bearerToken;
	

	public GenerateBearerToken() {
		
		baseURL = "https://qa.codefios.com/api";
		authFilePath = "src\\main\\java\\data\\authPayload.json";
		generateBearerToken();
		

	}
	
	public void generateBearerToken() {
		
		Response response=
				
		given()
		   .baseUri(baseURL)
		   .header("Content-Type", "application/json")
		   .body(new File (authFilePath))
		   .log().all().
		when()
		   .post("/user/login").
		then()
		   .log().all()
		   .statusCode(201)
		   .extract().response();
		
		int responseCode = response.getStatusCode();
		System.out.println("Response Code:" + responseCode);
		Assert.assertEquals(responseCode, 201, "Response codes are NOT matching!");
		
		String responseHeader = response.getHeader("Content-Type");
		System.out.println("Response Header:" + responseHeader);
		Assert.assertEquals(responseHeader, "application/json", "Response header are NOT matching!");
		
		String responseBody =response.getBody().asString();
		System.out.println("Response Body:" + responseBody);
		
		JsonPath jp = new JsonPath(responseBody);
		bearerToken = jp.getString("access_token");
		System.out.println("Bearer Token:" + bearerToken);

		
	}
	
	
}