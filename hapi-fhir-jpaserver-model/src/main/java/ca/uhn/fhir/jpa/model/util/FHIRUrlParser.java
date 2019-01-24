package ca.uhn.fhir.jpa.model.util;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.parser.DataFormatException;

public class FHIRUrlParser {
	/**
	 *
	 * @throws DataFormatException If the resource type is not known
	 */
	public static RuntimeResourceDefinition parseUrlResourceType(FhirContext theCtx, String theUrl) throws DataFormatException {
		int paramIndex = theUrl.indexOf('?');
		String resourceName = theUrl.substring(0, paramIndex);
		if (resourceName.contains("/")) {
			resourceName = resourceName.substring(resourceName.lastIndexOf('/') + 1);
		}
		return theCtx.getResourceDefinition(resourceName);
	}

}
