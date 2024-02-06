package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class UpdateOneAccount extends GenerateBearerToken{

	String baseURL;
	HashMap <String,String> updateAccountMap;

	
	public UpdateOneAccount() {
		
		baseURL = "https://qa.codefios.com/api";
		updateAccountMap = new HashMap<String,String>();
	}
	
	public Map<String,String> updateAccountHashMap(){
		
		updateAccountMap.put("account_id", "187");
		updateAccountMap.put("account_name", "Huda new updated account 2023");
		updateAccountMap.put("account_number", "18888889");
		updateAccountMap.put("description", "Test description 88");
		updateAccountMap.put("balance","588888.88");
		updateAccountMap.put("contact_person", "RJ");
		
		return updateAccountMap;	
		
	}
	
	@Test (priority = 1)
	public void updateOneAccount() {
		
		Response response=
				
		given()
		   .baseUri(baseURL)
		   .header("Content-Type", "application/json")
		   .body(updateAccountHashMap())
		   .header("Authorization","Bearer " + bearerToken)
		   .log().all().
		when()
		   .put("/account/update").
		then()
		   .log().all()
		   .extract().response();
		
		int responseCode = response.getStatusCode();
		System.out.println("Response Code:" + responseCode);
		Assert.assertEquals(responseCode, 200, "Response codes are NOT matching!");
		
		String responseHeader = response.getHeader("Content-Type");
		System.out.println("Response Header:" + responseHeader);
		Assert.assertEquals(responseHeader, "application/json", "Response header are NOT matching!");
		
		String responseBody =response.getBody().asString();
		System.out.println("Response Body:" + responseBody);
		
		JsonPath jp = new JsonPath(responseBody);
		String accountMessage = jp.getString("message");
		System.out.println("Account Message: " + accountMessage);
		Assert.assertEquals(accountMessage,"Account updated successfully.","Account Messages are NOT matching!");
		
	}
	@Test (priority = 2)
	public void readOneAccount() {
		
		Response response = 
		
	    given()
	       .baseUri(baseURL)
	       .header("Content-Type","application/json")
	       .header("Authorization","Bearer " + bearerToken)
	       .queryParam("account_id", updateAccountHashMap().get("account_id"))
	       .log().all().
	    when()
	       .get("/account/getOne").
	    then()
	       .log().all()
	       .extract().response();
		
		String responseBody = response.getBody().asString();
		JsonPath jp =  new JsonPath(responseBody);
		
		String actualAccountName  = jp.getString("account_name");
		System.out.println("Actual Account Name: " + actualAccountName);
		
		String actulAccountNumber = jp.getString("account_number");
		System.out.println("Actual Account Number: " + actulAccountNumber);
		
		String actulAccountDescription = jp.getString("description");
		System.out.println("Actual Account Description: " + actulAccountDescription);
		
		String actualAccountBalance = jp.getString("balance");
		System.out.println("Actual Account Balance: " + actualAccountBalance);
		
		String actualContactPerson = jp.getString("contact_person");
		System.out.println("Actual Contact Person: " + actualContactPerson);
		
		
		
		String expectedAccountName = updateAccountHashMap().get("account_name");
		System.out.println("Expected Account Name: " + expectedAccountName);
		
		String expectedAccountNumber = updateAccountHashMap().get("account_number");
		System.out.println("Expected Account Name: " + expectedAccountNumber);
		
		String expectedAccountDescription = updateAccountHashMap().get("description");
		System.out.println("Expected Account Description: " + expectedAccountDescription);
		
		String expectedAccountBalance = updateAccountHashMap().get("balance");
		System.out.println("Expected Account Balance: " + expectedAccountBalance);
		
		String expectedContactPerson = updateAccountHashMap().get("contact_person");
		System.out.println("Expected Contact Person: " + expectedContactPerson);
		
		Assert.assertEquals(actualAccountName, expectedAccountName , "Account names are NOT matching!");
		Assert.assertEquals(actulAccountNumber, expectedAccountNumber , "Account numbers are NOT matching!");
		Assert.assertEquals(actulAccountDescription, expectedAccountDescription , "Account description are NOT matching!");
		Assert.assertEquals(actualAccountBalance, expectedAccountBalance , "Account balances are NOT matching!");
		Assert.assertEquals(actualContactPerson, expectedContactPerson , "Contact persons are NOT matching!");
		
		
		long rsponseTime = response.getTimeIn(TimeUnit.MILLISECONDS);
		System.out.println("Rsponse Time" + rsponseTime);
		
		if(rsponseTime < 2500) {
			
			System.out.println("Response Time Is Within The Range");
			
		}else {
			
			System.out.println("Response Time Is Out Of Range");
			
		}
		
		
	}
	
	
}