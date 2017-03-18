package org.hl7.fhir.convertors;

import org.hl7.fhir.dstu3.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.dstu3.model.CodeSystem;
import org.hl7.fhir.dstu3.model.ValueSet;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.Resource;

public class NullVersionConverterAdvisor implements VersionConvertorAdvisor {

	@Override
	public boolean ignoreEntry(BundleEntryComponent theSrc) {
		return false;
	}

	@Override
	public Resource convert(org.hl7.fhir.dstu3.model.Resource theResource) throws FHIRException {
		return null;
	}

	@Override
	public void handleCodeSystem(CodeSystem theTgtcs, ValueSet theSource) {
		//nothing
	}

	@Override
	public CodeSystem getCodeSystem(ValueSet theSrc) {
		return null;
	}

}
