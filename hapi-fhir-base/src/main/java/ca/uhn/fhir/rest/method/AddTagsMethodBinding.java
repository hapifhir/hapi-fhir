package ca.uhn.fhir.rest.method;

import java.lang.reflect.Method;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.AddTags;

public class AddTagsMethodBinding extends BaseAddOrDeleteTagsMethodBinding {

	public AddTagsMethodBinding(Method theMethod, FhirContext theConetxt, Object theProvider, AddTags theAnnotation) {
		super(theMethod, theConetxt, theProvider, theAnnotation.type());
	}

	@Override
	protected boolean isDelete() {
		return false;
	}

}
