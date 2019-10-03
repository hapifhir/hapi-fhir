package ca.uhn.fhir.util.bundle;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementCompositeDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.util.ParametersUtil;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

public class ModifiableBundleEntry {
	private final BundleEntryParts myBundleEntryParts;
	private final BundleEntryMutator myBundleEntryMutator;

	ModifiableBundleEntry(BundleEntryParts theBundleEntryParts, BundleEntryMutator theBundleEntryMutator) {
		myBundleEntryParts = theBundleEntryParts;
		myBundleEntryMutator = theBundleEntryMutator;
	}

	BundleEntryParts getBundleEntryParts() {
		return myBundleEntryParts;
	}

	public void setRequestUrl(FhirContext theFhirContext, String theRequestUrl) {
		myBundleEntryMutator.setRequestUrl(theFhirContext, theRequestUrl);
	}

	public String getRequestUrl() {
		return myBundleEntryParts.getUrl();
	}
}
