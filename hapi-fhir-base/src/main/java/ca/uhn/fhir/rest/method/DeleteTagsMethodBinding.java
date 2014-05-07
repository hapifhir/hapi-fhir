package ca.uhn.fhir.rest.method;

import java.lang.reflect.Method;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.DeleteTags;

public class DeleteTagsMethodBinding extends BaseAddOrDeleteTagsMethodBinding {

	public DeleteTagsMethodBinding(Method theMethod, FhirContext theConetxt, Object theProvider, DeleteTags theDeleteTags) {
		super(theMethod, theConetxt, theProvider, theDeleteTags.type());
	}

	@Override
	protected boolean isDelete() {
		return true;
	}

}
