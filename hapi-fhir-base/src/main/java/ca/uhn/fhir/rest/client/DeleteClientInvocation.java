package ca.uhn.fhir.rest.client;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpRequestBase;

import ca.uhn.fhir.parser.DataFormatException;

public class DeleteClientInvocation extends BaseClientInvocation {

	private String myUrlPath;

	public DeleteClientInvocation(String... theUrlFragments) {
		super();
		myUrlPath = StringUtils.join(theUrlFragments, '/');
	}

	@Override
	public HttpRequestBase asHttpRequest(String theUrlBase) throws DataFormatException, IOException {
		StringBuilder b = new StringBuilder();
		b.append(theUrlBase);
		b.append(myUrlPath);

		HttpDelete retVal = new HttpDelete(b.toString());
		return retVal;
	}


}
