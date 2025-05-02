package org.hl7.fhir.r4.validation.performance;

import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.ValueSet;

public class LargeTerminologyUtil {

	private LargeTerminologyUtil() {
		// utility class
	}

	public static CodeSystem createLargeCodeSystem(int theNumConcepts) {
		CodeSystem largeCodeSystem = new CodeSystem();
		largeCodeSystem.setId("large-codesystem");
		largeCodeSystem.setUrl("http://acme.org/CodeSystem/large-codesystem");
		largeCodeSystem.setVersion("1");
		largeCodeSystem.setName("Large CodeSystem");
		largeCodeSystem.setTitle("Large CodeSystem");
		largeCodeSystem.setStatus(Enumerations.PublicationStatus.ACTIVE);
		largeCodeSystem.setDescription("Some Large CodeSystem");
		largeCodeSystem.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);

		for (int i = 1; i <= theNumConcepts; i++){
			CodeSystem.ConceptDefinitionComponent concept = largeCodeSystem.addConcept();
			concept
				.setCode("concept-" + i)
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

	public static ValueSet createLargeValueSet(CodeSystem theCodeSystem) {
		ValueSet largeValueSet = new ValueSet();
		largeValueSet.setId("large-valueset");
		largeValueSet.setUrl("http://acme.org/ValueSet/large-valueset");
		largeValueSet.setVersion("1");
		largeValueSet.setName("Large ValueSet");
		largeValueSet.setTitle("Large ValueSet");
		largeValueSet.setStatus(Enumerations.PublicationStatus.ACTIVE);
		largeValueSet.setDescription("Some Large ValueSet");
		ValueSet.ConceptSetComponent include = largeValueSet.getCompose().addInclude();
		include.setSystem(theCodeSystem.getUrl());

		for (CodeSystem.ConceptDefinitionComponent concept : theCodeSystem.getConcept()){
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
}
