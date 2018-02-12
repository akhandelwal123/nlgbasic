package com.restjersey.nlg.sports.dto.cricresource;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

import io.swagger.annotations.ApiModelProperty;
/**
 * The below class is capable to hold the metadata of transaction object of any report type.
 * 
 * @author abhishek
 *
 */
public class RequestMetadata {
	
	/**
	 * filed to hold copyright value.
	 */
	@ApiModelProperty(value = "copyright", notes = "copyright", required = true)
	private String copyright = "Copyright by Naren.";
	
	/**
	 * report creation timestamp.
	 */
	@ApiModelProperty(value = "timestamp", notes = "timestamp", required = true)
	private String timestamp;

	/**
	 * Default constructor initialize the object and sets the timestamp to the current time in UTC.
	 */
	public RequestMetadata()
	{
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS z");
	    TimeZone utc = TimeZone.getTimeZone("UTC");
	    dateFormatter.setTimeZone(utc);
	    timestamp = dateFormatter.format(new java.util.Date());
	}
	/**
	 * @return the timestamp
	 */
	public String getTimestamp() {
		return timestamp;
	}

	/**
	 * @param strTimestamp the timestamp to set
	 */
	public void setTimestamp(String strTimestamp) {
		this.timestamp = strTimestamp;
	}
	
	/**
	 * @return the copyright
	 */
	public String getCopyright() {
		return copyright;
	}

	/**
	 * @param strCopyright the copyright to set
	 */
	public void setCopyright(final String strCopyright) {
		this.copyright = strCopyright;
	}
}
