/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.jpa.dao.data.ITermCodeSystemDao;
import ca.uhn.fhir.jpa.dao.data.ITermCodeSystemVersionDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptDao;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.entity.TermCodeSystem;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.entity.TermConceptDesignation;
import ca.uhn.fhir.jpa.entity.TermConceptParentChildLink;
import ca.uhn.fhir.jpa.entity.TermConceptProperty;
import ca.uhn.fhir.jpa.term.api.ITermWriteSvc;
import ca.uhn.fhir.jpa.util.QueryChunker;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.ValidateUtil;
import ca.uhn.hapi.converters.canonical.VersionCanonicalizer;
import jakarta.persistence.EntityManager;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.common.hapi.validation.util.TermConceptPropertyTypeEnum;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.DecimalType;
import org.hl7.fhir.r4.model.IntegerType;
import org.hl7.fhir.r4.model.StringType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class TermWriteSvcImpl implements ITermWriteSvc {
	private static final Logger ourLog = LoggerFactory.getLogger(TermWriteSvcImpl.class);

	@Autowired
	private EntityManager myEntityManager;

	@Autowired
	private IHapiTransactionService myTxService;

	@Autowired
	private ITermCodeSystemVersionDao myTermCodeSystemVersionDao;

	@Autowired
	private ITermCodeSystemDao myTermCodeSystemDao;

	@Autowired
	private ITermConceptDao myTermConceptDao;

	@Autowired
	private VersionCanonicalizer myVersionCanonicalizer;

	@Override
	public StartStagingCodeSystemVersionResponse startStagingCodeSystemVersion(String theCodeSystemUrl, String theVersionId) {
		Validate.notBlank(theCodeSystemUrl, "theCodeSystemUrl must not be blank");
		Validate.isTrue(!theCodeSystemUrl.contains("|"), "theCodeSystemUrl must not be versioned: %s", theCodeSystemUrl);

		return myTxService.withSystemRequestOnDefaultPartition().execute(() -> {
			TermCodeSystem codeSystem = myTermCodeSystemDao.findByCodeSystemUri(theCodeSystemUrl);
			Validate.notNull(codeSystem, "CodeSystem not found: %s", theCodeSystemUrl);

			TermCodeSystemVersion version = new TermCodeSystemVersion();
			version.setResource(codeSystem.getResource());
			version.setCodeSystemVersionId(UUID.randomUUID().toString());
			version.setCodeSystemIntendedVersionId(theVersionId);
			version.setCodeSystem(codeSystem);
			version = myTermCodeSystemVersionDao.saveAndFlush(version);
			ourLog.info("Created new CodeSystemVersion[url={}, version={}] for writing with PID: {}", theVersionId, theCodeSystemUrl, version.getId());

			return new StartStagingCodeSystemVersionResponse(version.getCodeSystemVersionId());
		});
	}

	@Override
	public UploadCodeSystemConceptsResponse uploadCodeSystemConcepts(IBaseResource theCodeSystem) {
		CodeSystem codeSystem = myVersionCanonicalizer.codeSystemToCanonical(theCodeSystem);

		String systemUrl = codeSystem.getUrl();
		String systemVersionId = codeSystem.getVersion();
		ValidateUtil.isNotBlankOrThrowInvalidRequest(systemUrl, "CodeSystem must have a URL");
		ValidateUtil.isNotBlankOrThrowInvalidRequest(systemVersionId, "CodeSystem version must not be blank");

		return myTxService.withSystemRequestOnDefaultPartition().execute(() -> {
			UploadCodeSystemConceptsAccumulator progressAccumulator = new UploadCodeSystemConceptsAccumulator();

			TermCodeSystemVersion codeSystemVersionEntity = myTermCodeSystemVersionDao.findByCodeSystemUriAndVersion(systemUrl, systemVersionId);
			ValidateUtil.isTrueOrThrowInvalidRequest(codeSystemVersionEntity != null, "CodeSystemVersion not found: [url=%s, versionId=%s]", systemUrl, systemVersionId);

			Map<String, TermConcept> loadedConcepts = new HashMap<>();

			Set<String> codes = findAllCodes(codeSystem);
			QueryChunker.chunk(codes, codesSubList -> {
				List<TermConcept> conceptsSubList = myTermConceptDao.findByCodeSystemAndCodeList(codeSystemVersionEntity.getPid(), codesSubList);
				for (TermConcept concept : conceptsSubList) {
					loadedConcepts.put(concept.getCode(), concept);
				}
			});
			// FIXME: prefetch properties and designations if we have any to write

			for (CodeSystem.ConceptDefinitionComponent sourceConcept : codeSystem.getConcept()) {
				uploadCodeSystemConceptAndChildren(sourceConcept, loadedConcepts, codeSystemVersionEntity, progressAccumulator, null);
			}

			int conceptsAddedCount = progressAccumulator.getConceptsAddedCount();
			int conceptLinksAddedCount = progressAccumulator.getConceptLinksAddedCount();
			int designationsAddedCount = progressAccumulator.getDesignationsAddedCount();
			int propertiesAddedCount = progressAccumulator.getPropertiesAddedCount();
			return new UploadCodeSystemConceptsResponse(conceptsAddedCount, conceptLinksAddedCount, designationsAddedCount, propertiesAddedCount);
		});

	}

	private void uploadCodeSystemConceptAndChildren(CodeSystem.ConceptDefinitionComponent theSourceConcept, Map<String, TermConcept> theExistingConcepts, TermCodeSystemVersion theCodeSystemVersionEntity, UploadCodeSystemConceptsAccumulator theProgressAccumulator, TermConcept theParentConcept) {
		HapiTransactionService.requireTransaction();

		// Do we have an existing concept with the given code in the database?
		// If so, we should update it instead of creating a new one
		// If not, we need to create a new one
		TermConcept targetConcept = theExistingConcepts.get(theSourceConcept.getCode());
		if (targetConcept == null) {
			targetConcept = new TermConcept();
			targetConcept.setCode(theSourceConcept.getCode());
			targetConcept.setCodeSystemVersion(theCodeSystemVersionEntity);
		}

		// Populate display
		if (isNotBlank(theSourceConcept.getDisplay())) {
			targetConcept.setDisplay(theSourceConcept.getDisplay());
		}

		// Save the concept
		if (targetConcept.getId() == null) {
			myEntityManager.persist(targetConcept);
			theProgressAccumulator.incrementConceptsAddedCount();
		} else {
			myEntityManager.merge(targetConcept);
		}

		// Populate designations
		if (!theSourceConcept.getDesignation().isEmpty()) {
			Map<DesignationKey, TermConceptDesignation> existingDesignations = new HashMap<>();
			for (TermConceptDesignation existingDesignation : targetConcept.getDesignations()) {
				DesignationKey key = new DesignationKey(existingDesignation);
				existingDesignations.put(key, existingDesignation);
			}

			for (CodeSystem.ConceptDefinitionDesignationComponent sourceDesignation : theSourceConcept.getDesignation()) {
				DesignationKey key = new DesignationKey(sourceDesignation);
				TermConceptDesignation targetDesignation = existingDesignations.get(key);
				if (targetDesignation == null) {
					targetDesignation = new TermConceptDesignation();
					targetConcept.getDesignations().add(targetDesignation);
					targetDesignation.setConcept(targetConcept);
					targetDesignation.setCodeSystemVersion(theCodeSystemVersionEntity);
					targetDesignation.setPartitionId(targetConcept.getPartitionId());
				}

				targetDesignation.setLanguage(sourceDesignation.getLanguage());
				if (sourceDesignation.hasUse()) {
					targetDesignation.setUseSystem(sourceDesignation.getUse().getSystem());
					targetDesignation.setUseCode(sourceDesignation.getUse().getCode());
				}
				targetDesignation.setValue(sourceDesignation.getValue());

				if (targetDesignation.getId() == null) {
					theProgressAccumulator.incrementDesignationsAddedCount();
					myEntityManager.persist(targetDesignation);
				} else {
					myEntityManager.merge(targetDesignation);
				}
			}
		}

		// Populate properties
		if (!theSourceConcept.getProperty().isEmpty()) {
			Map<String, TermConceptProperty> existingProperties = new HashMap<>();
			for (TermConceptProperty existingProperty : targetConcept.getProperties()) {
				existingProperties.put(existingProperty.getKey(), existingProperty);
			}

			for (CodeSystem.ConceptPropertyComponent sourceProperty : theSourceConcept.getProperty()) {
				TermConceptProperty targetProperty = existingProperties.get(sourceProperty.getCode());
				if (targetProperty == null) {
					targetProperty = new TermConceptProperty();
					targetConcept.getProperties().add(targetProperty);
					targetProperty.setConcept(targetConcept);
					targetProperty.setCodeSystemVersion(theCodeSystemVersionEntity);
					targetProperty.setPartitionId(targetConcept.getPartitionId());
					targetProperty.setKey(sourceProperty.getCode());
				}

				populateTermConceptPropertyValue(sourceProperty, targetProperty);

				if (targetProperty.getId() == null) {
					theProgressAccumulator.incrementPropertiesAddedCount();
					myEntityManager.persist(targetProperty);
				} else {
					myEntityManager.merge(targetProperty);
				}
			}
		}

		// Parent Concept
		if (theParentConcept != null) {
			boolean haveParent = targetConcept.getParents().stream().anyMatch(t -> t.getParent().getCode().equals(theParentConcept.getCode()));
			if (!haveParent) {
				theProgressAccumulator.incrementConceptLinksAddedCount();
				TermConceptParentChildLink link = theParentConcept.addChild(targetConcept, TermConceptParentChildLink.RelationshipTypeEnum.ISA);
				myEntityManager.persist(link);
			}
		}

		// Child Concepts
		for (CodeSystem.ConceptDefinitionComponent child : theSourceConcept.getConcept()) {
			uploadCodeSystemConceptAndChildren(child, theExistingConcepts, theCodeSystemVersionEntity, theProgressAccumulator, targetConcept);
		}
	}

	private Set<String> findAllCodes(CodeSystem theCodeSystem) {
		Set<String> codes = new HashSet<>();
		findAllCodes(codes, theCodeSystem.getConcept());
		return codes;
	}

	private void findAllCodes(Set<String> theCodesToPopulate, List<CodeSystem.ConceptDefinitionComponent> theConcepts) {
		for (CodeSystem.ConceptDefinitionComponent next : theConcepts) {
			theCodesToPopulate.add(next.getCode());
			findAllCodes(theCodesToPopulate, next.getConcept());
		}
	}

	public static void populateTermConceptPropertyValue(CodeSystem.ConceptPropertyComponent next, TermConceptProperty property) {
		if (next.getValue() instanceof CodeType) {
			property.setType(TermConceptPropertyTypeEnum.CODE);
			property.setValue(((CodeType) next.getValue()).getValueAsString());
		} else if (next.getValue() instanceof StringType) {
			property.setType(TermConceptPropertyTypeEnum.STRING);
			property.setValue(next.getValueStringType().getValue());
		} else if (next.getValue() instanceof BooleanType) {
			property.setType(TermConceptPropertyTypeEnum.BOOLEAN);
			property.setValue(((BooleanType) next.getValue()).getValueAsString());
		} else if (next.getValue() instanceof IntegerType) {
			property.setType(TermConceptPropertyTypeEnum.INTEGER);
			property.setValue(((IntegerType) next.getValue()).getValueAsString());
		} else if (next.getValue() instanceof DecimalType) {
			property.setType(TermConceptPropertyTypeEnum.DECIMAL);
			property.setValue(((DecimalType) next.getValue()).getValueAsString());
		} else if (next.getValue() instanceof DateTimeType) {
			property.setType(TermConceptPropertyTypeEnum.DATETIME);
			property.setValue(((DateTimeType) next.getValue()).getValueAsString());
		} else if (next.getValue() instanceof Coding) {
			Coding nextCoding = next.getValueCoding();
			property.setType(TermConceptPropertyTypeEnum.CODING);
			property.setCodeSystem(nextCoding.getSystem());
			property.setValue(nextCoding.getCode());
			property.setDisplay(nextCoding.getDisplay());
		} else if (next.getValue() != null) {
			// FIXME: add code
			throw new InvalidRequestException("Don't know how to handle concept properties of type: " + next.getValue().getClass());
		}
	}

	private record DesignationKey(String language, String useSystem, String useCode) {
		public DesignationKey(TermConceptDesignation theDesignation) {
			this(theDesignation.getLanguage(), theDesignation.getUseSystem(), theDesignation.getUseCode());
		}

		public DesignationKey(CodeSystem.ConceptDefinitionDesignationComponent theDesignation) {
			this(theDesignation.getLanguage(), theDesignation.hasUse() ? theDesignation.getUse().getSystem() : null, theDesignation.hasUse() ? theDesignation.getUse().getCode() : null);
		}

	}

	private static class UploadCodeSystemConceptsAccumulator {
		private int myConceptsAddedCount = 0;
		private int myDesignationsAddedCount = 0;
		private int myPropertiesAddedCount = 0;
		private int myConceptLinksAddedCount = 0;

		public int getConceptLinksAddedCount() {
			return myConceptLinksAddedCount;
		}

		public void incrementConceptsAddedCount() {
			myConceptsAddedCount++;
		}

		public void incrementDesignationsAddedCount() {
			myDesignationsAddedCount++;
		}

		public void incrementPropertiesAddedCount() {
			myPropertiesAddedCount++;
		}

		public int getConceptsAddedCount() {
			return myConceptsAddedCount;
		}

		public int getDesignationsAddedCount() {
			return myDesignationsAddedCount;
		}

		public int getPropertiesAddedCount() {
			return myPropertiesAddedCount;
		}

		public void incrementConceptLinksAddedCount() {
			myConceptLinksAddedCount++;
		}
	}

}
