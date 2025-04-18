package org.hl7.fhir.r4.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class GenerateLargeTerminologyTest {

	private static final FhirContext ourFhirContext = FhirContext.forR4Cached();
	private static final IParser ourParser = ourFhirContext.newJsonParser().setPrettyPrint(true);

	@Test
	@Disabled
	public void createLargeCodeSystemAndValueSet(){
		int concepts = 100_000;

		CodeSystem largeCodeSystem = createLargeCodeSystem(concepts);
		String largeCodeSystemJson = ourParser.encodeResourceToString(largeCodeSystem);

		ValueSet largeValueSet = createLargeValueSet(largeCodeSystem);
		String largeValueSetJson = ourParser.encodeToString(largeValueSet);
	}

	private static ValueSet createLargeValueSet(CodeSystem theLargeCodeSystem) {
		ValueSet largeValueSet = new ValueSet();
		largeValueSet.setId("large-valueset");
		largeValueSet.setUrl("http://acme.org/ValueSet/large-valueset");
		largeValueSet.setVersion("1");
		largeValueSet.setName("Large ValueSet");
		largeValueSet.setTitle("Large ValueSet");
		largeValueSet.setStatus(Enumerations.PublicationStatus.ACTIVE);
		largeValueSet.setDescription("Some Large ValueSet");
		ValueSet.ConceptSetComponent include = largeValueSet.getCompose().addInclude();
		include.setSystem(theLargeCodeSystem.getUrl());

		for (CodeSystem.ConceptDefinitionComponent concept : theLargeCodeSystem.getConcept()){
			CodeSystem.ConceptDefinitionDesignationComponent designation = concept.getDesignationFirstRep();
			include
				.addConcept()
				.setCode(concept.getCode())
				.setDisplay(concept.getDisplay())
				.addDesignation()
				.setLanguage(designation.getLanguage())
				.setValue(designation.getValue())
				.getUse()
				.setSystem(designation.getUse().getSystem())
				.setCode(designation.getUse().getCode())
				.setDisplay(designation.getUse().getDisplay());
		}

		return largeValueSet;
	}

	private static CodeSystem createLargeCodeSystem(int theConcepts) {
		CodeSystem largeCodeSystem = new CodeSystem();
		largeCodeSystem.setId("large-codesystem");
		largeCodeSystem.setUrl("http://acme.org/CodeSystem/large-codesystem");
		largeCodeSystem.setVersion("1");
		largeCodeSystem.setName("Large CodeSystem");
		largeCodeSystem.setTitle("Large CodeSystem");
		largeCodeSystem.setStatus(Enumerations.PublicationStatus.ACTIVE);
		largeCodeSystem.setDescription("Some Large CodeSystem");
		largeCodeSystem.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);

		for (int i = 1; i <= theConcepts; i++){
			CodeSystem.ConceptDefinitionComponent concept = largeCodeSystem.addConcept();
			concept
				.setCode("concept-"+i)
				.setDisplay("Concept " + i)
				.addDesignation()
				.setLanguage("fr")
				.setValue("Le Concept " + i)
				.getUse()
				.setSystem("http://terminology.hl7.org/CodeSystem/hl7TermMaintInfra")
				.setCode("preferredForLanguage")
				.setDisplay("Preferred For Language");
		}

		return largeCodeSystem;
	}

	@Test
	public void testing(){
		String str = getString1();
	}

	private String getString1() {
		return getString2();
	}

	private String getString2() {
		return getString3();
	}

	private String getString3() {
		return "Hello world";
	}
}
