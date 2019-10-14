package ca.uhn.fhir.jpa.term.api;

import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.ValueSet;

/**
 * This interface is used to handle differences in versions of FHIR for the terminology
 * server. It is really just an internal interface used by the
 * {@link ITermReadSvc terminology read service}.
 */
public interface ITermVersionAdapterSvc {

	IIdType createOrUpdateCodeSystem(CodeSystem theCodeSystemResource);

	void createOrUpdateConceptMap(ConceptMap theNextConceptMap);

	void createOrUpdateValueSet(ValueSet theValueSet);

}
