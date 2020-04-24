package ca.uhn.fhir.jpa.dao.lastn;

import ca.uhn.fhir.context.*;
import ca.uhn.fhir.jpa.dao.data.IObservationIndexedCodeCodingSearchParamDao;
import ca.uhn.fhir.jpa.dao.data.IObservationIndexedSearchParamLastNDao;
import ca.uhn.fhir.jpa.dao.lastn.entity.*;
import ca.uhn.fhir.jpa.model.cross.IBasePersistedResource;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamToken;
import ca.uhn.fhir.jpa.searchparam.extractor.ISearchParamExtractor;
import ca.uhn.fhir.jpa.searchparam.extractor.PathAndRef;
import org.hl7.fhir.instance.model.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import java.util.*;

@Transactional(propagation = Propagation.REQUIRED)
public class ObservationLastNIndexPersistSvc {

	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	protected EntityManager myEntityManager;

	@Autowired
	IObservationIndexedSearchParamLastNDao myResourceIndexedObservationLastNDao;

	@Autowired
	IObservationIndexedCodeCodingSearchParamDao myObservationIndexedCodeCodingSearchParamDao;

	@Autowired
	public ISearchParamExtractor mySearchParameterExtractor;

	public void indexObservation(IBaseResource theResource) {

		String subjectId = null;
		List<IBase> subjectReferenceElement = mySearchParameterExtractor.extractValues("Observation.subject", theResource);
		if (subjectReferenceElement.size() == 1) {
			PathAndRef subjectPathAndRef = mySearchParameterExtractor.extractReferenceLinkFromResource(subjectReferenceElement.get(0), "Observation.subject");
			if (subjectPathAndRef != null) {
				IBaseReference subjectReference = subjectPathAndRef.getRef();
				if (subjectReference != null) {
					subjectId = subjectReference.getReferenceElement().getValue();
				}
			}
		}

		Date effectiveDtm = null;
		List<IBase> effectiveDateElement = mySearchParameterExtractor.extractValues("Observation.effective", theResource);
		if (effectiveDateElement.size() == 1) {
			effectiveDtm = mySearchParameterExtractor.extractDateFromResource(effectiveDateElement.get(0), "Observation.effective");
		}

		// Build CodeableConcept entity for Observation.Code.
		List<IBase> observationCodeCodeableConcepts = mySearchParameterExtractor.extractValues("Observation.code", theResource);


		// Only index for lastn if Observation has a subject, effective date/time and code
		if (subjectId == null || effectiveDtm == null || observationCodeCodeableConcepts.size() == 0) {
			return;
		}

		String resourcePID = theResource.getIdElement().getIdPart();

		// Determine if an index already exists for Observation:
		boolean observationIndexUpdate = false;
		ObservationIndexedSearchParamLastNEntity indexedObservation = null;
		if (resourcePID != null) {
			indexedObservation = myResourceIndexedObservationLastNDao.findForIdentifier(resourcePID);
		}
		if (indexedObservation == null) {
			indexedObservation = new ObservationIndexedSearchParamLastNEntity();
		} else {
			observationIndexUpdate = true;
		}

		indexedObservation.setEffectiveDtm(effectiveDtm);
		indexedObservation.setIdentifier(resourcePID);
		indexedObservation.setSubject(subjectId);


		// Determine if a Normalized ID was created previously for Observation Code
		boolean observationCodeUpdate = false;
		String observationCodeNormalizedId = getCodeCodeableConceptIdIfExists(observationCodeCodeableConcepts.get(0));
		if (observationCodeNormalizedId != null) {
			observationCodeUpdate = true;
		}
		// Generate a new a normalized ID if necessary
		if (observationCodeNormalizedId == null) {
			observationCodeNormalizedId = UUID.randomUUID().toString();
		}

		// Create/update normalized Observation Code index record
		ObservationIndexedCodeCodeableConceptEntity codeableConceptField = getCodeCodeableConcept(observationCodeCodeableConcepts.get(0), observationCodeNormalizedId);

		// Build CodeableConcept entities for Observation.Category
		List<IBase> observationCategoryCodeableConcepts = mySearchParameterExtractor.extractValues("Observation.category", theResource);
		Set<ObservationIndexedCategoryCodeableConceptEntity> categoryCodeableConceptEntities = new HashSet<>();
		for (IBase categoryCodeableConcept : observationCategoryCodeableConcepts) {
			// Build CodeableConcept entities for each category CodeableConcept
			categoryCodeableConceptEntities.add(getCategoryCodeableConceptEntities(categoryCodeableConcept));
		}
		indexedObservation.setCategoryCodeableConcepts(categoryCodeableConceptEntities);

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

	private ObservationIndexedCategoryCodeableConceptEntity getCategoryCodeableConceptEntities(IBase theValue) {
		String text = mySearchParameterExtractor.getDisplayTextFromCodeableConcept(theValue);
		ObservationIndexedCategoryCodeableConceptEntity categoryCodeableConcept = new ObservationIndexedCategoryCodeableConceptEntity(text);

		List<IBase> codings = mySearchParameterExtractor.getCodingsFromCodeableConcept(theValue);
		Set<ObservationIndexedCategoryCodingEntity> categoryCodingEntities = new HashSet<>();
		for (IBase nextCoding : codings) {
			categoryCodingEntities.add(getCategoryCoding(nextCoding));
		}

		categoryCodeableConcept.setObservationIndexedCategoryCodingEntitySet(categoryCodingEntities);

		return categoryCodeableConcept;
	}

	private ObservationIndexedCodeCodeableConceptEntity getCodeCodeableConcept(IBase theValue, String observationCodeNormalizedId) {
		String text = mySearchParameterExtractor.getDisplayTextFromCodeableConcept(theValue);
		ObservationIndexedCodeCodeableConceptEntity codeCodeableConcept = new ObservationIndexedCodeCodeableConceptEntity(text, observationCodeNormalizedId);

		List<IBase> codings = mySearchParameterExtractor.getCodingsFromCodeableConcept(theValue);
		for (IBase nextCoding : codings) {
			codeCodeableConcept.addCoding(getCodeCoding(nextCoding, observationCodeNormalizedId));
		}

		return codeCodeableConcept;
	}

	private String getCodeCodeableConceptIdIfExists(IBase theValue) {
		List<IBase> codings = mySearchParameterExtractor.getCodingsFromCodeableConcept(theValue);
		String codeCodeableConceptId = null;
		for (IBase nextCoding : codings) {
			ResourceIndexedSearchParamToken param = mySearchParameterExtractor.createSearchParamForCoding("Observation",
				new RuntimeSearchParam(null, null, "code", null, null, null, null, null, null, null),
				nextCoding);
			if (param != null) {
				String system = param.getSystem();
				String code = param.getValue();
				String text = mySearchParameterExtractor.getDisplayTextForCoding(nextCoding);
				if (code != null && system != null) {
					codeCodeableConceptId = myObservationIndexedCodeCodingSearchParamDao.findForCodeAndSystem(code, system);
				} else {
					codeCodeableConceptId = myObservationIndexedCodeCodingSearchParamDao.findForDisplay(text);
				}
				if (codeCodeableConceptId != null) {
					break;
				}
			}
		}

		return codeCodeableConceptId;
	}

	private ObservationIndexedCategoryCodingEntity getCategoryCoding(IBase theValue) {
		ResourceIndexedSearchParamToken param = mySearchParameterExtractor.createSearchParamForCoding("Observation",
			new RuntimeSearchParam(null, null, "category", null, null, null, null, null, null, null),
			theValue);
		ObservationIndexedCategoryCodingEntity observationIndexedCategoryCodingEntity = null;
		if (param != null) {
			String system = param.getSystem();
			String code = param.getValue();
			String text = mySearchParameterExtractor.getDisplayTextForCoding(theValue);
			observationIndexedCategoryCodingEntity = new ObservationIndexedCategoryCodingEntity(system, code, text);
		}
		return observationIndexedCategoryCodingEntity;
	}

	private ObservationIndexedCodeCodingEntity getCodeCoding(IBase theValue, String observationCodeNormalizedId) {
		ResourceIndexedSearchParamToken param = mySearchParameterExtractor.createSearchParamForCoding("Observation",
			new RuntimeSearchParam(null, null, "code", null, null, null, null, null, null, null),
			theValue);
		ObservationIndexedCodeCodingEntity observationIndexedCodeCodingEntity = null;
		if (param != null) {
			String system = param.getSystem();
			String code = param.getValue();
			String text = mySearchParameterExtractor.getDisplayTextForCoding(theValue);
			observationIndexedCodeCodingEntity = new ObservationIndexedCodeCodingEntity(system, code, text, observationCodeNormalizedId);
		}
		return observationIndexedCodeCodingEntity;
	}

	public void deleteObservationIndex(IBasePersistedResource theEntity) {
		ObservationIndexedSearchParamLastNEntity deletedObservationLastNEntity = myResourceIndexedObservationLastNDao.findForIdentifier(theEntity.getIdDt().getIdPart());
		if (deletedObservationLastNEntity != null) {
			myEntityManager.remove(deletedObservationLastNEntity);
		}
	}

}
