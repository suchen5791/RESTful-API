package com.qa.test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qa.base.TestBase;
import com.qa.client.RestClient;
import com.qa.data.Users;

public class PostAPITest extends TestBase {
	TestBase testBase;
	String serviceUrl;
	String apiUrl;
	String url;
	RestClient restClient;
	CloseableHttpResponse closebaleHttpResponse;
	
	@BeforeMethod
	public void setUp() {
		testBase = new TestBase();
		serviceUrl = prop.getProperty("URL");
		apiUrl = prop.getProperty("serviceUrl");
		//https://reques.in/api/user
		
		url = serviceUrl + apiUrl;
		
	
}

	@Test
	public void postAPITest() throws JsonGenerationException, JsonMappingException, IOException {
		restClient = new RestClient();
		HashMap<String, String>headerMap = new HashMap<String, String>();
		headerMap.put("Content-Type","application/json");
		
		//jackson API:
		ObjectMapper mapper = new ObjectMapper();
		Users users = new Users("morpheus", "leader");//expected users object
		
		//object to json file:
		mapper.writeValue(new File("C:\\Users\\suchendrags\\Documents\\workspace-sts-3.9.7.RELEASE\\restapi\\src\\main\\java\\com\\qa\\data\\users.json"),users);
		
		//object to json in String: MARSHALING
		String usersJsonString = mapper.writeValueAsString(users);
		System.out.println(usersJsonString);
		
		closebaleHttpResponse = restClient.post(url, usersJsonString, headerMap);
		
		//1. status code:
		int statusCode = closebaleHttpResponse.getStatusLine().getStatusCode();
		
		Assert.assertEquals(statusCode,  testBase.RESPONSE_STATUS_CODE_201);
		
		//2.JsonString:
		String responseString = EntityUtils.toString(closebaleHttpResponse.getEntity(), "UTF-8");
		System.out.println("responseString = " +responseString);
		JSONObject responseJson = new JSONObject(responseString);
		
		System.out.println("The response from API is:"+ responseJson);
		
		//json to java object:UN MARSHALING
		Users usersResObj = mapper.readValue(responseString, Users.class);//actual users object
		System.out.println(usersResObj);
		
		//Assertion comparing actual with expected
		System.out.println(users.getName().equals(usersResObj.getName()));
		
		System.out.println(users.getJob().equals(usersResObj.getName()));
		
		
		  System.out.println(usersResObj.getId());
		  System.out.println(usersResObj.getCreatedAt());
		 
		
		
		
		
		
		
		
		
		
		
		
		
		
	}
	
}
