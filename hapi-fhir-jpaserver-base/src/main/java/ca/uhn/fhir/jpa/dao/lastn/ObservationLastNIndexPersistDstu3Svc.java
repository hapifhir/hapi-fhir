package ca.uhn.fhir.jpa.dao.lastn;

import ca.uhn.fhir.jpa.dao.data.IObservationIndexedCodeCodeableConceptSearchParamDao;
import ca.uhn.fhir.jpa.dao.data.IObservationIndexedCodeCodingSearchParamDao;
import ca.uhn.fhir.jpa.dao.data.IObservationIndexedSearchParamLastNDao;
import ca.uhn.fhir.jpa.dao.lastn.entity.*;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.Observation;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ObservationLastNIndexPersistDstu3Svc {

	@Autowired
	IObservationIndexedSearchParamLastNDao myResourceIndexedObservationLastNDao;

   @Autowired
	IObservationIndexedCodeCodeableConceptSearchParamDao myObservationIndexedCodeableConceptSearchParamDao;

   @Autowired
	IObservationIndexedCodeCodingSearchParamDao myObservationIndexedCodeCodingSearchParamDao;

   // TODO: Change theSubjectId to be a Long
   public void indexObservation(Observation theObservation, String theSubjectId) {
		ObservationIndexedSearchParamLastNEntity indexedObservation = new ObservationIndexedSearchParamLastNEntity();
		String resourcePID = theObservation.getIdElement().getIdPart();
		indexedObservation.setIdentifier(resourcePID);
		indexedObservation.setSubject(theSubjectId);
		Date effectiveDtm = theObservation.getEffectiveDateTimeType().getValue();
		indexedObservation.setEffectiveDtm(effectiveDtm);

		// Build CodeableConcept entities for Observation.Category
		Set<ObservationIndexedCategoryCodeableConceptEntity> categoryConcepts = new HashSet<>();
		for(CodeableConcept categoryCodeableConcept : theObservation.getCategory()) {
			// Build Coding entities for each category CodeableConcept
			Set<ObservationIndexedCategoryCodingEntity> categoryCodingEntities = new HashSet<>();
			ObservationIndexedCategoryCodeableConceptEntity categoryCodeableConceptEntity = new ObservationIndexedCategoryCodeableConceptEntity(categoryCodeableConcept.getText());
			for(Coding categoryCoding : categoryCodeableConcept.getCoding()){
				categoryCodingEntities.add(new ObservationIndexedCategoryCodingEntity(categoryCoding.getSystem(), categoryCoding.getCode(), categoryCoding.getDisplay()));
			}
			categoryCodeableConceptEntity.setObservationIndexedCategoryCodingEntitySet(categoryCodingEntities);
			categoryConcepts.add(categoryCodeableConceptEntity);
		}
		indexedObservation.setCategoryCodeableConcepts(categoryConcepts);

		// Build CodeableConcept entity for Observation.Code.
		CodeableConcept codeCodeableConcept = theObservation.getCode();
		String observationCodeNormalizedId = null;

		// Determine if a Normalized ID was created previously for Observation Code
		for (Coding codeCoding : codeCodeableConcept.getCoding()) {
			if (codeCoding.hasCode() && codeCoding.hasSystem()) {
				observationCodeNormalizedId = myObservationIndexedCodeCodingSearchParamDao.findForCodeAndSystem(codeCoding.getCode(), codeCoding.getSystem());
			} else {
				observationCodeNormalizedId = myObservationIndexedCodeCodingSearchParamDao.findForDisplay(codeCoding.getDisplay());
			}
		}
		// Generate a new a normalized ID if necessary
		if (observationCodeNormalizedId == null) {
			observationCodeNormalizedId = UUID.randomUUID().toString();
		}

		// Create/update normalized Observation Code index record
		ObservationIndexedCodeCodeableConceptEntity codeableConceptField = new ObservationIndexedCodeCodeableConceptEntity(codeCodeableConcept.getText(), observationCodeNormalizedId);
		for (Coding codeCoding : codeCodeableConcept.getCoding()) {
			codeableConceptField.addCoding(new ObservationIndexedCodeCodingEntity(codeCoding.getSystem(), codeCoding.getCode(), codeCoding.getDisplay(), observationCodeNormalizedId));
		}
		myObservationIndexedCodeableConceptSearchParamDao.save(codeableConceptField);

		indexedObservation.setObservationCode(codeableConceptField);
		indexedObservation.setCodeNormalizedId(observationCodeNormalizedId);
		myResourceIndexedObservationLastNDao.save(indexedObservation);

	}

}
