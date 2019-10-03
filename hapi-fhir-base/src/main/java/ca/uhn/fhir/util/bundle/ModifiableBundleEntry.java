package ca.uhn.fhir.util.bundle;

import ca.uhn.fhir.context.FhirContext;

public class ModifiableBundleEntry {
	private final BundleEntryParts myBundleEntryParts;
	private final BundleEntryMutator myBundleEntryMutator;

	public ModifiableBundleEntry(BundleEntryParts theBundleEntryParts, BundleEntryMutator theBundleEntryMutator) {
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
