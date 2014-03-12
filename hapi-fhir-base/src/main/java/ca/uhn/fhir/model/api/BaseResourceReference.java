package ca.uhn.fhir.model.api;

import java.io.IOException;
import java.io.Reader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.client.ClientInvocationHandler;
import ca.uhn.fhir.rest.client.api.IRestfulClient;



public abstract class BaseResourceReference extends BaseElement {

	private IResource myResource;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BaseResourceReference.class);

	/**
	 * Gets the value(s) for <b>reference</b> (Relative, internal or absolute URL reference).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A reference to a location at which the other resource is found. The reference may a relative reference, in which case it is relative to the service base URL, or an absolute URL that resolves to the location where the resource is found. The reference may be version specific or not. If the reference is not to a FHIR RESTful server, then it should be assumed to be version specific. Internal fragment references (start with '#') refer to contained resources
     * </p> 
	 */	
	public abstract StringDt getReference();
	
	public IResource loadResource(IRestfulClient theClient) throws IOException {
		if (myResource != null) {
			return myResource;
		}
		
		ourLog.debug("Loading resource at URL: {}", getReference().getValue());
		
		HttpClient httpClient = theClient.getHttpClient();
		FhirContext context = theClient.getFhirContext();
		
		HttpGet get = new HttpGet(getReference().getValue());
		HttpResponse response = httpClient.execute(get);
		
		// TODO: choose appropriate parser based on response CT
		IParser parser = context.newXmlParser();
		
		Reader responseReader = ClientInvocationHandler.createReaderFromResponse(response);
		myResource = parser.parseResource(responseReader);
		
		return myResource;
	}
	
	
}
