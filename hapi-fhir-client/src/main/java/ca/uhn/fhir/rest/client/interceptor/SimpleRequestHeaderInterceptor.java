package ca.uhn.fhir.rest.client.interceptor;

import ca.uhn.fhir.rest.client.api.IClientInterceptor;
import ca.uhn.fhir.rest.client.api.IHttpRequest;
import ca.uhn.fhir.rest.client.api.IHttpResponse;
import org.apache.commons.lang3.Validate;

import java.io.IOException;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * This interceptor adds an arbitrary header to requests made by this client. Both the
 * header name and the header value are specified by the calling code.
 *
 * @see AdditionalRequestHeadersInterceptor for a more advanced version of this interceptor which can add multiple headers
 */
public class SimpleRequestHeaderInterceptor implements IClientInterceptor {

	private String myHeaderName;
	private String myHeaderValue;

	/**
	 * Constructor
	 */
	public SimpleRequestHeaderInterceptor() {
		this(null, null);
	}

	/**
	 * Constructor
	 *
	 * @param theHeaderName The header name, e.g. "<code>Authorization</code>"
	 * @param theHeaderValue The header value, e.g. "<code>Bearer 09uer90uw9yh</code>"
	 */
	public SimpleRequestHeaderInterceptor(String theHeaderName, String theHeaderValue) {
		super();
		myHeaderName = theHeaderName;
		myHeaderValue = theHeaderValue;
	}

	/**
	 * Constructor which takes a complete header including name and value
	 *
	 * @param theCompleteHeader The complete header, e.g. "<code>Authorization: Bearer af09ufe90efh</code>". Must not be null or empty.
	 */
	public SimpleRequestHeaderInterceptor(String theCompleteHeader) {
		Validate.notBlank(theCompleteHeader, "theCompleteHeader must not be null");

		int colonIdx = theCompleteHeader.indexOf(':');
		if (colonIdx != -1) {
			setHeaderName(theCompleteHeader.substring(0, colonIdx).trim());
			setHeaderValue(theCompleteHeader.substring(colonIdx+1, theCompleteHeader.length()).trim());
		} else {
			setHeaderName(theCompleteHeader.trim());
			setHeaderValue(null);
		}

	}

	public String getHeaderName() {
		return myHeaderName;
	}

	public String getHeaderValue() {
		return myHeaderValue;
	}

	@Override
	public void interceptRequest(IHttpRequest theRequest) {
		if (isNotBlank(getHeaderName())) {
			theRequest.addHeader(getHeaderName(), getHeaderValue());
		}
	}

	@Override
	public void interceptResponse(IHttpResponse theResponse) throws IOException {
		// nothing
	}

	public void setHeaderName(String theHeaderName) {
		myHeaderName = theHeaderName;
	}

	public void setHeaderValue(String theHeaderValue) {
		myHeaderValue = theHeaderValue;
	}

}
