package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;

import java.io.File;
import java.util.concurrent.TimeUnit;

public class DeleteOneAccount extends GenerateBearerToken {
	String baseURL;
	String accountId;
	SoftAssert softAssert;
	


	public DeleteOneAccount() {
		
		baseURL = "https://qa.codefios.com/api";
		accountId = "209";
		softAssert = new SoftAssert();

  }
	
	@Test (priority = 1)
	public void DeleteOneAccount() {
		
		Response response=
		
		given()
	    	   .baseUri(baseURL)
			   .header("Content-Type","application/json")
			   .header("Authorization","Bearer " + bearerToken)
			   .queryParam("account_id", accountId)
			   .log().all().
		when()
				.delete("/account/deleteOne").
		then()
			    .log().all()
			   	.extract().response();
		
		int responseCode = response.getStatusCode();
		System.out.println("Response Code:" + responseCode);
		Assert.assertEquals(responseCode, 200, "Response codes are NOT matching!");
		//softAssert.assertEquals(responseCode, 201, "Response codes are NOT matching!");
		
		String responseHeader = response.getHeader("Content-Type");
		System.out.println("Response Header:" + responseHeader);
		Assert.assertEquals(responseHeader, "application/json", "Response header are NOT matching!");
		
		String responseBody =response.getBody().asString();
		System.out.println("Response Body:" + responseBody);
		
		JsonPath jp = new JsonPath(responseBody);
		String accountMessage = jp.getString("message");
		System.out.println("Account Message: "+ accountMessage);
		Assert.assertEquals(accountMessage, "Account deleted successfully.","Account Messages are NOT matching!");
		
		softAssert.assertAll();
		
		}
	
	@Test (priority = 2)
	public void readOneAccount() {
		
		Response response = 
		
	    given()
	       .baseUri(baseURL)
	       .header("Content-Type","application/json")
	       .header("Authorization","Bearer " + bearerToken)
	       .queryParam("account_id", accountId )
	       .log().all().
	    when()
	       .get("/account/getOne").
	    then()
	       .log().all()
	       .extract().response();
		
		
		int responseCode = response.getStatusCode();
		System.out.println("Response Code: " + responseCode);
		Assert.assertEquals(responseCode, 404, "Response codes are NOT matching!");
		
			
		}	
}