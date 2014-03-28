package ca.uhn.fhir.rest.client;

import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.IResource;

public class PutClientInvocation extends BaseClientInvocationWithContents {

	public PutClientInvocation(FhirContext theContext, Bundle theBundle) {
		super(theContext, theBundle);
	}

	public PutClientInvocation(FhirContext theContext, IResource theResource, String theUrlExtension) {
		super(theContext, theResource, theUrlExtension);
	}

	@Override
	protected HttpRequestBase createRequest(String url, StringEntity theEntity) {
		HttpPut retVal = new HttpPut(url);
		retVal.setEntity(theEntity);
		return retVal;
	}


}
