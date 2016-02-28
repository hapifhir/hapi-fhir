package ca.uhn.fhir.rest.client.api;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.util.VersionUtil;

public class HttpClientUtil {

	public static String createUserAgentString(FhirContext theContext, String theClientType) {
		StringBuilder b = new StringBuilder();
		b.append("HAPI-FHIR/");
		b.append(VersionUtil.getVersion());
		b.append(" (FHIR Client; FHIR ");
		b.append(theContext.getVersion().getVersion().name());
		b.append("; ");
		b.append(theClientType);
		b.append(")");
		return b.toString();
	}

}
