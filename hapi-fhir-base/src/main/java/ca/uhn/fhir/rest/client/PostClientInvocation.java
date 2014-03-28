package ca.uhn.fhir.rest.client;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.IResource;

public class PostClientInvocation extends BaseClientInvocationWithContents {

	public PostClientInvocation(FhirContext theContext, Bundle theBundle) {
		super(theContext, theBundle);
	}

	public PostClientInvocation(FhirContext theContext, IResource theResource, String theUrlExtension) {
		super(theContext, theResource, theUrlExtension);
	}

	@Override
	protected HttpPost createRequest(String url, StringEntity theEntity) {
		HttpPost retVal = new HttpPost(url);
		retVal.setEntity(theEntity);
		return retVal;
	}

}
