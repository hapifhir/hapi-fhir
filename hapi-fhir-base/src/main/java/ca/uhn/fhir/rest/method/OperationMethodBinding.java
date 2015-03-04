package ca.uhn.fhir.rest.method;

import static org.apache.commons.lang3.StringUtils.*;

import org.hl7.fhir.instance.model.api.IBaseParameters;

import ca.uhn.fhir.context.FhirContext;

public class OperationMethodBinding {

	public static HttpPostClientInvocation createOperationInvocation(FhirContext theContext, String theResourceName, String theId, String theOperationName, IBaseParameters theInput) {
		StringBuilder b = new StringBuilder();
		if (theResourceName != null) {
			b.append(theResourceName);
			if (isNotBlank(theId)) {
				b.append('/');
				b.append(theId);
			}
		}
		if (b.length() > 0) {
			b.append('/');
		}
		if (!theOperationName.startsWith("$")) {
			b.append("$");
		}
		b.append(theOperationName);
		
		return new HttpPostClientInvocation(theContext, theInput, b.toString());
	}

}
