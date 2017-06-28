package ca.uhn.fhir.rest.client.method;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.SummaryEnum;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;

public class SummaryEnumParameter implements IParameter {

	@SuppressWarnings("unchecked")
	@Override
	public void translateClientArgumentIntoQueryArgument(FhirContext theContext, Object theSourceClientArgument, Map<String, List<String>> theTargetQueryArguments, IBaseResource theTargetResource)
			throws InternalErrorException {
		if (theSourceClientArgument instanceof Collection) {
			List<String> values = new ArrayList<String>();
			for (SummaryEnum next : (Collection<SummaryEnum>) theSourceClientArgument) {
				if (next != null) {
					values.add(next.getCode());
				}
			}
			theTargetQueryArguments.put(Constants.PARAM_SUMMARY, values);
		} else {
			SummaryEnum ss = (SummaryEnum) theSourceClientArgument;
			if (ss != null) {
				theTargetQueryArguments.put(Constants.PARAM_SUMMARY, Collections.singletonList(ss.getCode()));
			}
		}
	}

	@Override
	public void initializeTypes(Method theMethod, Class<? extends Collection<?>> theOuterCollectionType, Class<? extends Collection<?>> theInnerCollectionType, Class<?> theParameterType) {
		if (theOuterCollectionType != null) {
			throw new ConfigurationException("Method '" + theMethod.getName() + "' in type '" + theMethod.getDeclaringClass().getCanonicalName() + "' is of type " + SummaryEnum.class
					+ " but can not be a collection of collections");
		}
	}

}
