package com.restjersey.nlg.sports;

/**
 * The class below holds the data which is the news summary.
 * 
 * @author abhishek
 *
 */

public class NLGSportsData {
	
	/**
	 * summary of news.
	 */
	@io.swagger.annotations.ApiModelProperty(value = "data", notes = "summary of news", required = true)
	private
	String news ;

	String getNews() {
		return news;
	}

	void setNews(String news) {
		this.news = news;
	}

}
