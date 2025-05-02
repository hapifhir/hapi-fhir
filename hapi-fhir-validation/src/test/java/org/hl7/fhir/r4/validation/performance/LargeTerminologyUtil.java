package org.hl7.fhir.r4.validation.performance;

import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.ValueSet;

public class LargeTerminologyUtil {

	private LargeTerminologyUtil() {
		// utility class
	}

	public static CodeSystem createCodeSystem(String theId, String theTitle, CodeSystem.CodeSystemContentMode theContentMode, int theNumConcepts) {
		CodeSystem largeCodeSystem = new CodeSystem();
		largeCodeSystem.setId(theId);
		largeCodeSystem.setUrl("http://acme.org/CodeSystem/" + theId);
		largeCodeSystem.setVersion("1");
		largeCodeSystem.setTitle(theTitle);
		largeCodeSystem.setName(theTitle);
		largeCodeSystem.setStatus(Enumerations.PublicationStatus.ACTIVE);
		largeCodeSystem.setDescription(theTitle + " - Description");
		largeCodeSystem.setContent(theContentMode);

		for (int i = 1; i <= theNumConcepts; i++){
			CodeSystem.ConceptDefinitionComponent concept = largeCodeSystem.addConcept();
			concept
				.setCode(theId + "-concept-" + i)
				.setDisplay(theTitle + " Concept " + i)
				.addDesignation()
				.setLanguage("fr")
				.setValue("Le " + theTitle + " Concept " + i)
				.getUse()
				.setSystem("http://terminology.hl7.org/CodeSystem/hl7TermMaintInfra")
				.setCode("preferredForLanguage")
				.setDisplay("Preferred For Language");
		}

		return largeCodeSystem;
	}

	public static ValueSet createValueSetFromCodeSystem(String theId, String theTitle, CodeSystem theCodeSystem) {
		ValueSet largeValueSet = new ValueSet();
		largeValueSet.setId(theId);
		largeValueSet.setUrl("http://acme.org/ValueSet/" + theId);
		largeValueSet.setVersion("1");
		largeValueSet.setName(theTitle);
		largeValueSet.setTitle(theTitle);
		largeValueSet.setStatus(Enumerations.PublicationStatus.ACTIVE);
		largeValueSet.setDescription(theTitle + " - Description");
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
