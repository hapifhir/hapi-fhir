package ca.uhn.fhir.empi.rules.metric.matcher;

import ca.uhn.fhir.util.StringUtil;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

public abstract class BaseHapiStringMetric {
	protected String extractString(IPrimitiveType<?> thePrimitive, boolean theExact) {
		String theString = thePrimitive.getValueAsString();
		if (theExact) {
			return theString;
		}
		return StringUtil.normalizeStringForSearchIndexing(theString);
	}
}
