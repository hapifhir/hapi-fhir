package ca.uhn.fhir.jpa.term.api;

import ca.uhn.fhir.jpa.api.model.TranslationRequest;
import ca.uhn.fhir.jpa.api.model.TranslationResult;
import ca.uhn.fhir.jpa.entity.TermConceptMapGroupElement;
import ca.uhn.fhir.jpa.entity.TermConceptMapGroupElementTarget;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import org.hl7.fhir.r4.model.ConceptMap;

import java.util.List;

public interface ITermConceptMappingSvc {


	TranslationResult translate(TranslationRequest theTranslationRequest);

	TranslationResult translateWithReverse(TranslationRequest theTranslationRequest);

	void deleteConceptMapAndChildren(ResourceTable theResourceTable);

	void storeTermConceptMapAndChildren(ResourceTable theResourceTable, ConceptMap theConceptMap);



}
