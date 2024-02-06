package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;

import java.io.File;
import java.util.concurrent.TimeUnit;

public class ReadOneAccount {
	String baseURL;
	String authFilePath;
	String bearerToken;

	public ReadOneAccount() {
		
		baseURL = "https://qa.codefios.com/api";
		authFilePath = "src\\main\\java\\data\\authPayload.json";
		
		

	}
	@Test (priority = 1)
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
	@Test (priority = 2)
	public void getOneAccount() {
		
		Response response=
		
		given()
		   .baseUri(baseURL)
	       .header("Content-Type","application/json")
	       .auth().preemptive().basic("demo1@codefios.com", "abc123")
	       .queryParam("account_id","186")
	       .log().all().
		when()
		   .get("/account/getOne").
		then()
		 .log().all()
		 .statusCode(200)
	   	 .extract().response();
		
		int responseCode = response.getStatusCode();
		System.out.println("Response Code:" + responseCode);
		Assert.assertEquals(responseCode, 200, "Response codes are NOT matching!");
		
		String responseHeader = response.header("Content-Type");
		System.out.println("Response Header:" + responseHeader);
		Assert.assertEquals(responseHeader, "application/json", "Response headers are NOT matching!");
		
		String responseBody = response.getBody().asString();
		System.out.println("Response Body:" + responseBody);
		
		JsonPath jp = new JsonPath(responseBody);
		String accountName = jp.getString("account_name");
		System.out.println("Account Name:" + accountName);
		Assert.assertEquals(accountName, "Ramon Techfios account 222", "Account names are NOT matching!");
		
		String accountNumber = jp.getString("account_number");
		System.out.println("Account Number:" + accountNumber);
		Assert.assertEquals(accountNumber, "5555555", "Account numbers are NOT matching!");
		
		String accountDescription = jp.getString("description");
		System.out.println("Account Description:" + accountDescription);
		Assert.assertEquals(accountDescription, "Test description 1", "Account descriptions are NOT matching!");
		
		String accountBlanace = jp.getString("balance");
		System.out.println("Account Balance:" + accountBlanace);
		Assert.assertEquals(accountBlanace, "100.33",  "Account balances are NOT matching!");
		
		String contactPerson = jp.getString("contact_person");
		System.out.println("Contact Person:" + contactPerson);
		Assert.assertEquals(contactPerson, "MD Islam", "Contact persons are NOT matching!");	
		
		long responseTime = response.timeIn(TimeUnit.MILLISECONDS);
		System.out.println("Response Time :" + responseTime);
		if (responseTime < 2500) {
			
			System.out.println("Response Time Is Within The Range!");
			
		}

		else {
			
			System.out.println("Response Time Is Out Of Range!");
			
		}
	}
	
}