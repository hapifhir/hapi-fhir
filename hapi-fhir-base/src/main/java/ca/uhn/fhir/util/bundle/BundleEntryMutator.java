package ca.uhn.fhir.util.bundle;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementCompositeDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.util.ParametersUtil;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

public class BundleEntryMutator {
	private final IBase myEntry;
	private final BaseRuntimeChildDefinition myRequestChildDef;
	private final BaseRuntimeElementCompositeDefinition<?> myRequestChildContentsDef;

	public BundleEntryMutator(IBase theEntry, BaseRuntimeChildDefinition theRequestChildDef, BaseRuntimeElementCompositeDefinition<?> theRequestChildContentsDef) {
		myEntry = theEntry;
		myRequestChildDef = theRequestChildDef;
		myRequestChildContentsDef = theRequestChildContentsDef;
	}

	void setRequestUrl(FhirContext theFhirContext, String theRequestUrl) {
		BaseRuntimeChildDefinition requestUrlChildDef = myRequestChildContentsDef.getChildByName("url");
		IPrimitiveType<?> url = ParametersUtil.createUri(theFhirContext, theRequestUrl);
		for (IBase nextRequest : myRequestChildDef.getAccessor().getValues(myEntry)) {
			requestUrlChildDef.getMutator().addValue(nextRequest, url);
		}
	}
}
