package com.restjersey.nlg.sports;


import io.swagger.annotations.ApiModelProperty;

public class NLGSportsResponseModel {
	
	/**
	 * meta data.
	 */
	@ApiModelProperty(value = "meta data of the request", notes = "meta data of the request", required = true)
	public RequestMetadata meta = new RequestMetadata();
	
	/**
	 * all the report related data.
	 */
	@ApiModelProperty(value = "response data", notes = "response data", required = true)
	public NLGSportsData data;

	public RequestMetadata getMeta() {
		return meta;
	}

	public void setMeta(RequestMetadata meta) {
		this.meta = meta;
	}

	public NLGSportsData getData() {
		return data;
	}

	public void setData(NLGSportsData data) {
		this.data = data;
	}

}
