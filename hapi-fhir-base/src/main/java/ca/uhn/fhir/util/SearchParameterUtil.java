package ca.uhn.fhir.util;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import java.util.ArrayList;
import java.util.List;

public class SearchParameterUtil {

	public static List<String> getBaseAsStrings(FhirContext theContext, IBaseResource theResource) {
		Validate.notNull(theContext, "theContext must not be null");
		Validate.notNull(theResource, "theResource must not be null");
		RuntimeResourceDefinition def = theContext.getResourceDefinition(theResource);

		BaseRuntimeChildDefinition base = def.getChildByName("base");
		List<IBase> baseValues = base.getAccessor().getValues(theResource);
		List<String> retVal = new ArrayList<>();
		for (IBase next : baseValues) {
			IPrimitiveType<?> nextPrimitive = (IPrimitiveType<?>) next;
			retVal.add(nextPrimitive.getValueAsString());
		}

		return retVal;
	}

}
