package com.ovaldez.aws.crud.lambda.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.ovaldez.aws.crud.lambda.api.model.Product;

public class ProductLambdaHandler implements RequestStreamHandler{

	private String DYNAMO_TABLE = "Products";
	
	@SuppressWarnings("unchecked")
	public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
		OutputStreamWriter writer = new OutputStreamWriter(output);
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));
		JSONParser parser = new JSONParser();
		JSONObject responseObject = new JSONObject();
		JSONObject responseBody = new JSONObject();
		
		AmazonDynamoDB client = AmazonDynamoDBClientBuilder.defaultClient();
		DynamoDB dynamoDB = new DynamoDB(client);
		
		int id;
		Item resItem = null;
		System.out.println("in the lambda");
		try {
			JSONObject reqObject = (JSONObject) parser.parse(reader);
			System.out.println(reqObject.toJSONString());
			if(reqObject.get("pathParameters") != null) {
				JSONObject pps = (JSONObject) reqObject.get("pathParameters");
				if(pps.get("id")!= null) {
					id = Integer.parseInt((String)pps.get("id"));
					resItem = dynamoDB.getTable(DYNAMO_TABLE).getItem("id",id);
				}
			}else if(reqObject.get("queryStringParameters")!= null) {
				JSONObject qps = (JSONObject) reqObject.get("queryStringParameters");
				if(qps.get("id")!=null) {
					id = Integer.parseInt((String) qps.get("id"));
					resItem = dynamoDB.getTable(DYNAMO_TABLE).getItem("id",id);
				}
			}
			
			if(resItem != null) {
				Product product = new Product(resItem.toJSON());
				responseBody.put("product", product);
				responseObject.put("statusCode", 200);
			}else {
				responseBody.put("message", "No items found");
				responseObject.put("statusCode", 404);
			}
			responseObject.put("body", responseBody.toString());
		} catch (Exception e) {
			context.getLogger().log("Error: "+e.getMessage());
		}
		writer.write(responseObject.toString());
		reader.close();
		writer.close();
		
	}

}
