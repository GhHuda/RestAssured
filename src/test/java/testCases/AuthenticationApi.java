package testCases;

import org.testng.Assert;
import org.testng.annotations.*;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;

import java.io.File;

public class AuthenticationApi {

	String baseURL;
	String authFilePath;
	String bearerToken;

	public AuthenticationApi() {

		baseURL = "https://qa.codefios.com/api";
		authFilePath = "src\\main\\java\\data\\authPayload.json";
	}

	@Test(priority = 1)
	public void authenticationAPI() {

		/*
		 * given:all input details (baseURL,Headers Authorization,Payload/Body,QueryParameters ) 
		 * when: submit API requests(HTTP method, EndPoint/Resource)
		 * then: Validate response(Status code, Headers, ResponseTime, Payload/Body)
		 */

		Response response =

			given()
				.baseUri(baseURL)
				.header("Content-Type", "application/json")
				.body(new File(authFilePath))
				.log().all().
			when()
				.post("/user/login").
			then()
			    .log().all()
			    .statusCode(201)
			    .header("Content-Type", "application/json")
		     	.extract().response();

		int responseCode = response.getStatusCode();
		System.out.println("Response Code:" + responseCode);
		Assert.assertEquals(responseCode, 201,"Response codes are NOT matching!");
		
		String responseHeader = response.getHeader("Content-Type");
		System.out.println("Response Header:" + responseHeader);
		Assert.assertEquals(responseHeader, "application/json" ,"Response header are NOT matching!");

		String responseBody = response.getBody().asString();
		System.out.println("Response Body:" + responseBody);
		
		JsonPath jp = new JsonPath(responseBody);
	    bearerToken = jp.getString("access_token");
		System.out.println("Bearer Token:" + bearerToken);

	}
	
	@Test (priority = 2)
	public void readAllAccounts() {
		
		Response response =
				given()
				.baseUri(baseURL)
				.header("Content-Type", "application/json")
				.header("Authorization","Bearer" + bearerToken)
				.auth().preemptive().basic("demo1@codefios.com", "abc123")
				.log().all().
			when()
				.get("/account/getAll").
			then()
			    .log().all()
		     	.extract().response();
		
		int responseCode = response.getStatusCode();
		System.out.println("Response Code:" + responseCode);
		Assert.assertEquals(responseCode, 200, "Response codes are NOT matching!");
		
		String responseHeader =  response.getHeader("Content-Type");
		System.out.println("Response Header:" + responseHeader);
		Assert.assertEquals(responseHeader, "application/json", "Response headers are NOT matching!");
		
		String responseBody = response.getBody().asString();
		JsonPath jp = new JsonPath(responseBody);
		System.out.println("Response Body:" + responseBody);
		bearerToken = jp.getString("access_token");

		String firstAccID = jp.getString("records[0].account_id");
		System.out.println("First accound id: "+firstAccID);
		
		if(firstAccID != null) {
			System.out.println("First Account Id is not null");
		}
			else
			{
				System.out.println("First Account Id is  null");

		}
	}
}