package ca.uhn.fhir.jpa.dao;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.context.*;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.model.cross.IBasePersistedResource;
import ca.uhn.fhir.jpa.model.entity.ResourceEncodingEnum;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamToken;
import ca.uhn.fhir.jpa.model.util.CodeSystemHash;
import ca.uhn.fhir.jpa.search.lastn.IElasticsearchSvc;
import ca.uhn.fhir.jpa.search.lastn.json.CodeJson;
import ca.uhn.fhir.jpa.search.lastn.json.ObservationJson;
import ca.uhn.fhir.jpa.searchparam.extractor.ISearchParamExtractor;
import ca.uhn.fhir.jpa.searchparam.extractor.PathAndRef;
import ca.uhn.fhir.parser.IParser;
import org.hl7.fhir.instance.model.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public class ObservationLastNIndexPersistSvc {

	@Autowired
	private ISearchParamExtractor mySearchParameterExtractor;

	@Autowired(required = false)
	private IElasticsearchSvc myElasticsearchSvc;

	@Autowired
	private DaoConfig myConfig;

	@Autowired
	private FhirContext myContext;

	public void indexObservation(IBaseResource theResource) {

		if (myElasticsearchSvc == null) {
			// Elasticsearch is not enabled and therefore no index needs to be updated.
			return;
		}

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

		createOrUpdateIndexedObservation(theResource, effectiveDtm, subjectId, observationCodeCodeableConcepts, observationCategoryCodeableConcepts);

	}

	private void createOrUpdateIndexedObservation(IBaseResource theResource, Date theEffectiveDtm, String theSubjectId,
																 List<IBase> theObservationCodeCodeableConcepts,
																 List<IBase> theObservationCategoryCodeableConcepts) {
		String resourcePID = theResource.getIdElement().getIdPart();

		// Determine if an index already exists for Observation:
		ObservationJson indexedObservation = null;
		if (resourcePID != null) {
			indexedObservation = myElasticsearchSvc.getObservationDocument(resourcePID);
		}
		if (indexedObservation == null) {
			indexedObservation = new ObservationJson();
		}

		indexedObservation.setEffectiveDtm(theEffectiveDtm);
		indexedObservation.setIdentifier(resourcePID);
		if (myConfig.isStoreResourceInLuceneIndex()) {
			indexedObservation.setResource(encodeResource(theResource));
		}
		indexedObservation.setSubject(theSubjectId);

		addCodeToObservationIndex(theObservationCodeCodeableConcepts, indexedObservation);

		addCategoriesToObservationIndex(theObservationCategoryCodeableConcepts, indexedObservation);

		myElasticsearchSvc.createOrUpdateObservationIndex(resourcePID, indexedObservation);

	}

	private String encodeResource(IBaseResource theResource) {
		IParser parser = myContext.newJsonParser();
		return parser.encodeResourceToString(theResource);
	}

	private void addCodeToObservationIndex(List<IBase> theObservationCodeCodeableConcepts,
														ObservationJson theIndexedObservation) {
		// Determine if a Normalized ID was created previously for Observation Code
		String existingObservationCodeNormalizedId = getCodeCodeableConceptId(theObservationCodeCodeableConcepts.get(0));

		// Create/update normalized Observation Code index record
		CodeJson codeableConceptField =
			getCodeCodeableConcept(theObservationCodeCodeableConcepts.get(0),
				existingObservationCodeNormalizedId);

		myElasticsearchSvc.createOrUpdateObservationCodeIndex(codeableConceptField.getCodeableConceptId(), codeableConceptField);

		theIndexedObservation.setCode(codeableConceptField);
	}

	private void addCategoriesToObservationIndex(List<IBase> observationCategoryCodeableConcepts,
																ObservationJson indexedObservation) {
		// Build CodeableConcept entities for Observation.Category
		List<CodeJson> categoryCodeableConceptEntities = new ArrayList<>();
		for (IBase categoryCodeableConcept : observationCategoryCodeableConcepts) {
			// Build CodeableConcept entities for each category CodeableConcept
			categoryCodeableConceptEntities.add(getCategoryCodeableConceptEntities(categoryCodeableConcept));
		}
		indexedObservation.setCategories(categoryCodeableConceptEntities);
	}

	private CodeJson getCategoryCodeableConceptEntities(IBase theValue) {
		String text = mySearchParameterExtractor.getDisplayTextFromCodeableConcept(theValue);
		CodeJson categoryCodeableConcept = new CodeJson();
		categoryCodeableConcept.setCodeableConceptText(text);

		List<IBase> codings = mySearchParameterExtractor.getCodingsFromCodeableConcept(theValue);
		for (IBase nextCoding : codings) {
			addCategoryCoding(nextCoding, categoryCodeableConcept);
		}
		return categoryCodeableConcept;
	}

	private CodeJson getCodeCodeableConcept(IBase theValue, String observationCodeNormalizedId) {
		String text = mySearchParameterExtractor.getDisplayTextFromCodeableConcept(theValue);
		CodeJson codeCodeableConcept = new CodeJson();
		codeCodeableConcept.setCodeableConceptText(text);
		codeCodeableConcept.setCodeableConceptId(observationCodeNormalizedId);

		List<IBase> codings = mySearchParameterExtractor.getCodingsFromCodeableConcept(theValue);
		for (IBase nextCoding : codings) {
			addCodeCoding(nextCoding, codeCodeableConcept);
		}

		return codeCodeableConcept;
	}

	private String getCodeCodeableConceptId(IBase theValue) {
		List<IBase> codings = mySearchParameterExtractor.getCodingsFromCodeableConcept(theValue);
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

				String codeSystemHash = String.valueOf(CodeSystemHash.hashCodeSystem(system, code));
				CodeJson codeCodeableConceptDocument = myElasticsearchSvc.getObservationCodeDocument(codeSystemHash, text);
				if (codeCodeableConceptDocument != null) {
					codeCodeableConceptIdOptional = Optional.of(codeCodeableConceptDocument.getCodeableConceptId());
					break;
				}
			}
		}

		return codeCodeableConceptIdOptional.orElse(UUID.randomUUID().toString());
	}

	private void addCategoryCoding(IBase theValue, CodeJson theCategoryCodeableConcept) {
		ResourceIndexedSearchParamToken param = mySearchParameterExtractor.createSearchParamForCoding("Observation",
			new RuntimeSearchParam(null, null, "category", null, null, null, null, null, null, null),
			theValue);
		if (param != null) {
			String system = param.getSystem();
			String code = param.getValue();
			String text = mySearchParameterExtractor.getDisplayTextForCoding(theValue);
			theCategoryCodeableConcept.addCoding(system, code, text);
		}
	}

	private void addCodeCoding(IBase theValue, CodeJson theObservationCode) {
		ResourceIndexedSearchParamToken param = mySearchParameterExtractor.createSearchParamForCoding("Observation",
			new RuntimeSearchParam(null, null, "code", null, null, null, null, null, null, null),
			theValue);
		if (param != null) {
			String system = param.getSystem();
			String code = param.getValue();
			String text = mySearchParameterExtractor.getDisplayTextForCoding(theValue);
			theObservationCode.addCoding(system, code, text);
		}
	}

	public void deleteObservationIndex(IBasePersistedResource theEntity) {
		if (myElasticsearchSvc == null) {
			// Elasticsearch is not enabled and therefore no index needs to be updated.
			return;
		}

		ObservationJson deletedObservationLastNEntity = myElasticsearchSvc.getObservationDocument(theEntity.getIdDt().getIdPart());
		if (deletedObservationLastNEntity != null) {
			myElasticsearchSvc.deleteObservationDocument(deletedObservationLastNEntity.getIdentifier());
		}
	}

}
