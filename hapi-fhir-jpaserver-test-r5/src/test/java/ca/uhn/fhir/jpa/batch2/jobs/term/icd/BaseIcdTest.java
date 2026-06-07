package ca.uhn.fhir.jpa.batch2.jobs.term.icd;

import org.hl7.fhir.r4.model.CodeSystem;

import java.util.List;

public class BaseIcdTest {

	protected List<String> listCodes(List<CodeSystem.ConceptDefinitionComponent> theConcept) {
		return theConcept.stream().map(CodeSystem.ConceptDefinitionComponent::getCode).toList();
	}

}
