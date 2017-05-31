package com.restjersey.nlg.sports;
 
/**
 * @author abhishek
 */
 
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.json.simple.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;




@Path("/news")
@Api(value="sports", description="Cricket news generation for tri series using NLG")
public class SportResource {
	
	private SportsService objService = new SportsService() ;
	private Gson gson = new Gson();
	String response = "{\"meta\":{\"copyright\":\"copyright by naren\",\"timestamp\":\"\"},\"data\":{\"news\":\"1st Match Summary This match is played between XXX & XXX at some stadium on date XXX Total score by TeamA XXXX is XXXX by the fall of XXXX wickets in XXXx overs Total score by TeamB XXXX is XXXX by the fall of XXXX wickets in XXXx overs. Team XXX is won by XXXX runs/wickets This player XXXXX is scored the maximum runs XXXX. The max sixes title for match 1st goes to XXXXX\"}}";
	@GET
	@ApiOperation(
			httpMethod = "GET",
			produces = "application/json", value = "Cricket news generation for tri series using NLG"
			)
	@ApiResponses( value= {
			@ApiResponse(code = 200, message = "Successfully retrieved." , response = Response.class ),
			@ApiResponse(code = 500, message = "Unable to generate news due to internal error " ,
			response = Response.class),
	})
	public Response generateNews() {
/*		if (true) {
			return Response.status(200).entity(gson.toJson(response)).build();
		}
		else {*/
		objService.loadAllJsonFiles();
		objService.generateFacts();
		
		JsonElement output = null;
		return Response.status(200).entity(gson.toJson(output)).build();
		//}
	}

 
}