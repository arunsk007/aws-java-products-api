package com.serverless;

import java.util.Collections;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.serverless.dal.Product;

public class DeleteProductHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

	private static final Logger LOG = LogManager.getLogger(DeleteProductHandler.class);

	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
		try {
	        // get the 'pathParameters' from input
	        Map<String,String> pathParameters =  (Map<String,String>)input.get("pathParameters");
	        String productId = pathParameters.get("id");

	        // get the Product by id
	        Boolean success = new Product().delete(productId);

	        // send the response back
	        if (success) {
	          return ApiGatewayResponse.builder()
	      				.setStatusCode(204)
	      				.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
	      				.build();
	        } else {
	          return ApiGatewayResponse.builder()
	      				.setStatusCode(404)
	      				.setObjectBody("Product with id: '" + productId + "' not found.")
	      				.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
	      				.build();
	        }
	    } catch (Exception ex) {
	    	LOG.error("Error in deleting product: " + ex);

	        // send the error response back
	        Response responseBody = new Response("Error in saving product: ", input);
			return ApiGatewayResponse.builder()
					.setStatusCode(500)
					.setObjectBody(responseBody)
					.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
					.build();
	    }
	}
}
