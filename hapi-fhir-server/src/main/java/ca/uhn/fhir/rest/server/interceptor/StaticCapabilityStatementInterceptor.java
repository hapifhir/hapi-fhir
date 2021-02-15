package ca.uhn.fhir.rest.server.interceptor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.util.ClasspathUtil;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseConformance;

/**
 * This interceptor replaces the auto-generated CapabilityStatement that is generated
 * by the HAPI FHIR Server with a static hard-coded resource.
 */
@Interceptor
public class StaticCapabilityStatementInterceptor {

	private String myCapabilityStatementResource;
	private volatile IBaseConformance myCapabilityStatement;

	/**
	 * Sets the CapabilityStatement to use
	 *
	 * @see #setCapabilityStatementResource(String)  for an alternate way to supply the CapabilityStatement
	 */
	public void setCapabilityStatement(IBaseConformance theCapabilityStatement) {
		myCapabilityStatement = theCapabilityStatement;
	}

	/**
	 * Sets the classpath location of the CapabilityStatement to use. If this method is used to supply
	 * the CapabiltyStatement, then the given classpath resource will be read and parsed as a FHIR
	 * CapabilityStatement.
	 *
	 * @see #setCapabilityStatement(IBaseConformance) for an alternate way to supply the CapabilityStatement
	 */
	public void setCapabilityStatementResource(String theClasspath) {
		myCapabilityStatementResource = theClasspath;
		myCapabilityStatement = null;
	}

	@Hook(Pointcut.SERVER_CAPABILITY_STATEMENT_GENERATED)
	public IBaseConformance hook(RequestDetails theRequestDetails) {
		IBaseConformance retVal = myCapabilityStatement;

		if (retVal == null) {
			Validate.notBlank(myCapabilityStatementResource, "No CapabilityStatement defined");
			String output = ClasspathUtil.loadResource(myCapabilityStatementResource);

			FhirContext ctx = theRequestDetails.getFhirContext();
			EncodingEnum encoding = EncodingEnum.detectEncodingNoDefault(output);
			Validate.notNull(encoding, "Could not determine FHIR encoding for resource: %s", myCapabilityStatementResource);

			retVal = (IBaseConformance) encoding
				.newParser(ctx)
				.parseResource(output);
			myCapabilityStatement = retVal;
		}

		return retVal;
	}

}
