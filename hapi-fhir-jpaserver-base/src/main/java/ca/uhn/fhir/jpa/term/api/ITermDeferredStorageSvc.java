package ca.uhn.fhir.jpa.term.api;

import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.entity.TermConceptParentChildLink;
import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.ValueSet;

import java.util.List;

/**
 * This service handles processing "deferred" concept writes, meaning concepts that have neen
 * queued for storage because there are too many of them.
 */
public interface ITermDeferredStorageSvc {

	void saveDeferred();

	boolean isStorageQueueEmpty();

	/**
	 * This is mostly for unit tests - we can disable processing of deferred concepts
	 * by changing this flag
	 */
	void setProcessDeferred(boolean theProcessDeferred);

	void addConceptToStorageQueue(TermConcept theConcept);

	void addConceptLinkToStorageQueue(TermConceptParentChildLink theConceptLink);

	void addConceptMapsToStorageQueue(List<ConceptMap> theConceptMaps);

	void addValueSetsToStorageQueue(List<ValueSet> theValueSets);
}
