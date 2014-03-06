package ca.uhn.fhir.rest.client;

import java.util.Collections;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;

import ca.uhn.fhir.rest.common.BaseMethodBinding;

public class GetClientInvocation extends BaseClientInvocation {

	private final Map<String, String> myParameters;
	private final String myUrlPath;

	public GetClientInvocation(Map<String, String> theParameters, String... theUrlFragments) {
		myParameters = theParameters;
		myUrlPath = StringUtils.join(theUrlFragments, '/');
	}

	public GetClientInvocation(String... theUrlFragments) {
		myParameters = Collections.emptyMap();
		myUrlPath = StringUtils.join(theUrlFragments, '/');
	}


	public Map<String, String> getParameters() {
		return myParameters;
	}

	public String getUrlPath() {
		return myUrlPath;
	}

	@Override
	public HttpRequestBase asHttpRequest(String theUrlBase) {
		return new HttpGet(theUrlBase + myUrlPath);
	}
	
}
