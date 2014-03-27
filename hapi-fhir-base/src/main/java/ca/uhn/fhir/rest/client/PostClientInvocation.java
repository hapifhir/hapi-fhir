package ca.uhn.fhir.rest.client;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.server.Constants;

public class PostClientInvocation extends BaseClientInvocation {

	private final IResource myResource;
	private final Bundle myBundle;
	private final FhirContext myContext;
	private String myUrlExtension;

	public PostClientInvocation(FhirContext theContext, Bundle theBundle) {
		super();
		myContext = theContext;
		myResource = null;
		myBundle = theBundle;
	}

	public PostClientInvocation(FhirContext theContext, IResource theResource, String theUrlExtension) {
		super();
		myContext = theContext;
		myResource = theResource;
		myBundle = null;
		myUrlExtension = theUrlExtension;
	}

	@Override
	public HttpRequestBase asHttpRequest(String theUrlBase) throws DataFormatException, IOException {
		HttpPost httpPost = new HttpPost(theUrlBase + StringUtils.defaultString(myUrlExtension));
		String contents = myContext.newXmlParser().encodeResourceToString(myResource);
		httpPost.setEntity(new StringEntity(contents, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		return httpPost;
	}

}
