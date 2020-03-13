package ca.uhn.fhir.empi.rules.metric;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.empi.rules.EmpiMatchFieldJson;
import ca.uhn.fhir.empi.rules.IEmpiMatcher;
import ca.uhn.fhir.util.FhirTerser;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import java.util.List;

public class EmpiResourceFieldComparator implements IEmpiMatcher<IBaseResource> {
	private final FhirContext myFhirContext;
	private final EmpiMatchFieldJson myEmpiMatchFieldJson;
	private final String myResourceType;
	private final String myResourcePath;

	public EmpiResourceFieldComparator(FhirContext theFhirContext, EmpiMatchFieldJson theEmpiMatchFieldJson) {
		myFhirContext = theFhirContext;
		myEmpiMatchFieldJson = theEmpiMatchFieldJson;
		myResourceType = theEmpiMatchFieldJson.getResourceType();
		myResourcePath = theEmpiMatchFieldJson.getResourcePath();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean match(IBaseResource theLeftResource, IBaseResource theRightResource) {
		validate(theLeftResource);
		validate(theRightResource);

		FhirTerser terser = myFhirContext.newTerser();
		List<IPrimitiveType> leftValues = terser.getValues(theLeftResource, myResourcePath, IPrimitiveType.class);
		List<IPrimitiveType> rightValues = terser.getValues(theRightResource, myResourcePath, IPrimitiveType.class);
		return match(leftValues, rightValues);
	}

	@SuppressWarnings("rawtypes")
	private boolean match(List<IPrimitiveType> theLeftValues, List<IPrimitiveType> theRightValues) {
	boolean retval = false;
		for (IPrimitiveType leftValue : theLeftValues) {
			String leftString = leftValue.getValueAsString();
			for (IPrimitiveType rightValue : theRightValues) {
				String rightString = rightValue.getValueAsString();
				retval |= myEmpiMatchFieldJson.match(leftString, rightString);
			}
		}
		return retval;
	}

	private void validate(IBaseResource theResource) {
		String resourceType = theResource.getIdElement().getResourceType();
		Validate.notNull(resourceType, "Resource type may not be null");
		Validate.isTrue(myResourceType.equals(resourceType),"Expecting resource type %s got resource type %s", myResourceType, resourceType);
	}
}
