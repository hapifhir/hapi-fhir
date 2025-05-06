package ca.uhn.fhir.jpa.provider.validation.performance;

import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.ValueSet;

public class CreateTerminologyTestUtil {

	private CreateTerminologyTestUtil() {
		// utility class
	}

	public static CodeSystem createCodeSystem(String theId, String theTitle, CodeSystem.CodeSystemContentMode theContentMode, int theNumConcepts) {
		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setId(theId);
		codeSystem.setUrl("http://acme.org/CodeSystem/" + theId);
		codeSystem.setVersion("1");
		codeSystem.setTitle(theTitle);
		codeSystem.setName(theTitle);
		codeSystem.setStatus(Enumerations.PublicationStatus.ACTIVE);
		codeSystem.setDescription(theTitle + " - Description");
		codeSystem.setContent(theContentMode);

		for (int i = 1; i <= theNumConcepts; i++){
			CodeSystem.ConceptDefinitionComponent concept = codeSystem.addConcept();
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

		return codeSystem;
	}

	public static ValueSet createValueSetFromCodeSystemConcepts(String theId, String theTitle, CodeSystem theCodeSystem) {
		ValueSet valueSet = createValueSetWithMetaInfo(theId, theTitle);
		ValueSet.ConceptSetComponent include = valueSet.getCompose().addInclude();
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

		return valueSet;
	}

	public static ValueSet createValueSetFromCodeSystemUrl(String theId, String theTitle, String theCodeSystemUrl) {
		ValueSet valueSet = createValueSetWithMetaInfo(theId, theTitle);
		valueSet.getCompose().addInclude().setSystem(theCodeSystemUrl);
		return valueSet;
	}

	public static ValueSet createValueSetFromValueSets(String theId, String theTitle, ValueSet... theValueSets) {
		ValueSet combinedValueSet = createValueSetWithMetaInfo(theId, theTitle);
		ValueSet.ConceptSetComponent include = combinedValueSet.getCompose().addInclude();
		for (ValueSet valueSet : theValueSets){
			include.addValueSet(valueSet.getUrl());
		}
		return combinedValueSet;
	}

	private static ValueSet createValueSetWithMetaInfo(String theId, String theTitle) {
		ValueSet valueSet = new ValueSet();
		valueSet.setId(theId);
		valueSet.setUrl("http://acme.org/ValueSet/" + theId);
		valueSet.setVersion("1");
		valueSet.setName(theTitle);
		valueSet.setTitle(theTitle);
		valueSet.setStatus(Enumerations.PublicationStatus.ACTIVE);
		valueSet.setDescription(theTitle + " - Description");
		return valueSet;
	}
}
