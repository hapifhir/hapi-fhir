package ca.uhn.fhir.jpa.dao.lastn;

import ca.uhn.fhir.jpa.dao.data.IObservationIndexedCodeCodingSearchParamDao;
import ca.uhn.fhir.jpa.dao.data.IObservationIndexedSearchParamLastNDao;
import ca.uhn.fhir.jpa.dao.lastn.entity.*;
import ca.uhn.fhir.jpa.model.cross.IBasePersistedResource;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.Observation;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.r4.model.DateTimeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import java.util.*;

@Transactional(propagation = Propagation.REQUIRED)
public class ObservationLastNIndexPersistDstu3Svc {

	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	protected EntityManager myEntityManager;

	@Autowired
	IObservationIndexedSearchParamLastNDao myResourceIndexedObservationLastNDao;

	@Autowired
	IObservationIndexedCodeCodingSearchParamDao myObservationIndexedCodeCodingSearchParamDao;

	public void indexObservation(Observation theObservation) {
		// Only index for lastn if Observation has a subject and effective date/time
		if(theObservation.getSubject() == null || !theObservation.hasEffective()) {
			return;
		}

		// Determine most recent effective date/time
		Date effectiveDtm = null;
		if (theObservation.hasEffectiveDateTimeType()) {
			effectiveDtm = theObservation.getEffectiveDateTimeType().getValue();
		} else if (theObservation.hasEffectivePeriod()) {
			effectiveDtm = theObservation.getEffectivePeriod().getEnd();
		}
		if (effectiveDtm == null) {
			return;
		}

		// Determine if an index already exists for Observation:
		boolean observationIndexUpdate = false;
		ObservationIndexedSearchParamLastNEntity indexedObservation = null;
		if (theObservation.hasId()) {
			indexedObservation = myResourceIndexedObservationLastNDao.findForIdentifier(theObservation.getIdElement().getIdPart());
		}
		if (indexedObservation == null) {
			indexedObservation = new ObservationIndexedSearchParamLastNEntity();
		} else {
			observationIndexUpdate = true;
		}
		indexedObservation.setEffectiveDtm(effectiveDtm);
		Reference subjectReference = theObservation.getSubject();
		String subjectId = subjectReference.getReference();
		String resourcePID = theObservation.getIdElement().getIdPart();
		indexedObservation.setIdentifier(resourcePID);
		indexedObservation.setSubject(subjectId);

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
		boolean observationCodeUpdate = false;
		for (Coding codeCoding : codeCodeableConcept.getCoding()) {
			if (codeCoding.hasCode() && codeCoding.hasSystem()) {
				observationCodeNormalizedId = myObservationIndexedCodeCodingSearchParamDao.findForCodeAndSystem(codeCoding.getCode(), codeCoding.getSystem());
			} else {
				observationCodeNormalizedId = myObservationIndexedCodeCodingSearchParamDao.findForDisplay(codeCoding.getDisplay());
			}
			if(observationCodeNormalizedId != null) {
				observationCodeUpdate = true;
				break;
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
		if (observationCodeUpdate) {
			myEntityManager.merge(codeableConceptField);
		} else {
			myEntityManager.persist(codeableConceptField);
		}

		indexedObservation.setObservationCode(codeableConceptField);
		indexedObservation.setCodeNormalizedId(observationCodeNormalizedId);
		if (observationIndexUpdate) {
			myEntityManager.merge(indexedObservation);
		} else {
			myEntityManager.persist(indexedObservation);
		}

	}

	public void deleteObservationIndex(IBasePersistedResource theEntity) {
		ObservationIndexedSearchParamLastNEntity deletedObservationLastNEntity = myResourceIndexedObservationLastNDao.findForIdentifier(theEntity.getIdDt().getIdPart());
		if (deletedObservationLastNEntity != null) {
			myEntityManager.remove(deletedObservationLastNEntity);
		}
	}

}
