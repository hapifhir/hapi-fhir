package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.jpa.term.api.ITermVersionAdapterSvc;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.ValueSet;

public class TermVersionAdapterSvcDstu2 implements ITermVersionAdapterSvc {

	@Override
	public IIdType createOrUpdateCodeSystem(CodeSystem theCodeSystemResource) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void createOrUpdateConceptMap(ConceptMap theNextConceptMap) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void createOrUpdateValueSet(ValueSet theValueSet) {
		throw new UnsupportedOperationException();
	}

}
