// Created by claude-sonnet-4-5
package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This test validates that ValueSet expansion correctly handles case-sensitive
 * CodeSystems where codes differ only by case (e.g., "drug" vs "Drug").
 *
 * The test uses FRAGMENT content mode to reproduce the bug scenario where
 * a user-defined CodeSystem should override the built-in HL7 CodeSystem.
 */
class ValueSetExpansionCaseSensitiveManualTest extends BaseJpaR4Test {

	private static final Logger ourLog = LoggerFactory.getLogger(ValueSetExpansionCaseSensitiveManualTest.class);

	@Test
	void testValueSetExpansion_CaseSensitive_IncludeEntireSystem() {
		// Create case-sensitive CodeSystem with FRAGMENT content mode
		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setUrl("http://terminology.hl7.org/CodeSystem/insurance-plan-type");
		codeSystem.setStatus(Enumerations.PublicationStatus.ACTIVE);
		codeSystem.setCaseSensitive(true);
		codeSystem.setContent(CodeSystem.CodeSystemContentMode.FRAGMENT);
		codeSystem.setVersion("4.0.1");

		// Add all the codes from the real HL7 resource
		codeSystem.addConcept().setCode("medical").setDisplay("Medical");
		codeSystem.addConcept().setCode("dental").setDisplay("Dental");
		codeSystem.addConcept().setCode("mental").setDisplay("Mental Health");
		codeSystem.addConcept().setCode("subst-ab").setDisplay("Substance Abuse");
		codeSystem.addConcept().setCode("vision").setDisplay("Vision");
		codeSystem.addConcept().setCode("drug").setDisplay("Drug");  // lowercase - active
		codeSystem.addConcept().setCode("short-term").setDisplay("Short Term");
		codeSystem.addConcept().setCode("long-term").setDisplay("Long Term Care");
		codeSystem.addConcept().setCode("hospice").setDisplay("Hospice");
		codeSystem.addConcept().setCode("home").setDisplay("Home Health");

		// Add "Drug" (uppercase D) - retired
		CodeSystem.ConceptDefinitionComponent drugRetired = codeSystem.addConcept();
		drugRetired.setCode("Drug");
		drugRetired.setDisplay("Drug");
		CodeSystem.ConceptPropertyComponent statusProp = drugRetired.addProperty();
		statusProp.setCode("status");
		statusProp.setValue(new CodeType("retired"));
		CodeSystem.ConceptPropertyComponent inactiveProp = drugRetired.addProperty();
		inactiveProp.setCode("inactive");
		inactiveProp.setValue(new CodeType("true"));

		myCodeSystemDao.create(codeSystem, mySrd);
		myTerminologyDeferredStorageSvc.saveAllDeferred();

		// Create ValueSet that includes the entire CodeSystem (NOT enumerated concepts)
		ValueSet valueSet = new ValueSet();
		valueSet.setUrl("http://terminology.hl7.org/ValueSet/insuranceplan-type");
		valueSet.setStatus(Enumerations.PublicationStatus.ACTIVE);

		ValueSet.ConceptSetComponent include = valueSet.getCompose().addInclude();
		include.setSystem("http://terminology.hl7.org/CodeSystem/insurance-plan-type");
		// CRITICAL: Do NOT enumerate specific concepts - include entire system

		myValueSetDao.create(valueSet, mySrd);

		// Act: Expand the ValueSet
		ValueSet expanded = myTermSvc.expandValueSet(null, valueSet);

		// Assert: Both case variants should be present
		List<String> codes = expanded.getExpansion().getContains().stream()
			.map(ValueSet.ValueSetExpansionContainsComponent::getCode)
			.collect(Collectors.toList());

		ourLog.info("Expanded ValueSet contains {} codes: {}", codes.size(), codes);

		assertThat(codes)
			.as("ValueSet expansion should include both case variants")
			.contains("Drug", "drug");
	}
}
