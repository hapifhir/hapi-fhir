package ca.uhn.fhir.rest.server.interceptor;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.AuthenticationException;

/**
 * This interceptor creates verbose server log entries containing the complete request and response payloads. 
 * <p> 
 * This interceptor is mainly intended for debugging since it will generate very large log entries and
 * could potentially be a security risk since it logs every header and complete payload. Use with caution! 
 * </p>
 */
public class VerboseLoggingInterceptor extends InterceptorAdapter {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(VerboseLoggingInterceptor.class);

	@Override
	public boolean incomingRequestPostProcessed(RequestDetails theRequestDetails, HttpServletRequest theRequest, HttpServletResponse theResponse) throws AuthenticationException {
		
		StringBuilder b = new StringBuilder("Incoming request: ");
		b.append(theRequest.getMethod());
		b.append(" ");
		b.append(theRequest.getRequestURL());
		b.append("\n");
		
		for (Enumeration<String> headerEnumeration = theRequest.getHeaderNames(); headerEnumeration.hasMoreElements(); ) {
			String nextName = headerEnumeration.nextElement();
			for (Enumeration<String> valueEnumeration = theRequest.getHeaders(nextName); valueEnumeration.hasMoreElements(); ) {
				b.append(" * ").append(nextName).append(": ").append(valueEnumeration.nextElement()).append("\n");
			}
		}
		
		ourLog.info(b.toString());
		return true;
	}

	
	
}
