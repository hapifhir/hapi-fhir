package ca.uhn.fhir.jpa.partition;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.IRestfulServerDefaults;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.List;

public class SystemRequestDetails extends RequestDetails {
	public SystemRequestDetails(IInterceptorBroadcaster theInterceptorBroadcaster) {
		super(theInterceptorBroadcaster);
	}

	@Override
	protected byte[] getByteStreamRequestContents() {
		return new byte[0];
	}

	@Override
	public Charset getCharset() {
		return null;
	}

	@Override
	public FhirContext getFhirContext() {
		return null;
	}

	@Override
	public String getHeader(String name) {
		return null;
	}

	@Override
	public List<String> getHeaders(String name) {
		return null;
	}

	@Override
	public Object getAttribute(String theAttributeName) {
		return null;
	}

	@Override
	public void setAttribute(String theAttributeName, Object theAttributeValue) {

	}

	@Override
	public InputStream getInputStream() throws IOException {
		return null;
	}

	@Override
	public Reader getReader() throws IOException {
		return null;
	}

	@Override
	public IRestfulServerDefaults getServer() {
		return null;
	}

	@Override
	public String getServerBaseForRequest() {
		return null;
	}
}
