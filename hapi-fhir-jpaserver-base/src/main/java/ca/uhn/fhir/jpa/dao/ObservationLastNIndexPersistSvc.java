package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.context.*;
import ca.uhn.fhir.jpa.dao.data.IObservationIndexedCodeCodingSearchParamDao;
import ca.uhn.fhir.jpa.dao.data.IObservationIndexedSearchParamLastNDao;
import ca.uhn.fhir.jpa.model.cross.IBasePersistedResource;
import ca.uhn.fhir.jpa.model.entity.ObservationIndexedCategoryCodeableConceptEntity;
import ca.uhn.fhir.jpa.model.entity.ObservationIndexedCategoryCodingEntity;
import ca.uhn.fhir.jpa.model.entity.ObservationIndexedCodeCodeableConceptEntity;
import ca.uhn.fhir.jpa.model.entity.ObservationIndexedCodeCodingEntity;
import ca.uhn.fhir.jpa.model.entity.ObservationIndexedSearchParamLastNEntity;
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

		List<IBase> subjectReferenceElement = mySearchParameterExtractor.extractValues("Observation.subject", theResource);
		String subjectId = subjectReferenceElement.stream()
			.map(refElement -> mySearchParameterExtractor.extractReferenceLinkFromResource(refElement, "Observation.subject"))
			.filter(Objects::nonNull)
			.map(PathAndRef::getRef)
			.filter(Objects::nonNull)
			.map(subjectRef -> subjectRef.getReferenceElement().getValue())
			.filter(Objects::nonNull)
			.findFirst().orElse(null);

		Date effectiveDtm = null;
		List<IBase> effectiveDateElement = mySearchParameterExtractor.extractValues("Observation.effective", theResource);
		if (effectiveDateElement.size() > 0) {
			effectiveDtm = mySearchParameterExtractor.extractDateFromResource(effectiveDateElement.get(0), "Observation.effective");
		}

		List<IBase> observationCodeCodeableConcepts = mySearchParameterExtractor.extractValues("Observation.code", theResource);

		// Only index for lastn if Observation has a code
		if (observationCodeCodeableConcepts.size() == 0) {
			return;
		}

		List<IBase> observationCategoryCodeableConcepts = mySearchParameterExtractor.extractValues("Observation.category", theResource);

		String resourcePID = theResource.getIdElement().getIdPart();

		createOrUpdateIndexedObservation(resourcePID, effectiveDtm, subjectId, observationCodeCodeableConcepts, observationCategoryCodeableConcepts);

	}

	private void createOrUpdateIndexedObservation(String resourcePID, Date theEffectiveDtm, String theSubjectId,
																 List<IBase> theObservationCodeCodeableConcepts,
																 List<IBase> theObservationCategoryCodeableConcepts) {

		// Determine if an index already exists for Observation:
		boolean observationIndexUpdate = false;
		ObservationIndexedSearchParamLastNEntity indexedObservation = null;
		if (resourcePID != null) {
			indexedObservation = myResourceIndexedObservationLastNDao.findByIdentifier(resourcePID);
		}
		if (indexedObservation == null) {
			indexedObservation = new ObservationIndexedSearchParamLastNEntity();
		} else {
			observationIndexUpdate = true;
		}

		indexedObservation.setEffectiveDtm(theEffectiveDtm);
		indexedObservation.setIdentifier(resourcePID);
		indexedObservation.setSubject(theSubjectId);

		addCodeToObservationIndex(theObservationCodeCodeableConcepts, indexedObservation);

		addCategoriesToObservationIndex(theObservationCategoryCodeableConcepts, indexedObservation);

		if (observationIndexUpdate) {
			myEntityManager.merge(indexedObservation);
		} else {
			myEntityManager.persist(indexedObservation);
		}

	}

	private void addCodeToObservationIndex(List<IBase> theObservationCodeCodeableConcepts,
														ObservationIndexedSearchParamLastNEntity theIndexedObservation) {
		// Determine if a Normalized ID was created previously for Observation Code
		Optional<String> existingObservationCodeNormalizedId = getCodeCodeableConceptIdIfExists(theObservationCodeCodeableConcepts.get(0));

		// Create/update normalized Observation Code index record
		ObservationIndexedCodeCodeableConceptEntity codeableConceptField =
			getCodeCodeableConcept(theObservationCodeCodeableConcepts.get(0),
				existingObservationCodeNormalizedId.orElse(UUID.randomUUID().toString()));

		if (existingObservationCodeNormalizedId.isPresent()) {
			myEntityManager.merge(codeableConceptField);
		} else {
			myEntityManager.persist(codeableConceptField);
		}

		theIndexedObservation.setObservationCode(codeableConceptField);
		theIndexedObservation.setCodeNormalizedId(codeableConceptField.getCodeableConceptId());

	}

	private void addCategoriesToObservationIndex(List<IBase> observationCategoryCodeableConcepts,
																ObservationIndexedSearchParamLastNEntity indexedObservation) {
		// Build CodeableConcept entities for Observation.Category
		Set<ObservationIndexedCategoryCodeableConceptEntity> categoryCodeableConceptEntities = new HashSet<>();
		for (IBase categoryCodeableConcept : observationCategoryCodeableConcepts) {
			// Build CodeableConcept entities for each category CodeableConcept
			categoryCodeableConceptEntities.add(getCategoryCodeableConceptEntities(categoryCodeableConcept));
		}
		indexedObservation.setCategoryCodeableConcepts(categoryCodeableConceptEntities);

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

	private Optional<String> getCodeCodeableConceptIdIfExists(IBase theValue) {
		List<IBase> codings = mySearchParameterExtractor.getCodingsFromCodeableConcept(theValue);
		String codeCodeableConceptId = null;
		Optional<String> codeCodeableConceptIdOptional = Optional.empty();

		for (IBase nextCoding : codings) {
			ResourceIndexedSearchParamToken param = mySearchParameterExtractor.createSearchParamForCoding("Observation",
				new RuntimeSearchParam(null, null, "code", null, null, null,
					null, null, null, null),
				nextCoding);
			if (param != null) {
				String system = param.getSystem();
				String code = param.getValue();
				String text = mySearchParameterExtractor.getDisplayTextForCoding(nextCoding);
				if (code != null && system != null) {
					codeCodeableConceptIdOptional = Optional.ofNullable(myObservationIndexedCodeCodingSearchParamDao.findByCodeAndSystem(code, system));
				} else {
					codeCodeableConceptIdOptional = Optional.ofNullable(myObservationIndexedCodeCodingSearchParamDao.findByDisplay(text));
				}
				if (codeCodeableConceptIdOptional.isPresent()) {
					break;
				}
			}
		}

		return codeCodeableConceptIdOptional;
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
		ObservationIndexedSearchParamLastNEntity deletedObservationLastNEntity = myResourceIndexedObservationLastNDao.findByIdentifier(theEntity.getIdDt().getIdPart());
		if (deletedObservationLastNEntity != null) {
			myEntityManager.remove(deletedObservationLastNEntity);
		}
	}

}
