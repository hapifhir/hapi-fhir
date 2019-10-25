package ca.uhn.fhir.rest.server.interceptor;


import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.util.MetaUtil;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * This interceptor examines a header on the incoming request and places it in
 * <code>Resource.meta.source</code> (R4 and above) or in an extension on <code>Resource.meta</code>
 * with the URL <code>http://hapifhir.io/fhir/StructureDefinition/resource-meta-source</code> (DSTU3).
 * <p>
 * This interceptor does not support versions of FHIR below DSTU3.
 * </p>
 *
 * @see <a href="http://hl7.org/fhir/resource-definitions.html#Resource.meta">Meta.source</a>
 */
@Interceptor
public class CaptureResourceSourceFromHeaderInterceptor {

	private static final Logger ourLog = LoggerFactory.getLogger(CaptureResourceSourceFromHeaderInterceptor.class);
	private final FhirContext myFhirContext;
	private String myHeaderName;

	public CaptureResourceSourceFromHeaderInterceptor(FhirContext theFhirContext) {
		myFhirContext = theFhirContext;
		setHeaderName(Constants.HEADER_REQUEST_SOURCE);
	}

	/**
	 * Provides the header name to examine in incoming requests. Default is {@link ca.uhn.fhir.rest.api.Constants#HEADER_REQUEST_SOURCE "X-Request-Source"}.
	 */
	@SuppressWarnings("WeakerAccess")
	public String getHeaderName() {
		return myHeaderName;
	}

	/**
	 * Provides the header name to examine in incoming requests. Default is {@link ca.uhn.fhir.rest.api.Constants#HEADER_REQUEST_SOURCE "X-Request-Source"}.
	 */
	@SuppressWarnings("WeakerAccess")
	public void setHeaderName(String theHeaderName) {
		myHeaderName = theHeaderName;
	}

	@Hook(Pointcut.SERVER_INCOMING_REQUEST_PRE_HANDLED)
	public void extractSource(RequestDetails theRequestDetails) {
		IBaseResource resource = theRequestDetails.getResource();
		if (resource != null) {
			String requestSource = theRequestDetails.getHeader(getHeaderName());
			if (isNotBlank(requestSource)) {
				ourLog.trace("Setting Meta.source to \"{}\" because of header \"{}\"", requestSource, getHeaderName());
				MetaUtil.setSource(myFhirContext, resource, requestSource);
			}
		}
	}
}
