package org.hl7.fhir.r6.hapi.ctx;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.util.VersionUtil;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.model.IModelContext;
import org.hl7.fhir.model.ModelContextInformation;

public class HapiModelContext implements IModelContext {

	private final FhirContext myFhirContext;

	public HapiModelContext(FhirContext theFhirContext) {
		myFhirContext = theFhirContext;
	}

	@Nonnull
	@Override
	public String getFHIRVersion() {
		return myFhirContext.getVersion().getVersion().getFhirVersionString();
	}

	@Nonnull
	@Override
	public ModelContextInformation getContextInformation() {
		return null;
	}

	@Override
	public boolean isCompatibleModelContext(IModelContext modelContext) {
		return false;
	}

	@Override
	public String describeContext() {
		return "HAPI FHIR " + VersionUtil.getVersion();
	}
}
