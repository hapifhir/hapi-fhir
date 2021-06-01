package ca.uhn.fhir.jpa.delete.model;

import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

/**
 * Serialize a list of URLs so Spring Batch can store it as a String
 */
public class UrlListJson implements IModelJson {
	static final ObjectMapper ourObjectMapper = new ObjectMapper();

	@JsonProperty("urlList")
	List<String> myUrlList;

	public static UrlListJson fromUrlStrings(List<String> elements) {
		return new UrlListJson().setUrlList(elements);
	}

	public static UrlListJson fromJson(String theJson) {
		try {
			return ourObjectMapper.readValue(theJson, UrlListJson.class);
		} catch (JsonProcessingException e) {
			throw new InternalErrorException("Failed to decode " + UrlListJson.class);
		}
	}

	public List<String> getUrlList() {
		return myUrlList;
	}

	public UrlListJson setUrlList(List<String> theUrlList) {
		myUrlList = theUrlList;
		return this;
	}

	@Override
	public String toString() {
		try {
			return ourObjectMapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			throw new InvalidRequestException("Failed to encode " + UrlListJson.class, e);
		}
	}
}
