package com.restjersey.nlg.sports.resources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.Gson;
import com.restjersey.nlg.sports.dto.cricresource.NLGSportsData;
import com.restjersey.nlg.sports.dto.cricresource.NLGSportsResponseModel;
import com.restjersey.nlg.sports.services.BasketBallService;
import com.restjersey.nlg.sports.services.CricketService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("/basketball")
@Api(value="sportsnews", description="BasketBall news generation for tri series using NLG")
public class BasketResource {
	private BasketBallService objService = new BasketBallService() ;
	private Gson gson = new Gson();
	//created to check the hard code respose when started coding for the service.
	String response = "{\"meta\":{\"copyright\":\"copyright by naren\",\"timestamp\":\"\"},\"data\":{\"news\":\"1st Match Summary This match is played between XXX & XXX at some stadium on date XXX Total score by TeamA XXXX is XXXX by the fall of XXXX wickets in XXXx overs Total score by TeamB XXXX is XXXX by the fall of XXXX wickets in XXXx overs. Team XXX is won by XXXX runs/wickets This player XXXXX is scored the maximum runs XXXX. The max sixes title for match 1st goes to XXXXX\"}}";
	@GET
	@ApiOperation(
			httpMethod = "GET",
			produces = "application/json", value = "BasketBall news generation for tri series using NLG"
			)
	@ApiResponses( value= {
			@ApiResponse(code = 200, message = "Successfully retrieved." , response = NLGSportsResponseModel.class ),
			@ApiResponse(code = 500, message = "Unable to generate news due to internal error " ,
			response = Response.class),
	})
	public Response generateNews() {
/*		if (true) {
			return Response.status(200).entity(gson.toJson(response)).build();
		}
		else {*/
		objService.loadAllJsonFiles();
		ArrayList<StringBuilder> ayyayStrBuilder = (ArrayList) objService.generateFacts();
		NLGSportsResponseModel ngmod = new NLGSportsResponseModel();
		NLGSportsData nlgdata = new NLGSportsData();
		for (StringBuilder strBuilder : ayyayStrBuilder) {
			if (StringUtils.isNotBlank(strBuilder)) {
				Map<String,String> map = new HashMap<String,String>();
				String [] allnews = strBuilder.toString().split(",");
				for (int i=0; i < allnews.length ; i++) {
					map.put(String.valueOf(i), allnews[i]);
				}
				nlgdata.add(map);
			}
		}
		ngmod.setData(nlgdata);
		return Response.status(200).entity(gson.toJson(ngmod)).build();
	}

 
}