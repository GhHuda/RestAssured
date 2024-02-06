package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;


import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.*;

import java.io.File;
import java.util.concurrent.TimeUnit;

public class CreateOneAccount extends GenerateBearerToken {
	String baseURL;
	String createAccountPath;
	String firstId;


	public CreateOneAccount() {
		
		baseURL = "https://qa.codefios.com/api";
		createAccountPath = "src\\main\\java\\data\\createAccount.json";

  }
	
	@Test (priority = 1)
	public void createOneAccount() {
		
		Response response=
		
		given()
	    	   .baseUri(baseURL)
			   .header("Content-Type","application/json")
			   .body(new File(createAccountPath))
			   .header("Authorization","Bearer " + bearerToken)
			   .log().all().
		when()
				.post("/account/create").
		then()
			    .log().all()
			   	.extract().response();
		
		int responseCode = response.getStatusCode();
		System.out.println("Response Code:" + responseCode);
		Assert.assertEquals(responseCode, 201 , "Response codes are NOT matching!!");
		
		String responseHeader = response.getHeader("Content-Type");
		System.out.println("Response Header:" + responseHeader);
		Assert.assertEquals(responseHeader, "application/json", "Response codes are NOT matching!!");
		
		String responseBody = response.getBody().asString();
		System.out.println("Response Body:" + responseBody);
		
		JsonPath jp = new JsonPath(responseBody);
		String accountMessage = jp.getString("message");
		System.out.println("Account created message: " + accountMessage);
		Assert.assertEquals(accountMessage,"Account created successfully." ,"Account created messages are NOT matching!!");				
			
	}
    	@Test (priority = 2)
        public void readAllAccounts() {
		
		Response response = 
		given()
		       .baseUri(baseURL)
		       .header("Content-Type", "application/json")
		       .header("Authorization", "Bearer "+ bearerToken)
		       .log().all().
		when()
		       .get("/account/getAll").
	   then()
	           .log().all()
	           .extract().response();
		
		String responseBody = response.getBody().asString();
		System.out.println("Response Body:" + responseBody);
		
		JsonPath jp = new JsonPath(responseBody);
		firstId = jp.getString("records[0].account_id");
		System.out.println("First account id:" + firstId);
		
		     		   	
        }
    	@Test (priority = 3)
        public void readOneAccount() {
        	
        	Response response = 
            given()
               .baseUri(baseURL)
               .header("Content-Type", "application/json")
               .auth().preemptive().basic("demo1@codefios.com", "abc123")
               .queryParam("account_id", firstId)
               .log().all().
            when()
               .get("/account/getOne").
            then()
               .log().all()
               .extract().response();
        	
        	String responseBody = response.getBody().asString();
        	System.out.println("Response Body:"+ responseBody);
        	
        	JsonPath jp = new JsonPath(responseBody);
        	String actualAccountName = jp.getString("account_name");
        	System.out.println("Actual Account Name: " + actualAccountName);
        	
        	String actualAccountNumber = jp.getString("account_number");
        	System.out.println("Actual Account Number: " + actualAccountNumber );
        	
        	String actualAccountDescription = jp.getString("description");
        	System.out.println("Actual Account Description: " + actualAccountDescription);
        	
        	String actualAccountBalance = jp.getString("balance");
        	System.out.println("Actual Account Balance: " + actualAccountBalance);
        	
        	String actualContactPerson = jp.getString("contact_person");
        	System.out.println("Actual Contact Person: " + actualContactPerson);
        	
        	File requestBody = new File(createAccountPath);
        	JsonPath jp2 = new JsonPath(requestBody);
        	
        	String expectedAccountName =  jp2.getString("account_name");
        	System.out.println("Expected Account Name: " + expectedAccountName);
        	
        	String expectedAccountNumber = jp2.getString("account_number");
        	System.out.println("Expected Account Number: " + expectedAccountNumber);
        	
        	String expectedAccountDescription = jp2.getString("description");
        	System.out.println("Expected Account Description: " + expectedAccountDescription);
        	
        	String expectedAccountBalance = jp2.getString("balance");
        	System.out.println("Expected Account Balance: " + expectedAccountBalance);
        	
        	String expectedCotactPerson = jp2.getString("contact_person");
        	System.out.println("Epected Cotact Person: " + expectedCotactPerson);
        	
        	
        	Assert.assertEquals(actualAccountName, expectedAccountName, "Account names are not matching");
        	Assert.assertEquals(actualAccountNumber, expectedAccountNumber, "Account numbers are not matching");
        	Assert.assertEquals(actualAccountDescription, expectedAccountDescription, "Account descriptions are not matching");
        	Assert.assertEquals(actualAccountBalance, expectedAccountBalance, "Account balances are not matching");
        	Assert.assertEquals(actualContactPerson, expectedCotactPerson, "Cotact persons are not matching");
        	
        	long responseTime = response.getTimeIn(TimeUnit.MILLISECONDS);
        	System.out.println("Response Time:" + responseTime );
        	
        	
        	if (responseTime < 25000) {
        		
        		System.out.println("Response Time Is Within The Range.");
        		
               	}else {
               		
               		System.out.println("Response Time Is OUT Of Range!!");
               		
               	}
        		
        		
        	}      	
        
}