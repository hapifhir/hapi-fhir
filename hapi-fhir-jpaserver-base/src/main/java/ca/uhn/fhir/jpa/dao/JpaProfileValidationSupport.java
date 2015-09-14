package ca.uhn.fhir.jpa.dao;

import org.hl7.fhir.instance.model.ValueSet;
import org.hl7.fhir.instance.model.ValueSet.ConceptSetComponent;
import org.hl7.fhir.instance.model.ValueSet.ValueSetExpansionComponent;
import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.validation.IValidationSupport;


public class JpaProfileValidationSupport implements IValidationSupport {

	@Override
	public ValueSetExpansionComponent expandValueSet(ConceptSetComponent theInclude) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ValueSet fetchCodeSystem(String theSystem) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends IBaseResource> T fetchResource(FhirContext theContext, Class<T> theClass, String theUri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isCodeSystemSupported(String theSystem) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public CodeValidationResult validateCode(String theCodeSystem, String theCode, String theDisplay) {
		// TODO Auto-generated method stub
		return null;
	}

}
