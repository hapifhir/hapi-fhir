package ca.uhn.fhir.jpa.delete.model;

import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobParameter;

import java.util.ArrayList;
import java.util.List;

public class UrlListJson implements IModelJson {
	private static final Logger ourLog = LoggerFactory.getLogger(UrlListJson.class);
	static final ObjectMapper ourObjectMapper = new ObjectMapper();

	@JsonProperty("urlList")
	List<String> myUrlList;

	public static UrlListJson fromUrlStrings(String... elements) {
		return new UrlListJson().setUrlList(Lists.newArrayList(elements));
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
