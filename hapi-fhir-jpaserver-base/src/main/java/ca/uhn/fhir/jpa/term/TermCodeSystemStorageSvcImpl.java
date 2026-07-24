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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.api.svc.ResolveIdentityMode;
import ca.uhn.fhir.jpa.dao.data.IResourceTableDao;
import ca.uhn.fhir.jpa.dao.data.ITermCodeSystemDao;
import ca.uhn.fhir.jpa.dao.data.ITermCodeSystemVersionDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptDesignationDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptParentChildLinkDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptPropertyDao;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.entity.TermCodeSystem;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.entity.TermConceptDesignation;
import ca.uhn.fhir.jpa.entity.TermConceptParentChildLink;
import ca.uhn.fhir.jpa.entity.TermConceptProperty;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.entity.EntityIndexStatusEnum;
import ca.uhn.fhir.jpa.model.entity.IdAndPartitionId;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemStorageSvc;
import ca.uhn.fhir.jpa.term.api.ITermDeferredStorageSvc;
import ca.uhn.fhir.jpa.term.api.ITermReadSvc;
import ca.uhn.fhir.jpa.term.api.ITermVersionAdapterSvc;
import ca.uhn.fhir.jpa.util.QueryChunker;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.util.StopWatch;
import ca.uhn.fhir.util.UrlUtil;
import ca.uhn.fhir.util.ValidateUtil;
import ca.uhn.hapi.converters.canonical.VersionCanonicalizer;
import jakarta.annotation.Nonnull;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceContextType;
import org.apache.commons.lang3.Strings;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.common.hapi.validation.util.TermConceptPropertyTypeEnum;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.DecimalType;
import org.hl7.fhir.r4.model.IntegerType;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.ValueSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class TermCodeSystemStorageSvcImpl implements ITermCodeSystemStorageSvc {
	private static final Logger ourLog = LoggerFactory.getLogger(TermCodeSystemStorageSvcImpl.class);
	private static final Object PLACEHOLDER_OBJECT = new Object();
	public static final String DONT_POPULATE_PARENT_PIDS_CS_USERDATA_KEY =
			TermCodeSystemStorageSvcImpl.class.getName() + "_DONT_POPULATE_PARENT_PIDS";

	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	protected EntityManager myEntityManager;

	@Autowired
	private IHapiTransactionService myTxService;

	@Autowired
	private VersionCanonicalizer myVersionCanonicalizer;

	@Autowired
	protected ITermCodeSystemDao myCodeSystemDao;

	@Autowired
	protected ITermCodeSystemVersionDao myCodeSystemVersionDao;

	@Autowired
	protected ITermConceptDao myConceptDao;

	@Autowired
	protected ITermConceptPropertyDao myConceptPropertyDao;

	@Autowired
	protected ITermConceptDesignationDao myConceptDesignationDao;

	@Autowired
	protected IIdHelperService<JpaPid> myIdHelperService;

	@Autowired
	private ITermConceptParentChildLinkDao myConceptParentChildLinkDao;

	@Autowired
	private ITermVersionAdapterSvc myTerminologyVersionAdapterSvc;

	@Autowired
	private ITermDeferredStorageSvc myDeferredStorageSvc;

	@Autowired
	private FhirContext myContext;

	@Autowired
	private ITermReadSvc myTerminologySvc;

	@Autowired
	private JpaStorageSettings myStorageSettings;

	@Autowired
	private IResourceTableDao myResourceTableDao;

	@Autowired
	private DaoRegistry myDaoRegistry;

	@Autowired
	private TermConceptDaoSvc myTermConceptDaoSvc;

	@Autowired
	private ITermCodeSystemDao myTermCodeSystemDao;

	@Nonnull
	private UploadStatistics addConceptsToCodeSystemVersion(
			TermCodeSystem theTermCodeSystem,
			TermCodeSystemVersion theCodeSystemVersion,
			List<TermConcept> theAdditions) {
		HapiTransactionService.requireTransaction();

		Validate.notNull(theTermCodeSystem, "No code system found for code system version: %s", theCodeSystemVersion);
		Validate.notNull(theTermCodeSystem.getPid(), "No pid found for code system: %s", theTermCodeSystem);

		String codeSystemUrl = theTermCodeSystem.getCodeSystemUri();
		CodeSystem codeSystem = myTerminologySvc.fetchCanonicalCodeSystemFromCompleteContext(codeSystemUrl);
		if (codeSystem != null && codeSystem.getContent() != CodeSystem.CodeSystemContentMode.NOTPRESENT) {
			throw new InvalidRequestException(
					Msg.code(844) + "CodeSystem with url[" + Constants.codeSystemWithDefaultDescription(codeSystemUrl)
							+ "] can not apply a delta - wrong content mode: " + codeSystem.getContent());
		}

		IIdType codeSystemId = theTermCodeSystem.getResource().getIdDt();

		UploadStatistics retVal = new UploadStatistics(codeSystemId);
		Map<String, TermConcept> codeToConcept = new HashMap<>();

		// Fetch any existing concepts matching codes to be added
		Set<String> codes = findAllCodes(theAdditions);
		if (!codes.isEmpty()) {
			QueryChunker.chunk(codes, codesSubList -> {
				StopWatch sw = new StopWatch();
				List<TermConcept> conceptsSubList =
						myConceptDao.findByCodeSystemAndCodeList(theCodeSystemVersion.getPid(), codesSubList);
				ourLog.info(
						"Handled prefetch on {} candidate codes and found {} existing in {}",
						codesSubList.size(),
						conceptsSubList.size(),
						sw);

				List<TermConcept.TermConceptPk> conceptIds =
						conceptsSubList.stream().map(TermConcept::getPid).toList();
				if (!conceptIds.isEmpty()) {
					myConceptDao.fetchConceptsAndDesignationsByConceptPids(conceptIds);
					myConceptDao.fetchConceptsAndPropertiesByConceptPids(conceptIds);
					myConceptDao.fetchConceptsAndParentLinksByConceptPids(conceptIds);
					myConceptDao.fetchConceptsAndChildLinksByConceptPids(conceptIds);

					for (TermConcept concept : conceptsSubList) {
						codeToConcept.put(concept.getCode(), concept);
					}
				}
			});
		}

		// Add root concepts
		for (TermConcept nextRootConcept : theAdditions) {
			List<String> parentCodes = Collections.emptyList();
			addConceptInHierarchy(theCodeSystemVersion, parentCodes, nextRootConcept, retVal, codeToConcept, 0);
		}

		myTerminologySvc.invalidateCodeSystemCaches();
		return retVal;
	}

	private void deleteEverythingRelatedToConcept(TermConcept theConcept, UploadStatistics theUploadStatistics) {

		for (TermConceptParentChildLink nextParent : theConcept.getParents()) {
			nextParent.getParent().getChildren().remove(nextParent);
			myConceptParentChildLinkDao.deleteById(nextParent.getPid());
			theUploadStatistics.incrementConceptLinksRemovedCount();
		}
		for (TermConceptParentChildLink nextChild : theConcept.getChildren()) {
			nextChild.getChild().getParents().remove(nextChild);
			myConceptParentChildLinkDao.deleteById(nextChild.getPid());
			theUploadStatistics.incrementConceptLinksRemovedCount();
		}

		for (TermConceptDesignation next : theConcept.getDesignations()) {
			myConceptDesignationDao.deleteById(next.getPartitionedId());
			theUploadStatistics.incrementDesignationsRemovedCount();
		}
		theConcept.getDesignations().clear();
		for (TermConceptProperty next : theConcept.getProperties()) {
			myConceptPropertyDao.deleteById(next.getPartitionedId());
			theUploadStatistics.incrementPropertiesRemovedCount();
		}
		theConcept.getProperties().clear();

		ourLog.info("Deleting concept {} - Code {}", theConcept.getId(), theConcept.getCode());

		theUploadStatistics.incrementConceptsRemovedCount();
		myConceptDao.deleteById(theConcept.getPid());
	}

	private List<TermConcept> flattenChildren(TermConcept theTermConcept) {
		if (theTermConcept.getChildren().isEmpty()) {
			return Arrays.asList(theTermConcept);
		}

		// Recursively flatten children
		List<TermConcept> childTermConcepts = theTermConcept.getChildren().stream()
				.map(TermConceptParentChildLink::getChild)
				.flatMap(childConcept -> flattenChildren(childConcept).stream())
				.collect(Collectors.toList());

		// Add itself before its list of children
		childTermConcepts.add(0, theTermConcept);
		return childTermConcepts;
	}

	/**
	 * Returns the number of saved concepts
	 */
	@Override
	public int saveConcept(TermConcept theConcept) {
		return myTermConceptDaoSvc.saveConcept(theConcept);
	}

	// Generated by Claude Opus 4.6
	@Override
	public Optional<JpaPid> findExistingCodeSystemResourcePid(String theUrl, String theVersion) {
		HapiTransactionService.requireTransaction();

		TermCodeSystem tcs = myCodeSystemDao.findByCodeSystemUri(theUrl);
		if (tcs == null) {
			return Optional.empty();
		}
		TermCodeSystemVersion csv = getExistingTermCodeSystemVersion(tcs.getPid(), theVersion);
		return csv != null ? Optional.of(csv.getResource().getId()) : Optional.empty();
	}

	@Override
	public void storeNewCodeSystemVersionIfNeeded(
			IBaseResource theCodeSystem, ResourceTable theResourceEntity, RequestDetails theRequestDetails) {
		HapiTransactionService.requireTransaction();

		CodeSystem codeSystem = myVersionCanonicalizer.codeSystemToCanonical(theCodeSystem);

		if (codeSystem != null && isNotBlank(codeSystem.getUrl())) {
			String codeSystemUrl = codeSystem.getUrl();
			if (codeSystem.getContent() == CodeSystem.CodeSystemContentMode.COMPLETE
					|| codeSystem.getContent() == null
					|| codeSystem.getContent() == CodeSystem.CodeSystemContentMode.NOTPRESENT) {
				ourLog.info(
						"CodeSystem {} has a status of {}, going to store concepts in terminology tables",
						theResourceEntity.getIdDt().getValue(),
						codeSystem.getContentElement().getValueAsString());

				detectDuplicatesInCodeSystem(codeSystem);

				/*
				 * If this is a not-present codesystem and codesystem version already exists, we don't want to
				 * overwrite the existing version since that will wipe out the existing concepts. We do create
				 * or update the TermCodeSystem table though, since that allows the DB to reject changes that would
				 * result in duplicate CodeSystem.url values.
				 */
				if (codeSystem.getContent() == CodeSystem.CodeSystemContentMode.NOTPRESENT) {
					TermCodeSystem termCodeSystem = myCodeSystemDao.findByCodeSystemUri(codeSystem.getUrl());
					if (termCodeSystem != null) {
						TermCodeSystemVersion codeSystemVersion =
								getExistingTermCodeSystemVersion(termCodeSystem.getPid(), codeSystem.getVersion());
						if (codeSystemVersion != null) {
							getOrCreateDistinctTermCodeSystem(
									codeSystem.getUrl(),
									codeSystem.getUrl(),
									codeSystem.getVersion(),
									theResourceEntity,
									true);
							if (getExistingTermCodeSystemVersion(termCodeSystem.getPid(), codeSystem.getVersion())
									!= null) {
								return;
							}
						}
					}
				}

				TermCodeSystemVersion persCs = new TermCodeSystemVersion();
				populateCodeSystemVersionProperties(persCs, codeSystem, theResourceEntity);
				myEntityManager.persist(persCs);

				persCs.getConcepts().addAll(TermReadSvcImpl.toPersistedConcepts(codeSystem.getConcept(), persCs));
				linkFlatHierarchyFromConceptProperties(codeSystem, persCs);
				ourLog.debug("Code system has {} concepts", persCs.getConcepts().size());
				storeNewCodeSystemVersion(
						codeSystemUrl,
						codeSystem.getName(),
						codeSystem.getVersion(),
						persCs,
						theResourceEntity,
						theRequestDetails);
			}
		}
	}

	private static void detectDuplicatesInCodeSystem(CodeSystem theCodeSystem) {
		detectDuplicatesInCodeSystem(theCodeSystem.getConcept(), new HashSet<>());
	}

	private static void detectDuplicatesInCodeSystem(
			List<CodeSystem.ConceptDefinitionComponent> theCodeList, Set<String> theFoundCodesBuffer) {
		for (var next : theCodeList) {
			if (isNotBlank(next.getCode())) {
				if (!theFoundCodesBuffer.add(next.getCode())) {
					/*
					 * Note: We could possibly modify this behaviour to be forgiving, and just
					 * ignore duplicates. The only issue is that concepts can have properties,
					 * designations, etc. and it could be dangerous to just pick one and ignore the
					 * other. So the safer thing seems to be to just throw an error.
					 */
					throw new PreconditionFailedException(Msg.code(2528) + "Duplicate concept detected in CodeSystem: "
							+ UrlUtil.sanitizeUrlPart(next.getCode()));
				}
			}
			// Test child concepts within the parent concept
			detectDuplicatesInCodeSystem(next.getConcept(), theFoundCodesBuffer);
		}
	}

	@Override
	@Transactional
	public IIdType storeNewCodeSystemVersion(
			CodeSystem theCodeSystemResource,
			TermCodeSystemVersion theCodeSystemVersion,
			RequestDetails theRequest,
			List<ValueSet> theValueSets,
			List<ConceptMap> theConceptMaps) {
		assert TransactionSynchronizationManager.isActualTransactionActive();

		Validate.notBlank(theCodeSystemResource.getUrl(), "theCodeSystemResource must have a URL");

		// Note that this creates the TermCodeSystem and TermCodeSystemVersion entities if needed
		IIdType csId = myTerminologyVersionAdapterSvc.createOrUpdateCodeSystem(theCodeSystemResource, theRequest);

		JpaPid codeSystemResourcePid = myIdHelperService.resolveResourceIdentityPid(
				RequestPartitionId.allPartitions(),
				csId.getResourceType(),
				csId.getIdPart(),
				ResolveIdentityMode.includeDeleted().cacheOk());
		ResourceTable resource = myResourceTableDao.getOne(codeSystemResourcePid);

		ourLog.info("CodeSystem resource has ID: {}", csId.getValue());

		populateCodeSystemVersionProperties(theCodeSystemVersion, theCodeSystemResource, resource);

		storeNewCodeSystemVersion(
				theCodeSystemResource.getUrl(),
				theCodeSystemResource.getName(),
				theCodeSystemResource.getVersion(),
				theCodeSystemVersion,
				resource,
				theRequest);

		myDeferredStorageSvc.addConceptMapsToStorageQueue(theConceptMaps);
		myDeferredStorageSvc.addValueSetsToStorageQueue(theValueSets);

		return csId;
	}

	@Override
	@Transactional
	public void storeNewCodeSystemVersion(
			String theSystemUri,
			String theSystemName,
			String theCodeSystemVersionId,
			TermCodeSystemVersion theCodeSystemVersion,
			ResourceTable theCodeSystemResourceTable,
			RequestDetails theRequestDetails) {
		assert TransactionSynchronizationManager.isActualTransactionActive();

		ourLog.debug("Storing code system");
		Date updated = new Date();

		TermCodeSystemVersion codeSystemToStore = theCodeSystemVersion;
		ValidateUtil.isTrueOrThrowInvalidRequest(codeSystemToStore.getResource() != null, "No resource supplied");
		ValidateUtil.isNotBlankOrThrowInvalidRequest(theSystemUri, "No system URI supplied");

		TermCodeSystem codeSystem = getOrCreateDistinctTermCodeSystem(
				theSystemUri, theSystemName, theCodeSystemVersionId, theCodeSystemResourceTable, false);

		List<TermCodeSystemVersion> existing =
				myCodeSystemVersionDao.findByCodeSystemResourcePid(theCodeSystemResourceTable.getResourceId());
		for (TermCodeSystemVersion next : existing) {
			if (Objects.equals(next.getCodeSystemVersionId(), theCodeSystemVersionId)
					&& myConceptDao.countByCodeSystemVersion(next.getPid()) == 0) {

				/*
				 * If we already have a CodeSystemVersion that matches the version we're storing, we
				 * can reuse it. Note that we only reuse if there are no concepts attached to the
				 * existing codesystem because we always write a completely fresh set of concepts
				 * and mark the old one for deletion. Theoretically we could optimize this by
				 * figuring out a delta and only writing that, but that is fairly involved
				 * since concepts have parents and children and properties and designations and
				 * all that - so it's safer to just always assume changes and write everything
				 * fresh. Also, this isn't the kind of thing that's expected to happen often
				 * so we aren't particularly performance sensitive here.
				 */
				next.setCodeSystemDisplayName(theSystemName);
				codeSystemToStore = next;

			} else {

				/*
				 * If we already have a TermCodeSystemVersion that corresponds to the FHIR Resource ID we're
				 * adding a version to, we will mark it for deletion. For any one resource there can only
				 * be one TermCodeSystemVersion entity in the DB. Multiple versions of a codesystem uses
				 * multiple CodeSystem resources with CodeSystem.version set differently (as opposed to
				 * multiple versions of the same CodeSystem, where CodeSystem.meta.versionId is different)
				 */
				deleteCodeSystemVersion(next);
			}
		}

		/*
		 * Do the upload
		 */

		if (codeSystemToStore.getPid() == null) {
			codeSystemToStore.setCodeSystem(codeSystem);
		} else {
			codeSystemToStore.setCodeSystemPid(codeSystem.getPid());
		}
		codeSystemToStore.setCodeSystemDisplayName(theSystemName);
		codeSystemToStore.setCodeSystemVersionId(theCodeSystemVersionId);

		if (codeSystemToStore.getPid() == null) {
			myEntityManager.persist(codeSystemToStore);
		}

		ourLog.debug("Validating all codes in CodeSystem for storage (this can take some time for large sets)");

		// Validate the code system
		ArrayList<String> conceptsStack = new ArrayList<>();
		IdentityHashMap<TermConcept, Object> allConcepts = new IdentityHashMap<>();
		int totalCodeCount = 0;
		Collection<TermConcept> conceptsToSave = theCodeSystemVersion.getConcepts();
		for (TermConcept next : conceptsToSave) {
			totalCodeCount += validateConceptForStorage(next, codeSystemToStore, conceptsStack, allConcepts);
			Validate.isTrue(next.getPid().getId() == null);
			Validate.isTrue(codeSystemToStore.getPid() != null);

			// Make sure to initialize the PK object so that hibernate doesn't choke on creation
			next.setId(null);

			next.setCodeSystemVersion(codeSystemToStore);
			next.setUpdated(updated);

			myEntityManager.persist(next);
			for (var property : next.getProperties()) {
				assert property.getId() == null;
				property.setCodeSystemVersion(codeSystemToStore);
				myEntityManager.persist(property);
			}
			for (var designation : next.getDesignations()) {
				assert designation.getId() == null;
				designation.setCodeSystemVersion(codeSystemToStore);
				myEntityManager.persist(designation);
			}
		}

		ourLog.debug("Saving version containing {} concepts", totalCodeCount);
		Validate.notNull(codeSystemToStore.getPid(), "Code system not saved");
		codeSystemToStore = myEntityManager.merge(codeSystemToStore);

		boolean isMakeVersionCurrent = ITermCodeSystemStorageSvc.isMakeVersionCurrent(theRequestDetails);
		if (isMakeVersionCurrent) {
			codeSystem.setCurrentVersionPid(codeSystemToStore);
		}

		ourLog.debug("Setting CodeSystemVersion[{}] on {} concepts...", codeSystem.getPid(), totalCodeCount);
		for (TermConcept next : conceptsToSave) {
			populateVersion(next, codeSystemToStore);
		}

		ourLog.debug("Saving {} concepts...", totalCodeCount);
		IdentityHashMap<TermConcept, Object> conceptsStack2 = new IdentityHashMap<>();
		for (TermConcept next : conceptsToSave) {
			persistChildren(next, codeSystemToStore, conceptsStack2, totalCodeCount);
		}

		ourLog.debug("Done saving concepts, flushing to database");
		if (!myDeferredStorageSvc.isStorageQueueEmpty(true)) {
			ourLog.info("Note that some concept saving has been deferred");
		}

		if (isMakeVersionCurrent) {
			myTerminologySvc.updateCodeSystemVersionCache(theSystemUri, codeSystemToStore);
		}
	}

	private void deleteCodeSystemVersion(TermCodeSystemVersion theCodeSystemVersion) {
		HapiTransactionService.requireTransaction();

		theCodeSystemVersion.setCodeSystemVersionId("DELETED_" + UUID.randomUUID());
		myCodeSystemVersionDao.saveAndFlush(theCodeSystemVersion);
		myDeferredStorageSvc.deleteCodeSystemVersion(theCodeSystemVersion);
	}

	private TermCodeSystemVersion getExistingTermCodeSystemVersion(
			Long theCodeSystemVersionPid, String theCodeSystemVersion) {
		TermCodeSystemVersion existing;
		if (theCodeSystemVersion == null) {
			existing = myCodeSystemVersionDao.findByCodeSystemPidVersionIsNull(theCodeSystemVersionPid);
		} else {
			existing =
					myCodeSystemVersionDao.findByCodeSystemPidAndVersion(theCodeSystemVersionPid, theCodeSystemVersion);
		}

		return existing;
	}

	private void validateDstu3OrNewer() {
		Validate.isTrue(
				myContext.getVersion().getVersion().isEqualOrNewerThan(FhirVersionEnum.DSTU3),
				"Terminology operations only supported in DSTU3+ mode");
	}

	private void addConceptInHierarchy(
			TermCodeSystemVersion theCsv,
			Collection<String> theParentCodes,
			TermConcept theSourceConcept,
			UploadStatistics theStatisticsTracker,
			Map<String, TermConcept> theCodeToConcept,
			int theSequence) {

		String parentDescription = "(root concept)";
		if (!theParentCodes.isEmpty()) {
			parentDescription = theParentCodes.toString();
		}

		ourLog.atDebug()
				.setMessage("Saving concept[{}] count {} at index {} with parent {}")
				.addArgument(theSourceConcept.getCode())
				.addArgument(theStatisticsTracker.getUpdatedConceptCount())
				.addArgument(theSequence)
				.addArgument(parentDescription)
				.log();

		TermConcept targetConcept = theCodeToConcept.get(theSourceConcept.getCode());
		boolean updatingExisting = false;
		if (targetConcept != null) {
			targetConcept.setIndexStatus(null);
			updatingExisting = true;
		} else {
			targetConcept = new TermConcept();
			targetConcept.setCode(theSourceConcept.getCode());
			targetConcept.setCodeSystemVersion(theCsv);

			theCodeToConcept.put(theSourceConcept.getCode(), targetConcept);
			theStatisticsTracker.incrementConceptsAddedCount();
		}

		targetConcept.setDontPopulateParentPids(theSourceConcept.isDontPopulateParentPids());

		// Display
		boolean changed = false;
		if (isNotBlank(theSourceConcept.getDisplay())
				&& !Strings.CS.equals(theSourceConcept.getDisplay(), targetConcept.getDisplay())) {
			targetConcept.setDisplay(theSourceConcept.getDisplay());
			changed = true;
		}

		// Sequence
		if (targetConcept.getSequence() == null && !Objects.equals(theSequence, targetConcept.getSequence())) {
			targetConcept.setSequence(theSequence);
			changed = true;
		}

		if (changed && updatingExisting) {
			theStatisticsTracker.incrementUpdatedConceptCount();
		}

		// Null out the hierarchy PIDs for this concept always. We do this because we're going to
		// force a reindex, and it'll be regenerated then
		targetConcept.setParentPids(null);

		// Properties
		if (theSourceConcept.hasProperties()) {
			Map<String, TermConceptProperty> existingProperties = new HashMap<>();
			for (TermConceptProperty existingProperty : targetConcept.getProperties()) {
				existingProperties.put(existingProperty.getKey(), existingProperty);
			}

			for (TermConceptProperty sourceProperty : theSourceConcept.getProperties()) {
				TermConceptProperty targetProperty = existingProperties.get(sourceProperty.getKey());
				if (targetProperty == null) {
					targetProperty = new TermConceptProperty();
					targetConcept.getProperties().add(targetProperty);
					targetProperty.setConcept(targetConcept);
					targetProperty.setCodeSystemVersion(theCsv);
					targetProperty.setPartitionId(targetConcept.getPartitionId());
					targetProperty.setKey(sourceProperty.getKey());
				}

				targetProperty.setType(sourceProperty.getType());
				targetProperty.setValue(sourceProperty.getValue());
				targetProperty.setCodeSystem(sourceProperty.getCodeSystem());
				targetProperty.setDisplay(sourceProperty.getDisplay());

				if (targetProperty.getId() == null) {
					theStatisticsTracker.incrementPropertiesAddedCount();
				}
			}
		}

		// Designations
		if (theSourceConcept.hasDesignations()) {
			Map<DesignationKey, TermConceptDesignation> existingDesignations = new HashMap<>();
			for (TermConceptDesignation existingDesignation : targetConcept.getDesignations()) {
				DesignationKey key = new DesignationKey(existingDesignation);
				existingDesignations.put(key, existingDesignation);
			}

			for (TermConceptDesignation sourceDesignation : theSourceConcept.getDesignations()) {
				DesignationKey key = new DesignationKey(sourceDesignation);
				TermConceptDesignation targetDesignation = existingDesignations.get(key);
				if (targetDesignation == null) {
					targetDesignation = new TermConceptDesignation();
					targetConcept.getDesignations().add(targetDesignation);
					targetDesignation.setConcept(targetConcept);
					targetDesignation.setCodeSystemVersion(theCsv);
					targetDesignation.setPartitionId(targetConcept.getPartitionId());
				}

				targetDesignation.setLanguage(sourceDesignation.getLanguage());
				targetDesignation.setUseSystem(sourceDesignation.getUseSystem());
				targetDesignation.setUseCode(sourceDesignation.getUseCode());
				targetDesignation.setValue(sourceDesignation.getValue());

				if (targetDesignation.getId() == null) {
					theStatisticsTracker.incrementDesignationsAddedCount();
				}
			}
		}

		// Links to parents
		for (String nextParentCode : theParentCodes) {

			// Don't add parent links that already exist for the code
			if (targetConcept.getParents().stream()
					.anyMatch(t -> t.getParent().getCode().equals(nextParentCode))) {
				continue;
			}

			TermConcept nextParentOpt = theCodeToConcept.get(nextParentCode);
			if (nextParentOpt == null) {
				nextParentOpt = myConceptDao
						.findByCodeSystemAndCode(theCsv.getPid(), nextParentCode)
						.orElse(null);
			}
			if (nextParentOpt == null) {
				throw new InvalidRequestException(Msg.code(846) + "Unable to add code \"" + theSourceConcept.getCode()
						+ "\" to unknown parent: " + nextParentCode);
			}

			nextParentOpt.addChild(targetConcept, TermConceptParentChildLink.RelationshipTypeEnum.ISA);
			theStatisticsTracker.incrementConceptLinksAddedCount();
		}

		saveConcept(targetConcept);
		Long nextConceptPid = targetConcept.getId();
		Validate.notNull(nextConceptPid, "Concept ID cannot be null after saving");

		ourLog.trace("About to save parent-child links");

		// Save children recursively
		int childIndex = 0;
		for (TermConceptParentChildLink nextChildConceptLink : theSourceConcept.getChildren()) {
			TermConcept nextChild = nextChildConceptLink.getChild();

			addConceptInHierarchy(
					theCsv,
					Set.of(theSourceConcept.getCode()),
					nextChild,
					theStatisticsTracker,
					theCodeToConcept,
					childIndex);

			childIndex++;
		}
	}

	private void persistChildren(
			TermConcept theConcept,
			TermCodeSystemVersion theCodeSystem,
			IdentityHashMap<TermConcept, Object> theConceptsStack,
			int theTotalConcepts) {
		if (theConceptsStack.put(theConcept, PLACEHOLDER_OBJECT) != null) {
			return;
		}

		if ((theConceptsStack.size() + 1) % 10000 == 0) {
			float pct = (float) theConceptsStack.size() / (float) theTotalConcepts;
			ourLog.info(
					"Have processed {}/{} concepts ({}%)",
					theConceptsStack.size(), theTotalConcepts, (int) (pct * 100.0f));
		}

		theConcept.setCodeSystemVersion(theCodeSystem);
		theConcept.setIndexStatus(EntityIndexStatusEnum.INDEXED_ALL);

		if (theConceptsStack.size() <= myStorageSettings.getDeferIndexingForCodesystemsOfSize()) {
			saveConcept(theConcept);
		} else {
			myDeferredStorageSvc.addConceptToStorageQueue(theConcept);
		}

		for (TermConceptParentChildLink next : theConcept.getChildren()) {
			persistChildren(next.getChild(), theCodeSystem, theConceptsStack, theTotalConcepts);
		}

		for (TermConceptParentChildLink next : theConcept.getChildren()) {
			if (theConceptsStack.size() <= myStorageSettings.getDeferIndexingForCodesystemsOfSize()) {
				saveConceptLink(next);
			} else {
				myDeferredStorageSvc.addConceptLinkToStorageQueue(next);
			}
		}
	}

	private void populateVersion(TermConcept theNext, TermCodeSystemVersion theCodeSystemVersion) {
		theNext.setCodeSystemVersion(theCodeSystemVersion);
		for (TermConceptParentChildLink next : theNext.getChildren()) {
			populateVersion(next.getChild(), theCodeSystemVersion);
		}
		theNext.getProperties().forEach(t -> t.setCodeSystemVersion(theCodeSystemVersion));
		theNext.getDesignations().forEach(t -> t.setCodeSystemVersion(theCodeSystemVersion));
	}

	private void saveConceptLink(TermConceptParentChildLink next) {
		if (next.getId() == null) {
			myConceptParentChildLinkDao.save(next);
		}
	}

	@Nonnull
	private TermCodeSystem getOrCreateDistinctTermCodeSystem(
			String theSystemUri,
			String theSystemName,
			String theSystemVersionId,
			ResourceTable theCodeSystemResourceTable,
			boolean theAllowPlaceholderRepoint) {
		TermCodeSystem codeSystem = myCodeSystemDao.findByCodeSystemUri(theSystemUri);
		if (codeSystem == null) {
			codeSystem = myCodeSystemDao.findByResourcePid((theCodeSystemResourceTable.getId()));
			if (codeSystem == null) {
				codeSystem = new TermCodeSystem();
			}
		} else {
			checkForCodeSystemVersionDuplicate(
					codeSystem,
					theSystemUri,
					theSystemVersionId,
					theCodeSystemResourceTable,
					theAllowPlaceholderRepoint);
		}

		if (codeSystem.getPid() == null) {
			codeSystem.setResource(theCodeSystemResourceTable);
		} else {
			codeSystem.setResourcePid(theCodeSystemResourceTable);
		}
		codeSystem.setCodeSystemUri(theSystemUri);
		codeSystem.setName(theSystemName);
		codeSystem = myCodeSystemDao.save(codeSystem);
		return codeSystem;
	}

	private void checkForCodeSystemVersionDuplicate(
			TermCodeSystem theCodeSystem,
			String theSystemUri,
			String theSystemVersionId,
			ResourceTable theCodeSystemResourceTable,
			boolean theAllowPlaceholderRepoint) {
		TermCodeSystemVersion codeSystemVersionEntity;
		if (theSystemVersionId == null) {
			codeSystemVersionEntity = myCodeSystemVersionDao.findByCodeSystemPidVersionIsNull(theCodeSystem.getPid());
		} else {
			codeSystemVersionEntity =
					myCodeSystemVersionDao.findByCodeSystemPidAndVersion(theCodeSystem.getPid(), theSystemVersionId);
		}

		if (codeSystemVersionEntity == null) {
			return;
		}

		if (Objects.equals(
				codeSystemVersionEntity.getResourcePidValue(),
				theCodeSystemResourceTable.getId().getId())) {
			return;
		}

		// Future: re-keying TermCodeSystemVersion on (resource-pid, version) instead of
		// (codesystem-pid, version) would remove the whole conflict-release helper, at the
		// cost of a schema migration and a wide findByCodeSystemPid* refactor.
		if (tryReleaseConflictingVersionRow(
				codeSystemVersionEntity, theCodeSystemResourceTable, theAllowPlaceholderRepoint)) {
			return;
		}

		throw new UnprocessableEntityException(Msg.code(848)
				+ buildDuplicateCodeSystemMessage(codeSystemVersionEntity, theSystemUri, theSystemVersionId));
	}

	// Created by Claude Opus 4.7
	/**
	 * Try to release a {@code (codesystem-pid, version)} slot owned by a different resource.
	 * Repoint a 0-concept NOTPRESENT placeholder; otherwise mark the orphan deleted if the
	 * caller is updating an existing resource (so a create-create duplicate still throws).
	 *
	 * @return {@code true} if the slot is available; {@code false} for a genuine duplicate.
	 */
	private boolean tryReleaseConflictingVersionRow(
			TermCodeSystemVersion theExistingRow,
			ResourceTable theCurrentResource,
			boolean theAllowPlaceholderRepoint) {
		if (Objects.equals(theExistingRow.getResource().getId(), theCurrentResource.getId())) {
			return true;
		}

		boolean isPlaceholder =
				theAllowPlaceholderRepoint && myConceptDao.countByCodeSystemVersion(theExistingRow.getPid()) == 0;
		boolean isUpdate = isUpdateOfExistingResource(theCurrentResource);

		if (!isPlaceholder && !isUpdate) {
			return false;
		}

		if (isPlaceholder) {
			theExistingRow.setResourcePid(theCurrentResource);
			myCodeSystemVersionDao.save(theExistingRow);
		} else {
			markCodeSystemVersionForDeletion(theExistingRow);
		}
		return true;
	}

	/** {@code meta.versionId} is incremented by {@code super.updateEntity} before we run, so create=1, update&ge;2. */
	private boolean isUpdateOfExistingResource(ResourceTable theResource) {
		return theResource.getVersion() > 1L;
	}

	/** Renames the version row out of its unique slot and queues it for deletion. */
	private void markCodeSystemVersionForDeletion(TermCodeSystemVersion theVersion) {
		theVersion.setCodeSystemVersionId("DELETED_" + UUID.randomUUID());
		myCodeSystemVersionDao.saveAndFlush(theVersion);
		myDeferredStorageSvc.deleteCodeSystemVersion(theVersion);
	}

	// Created by claude-opus-4-6
	private String buildDuplicateCodeSystemMessage(
			TermCodeSystemVersion theVersionEntity, String theSystemUri, String theSystemVersionId) {
		String resourceRef = theVersionEntity
				.getResource()
				.getIdDt()
				.toUnqualifiedVersionless()
				.getValue();
		boolean hasVersion = theSystemVersionId != null;
		String messageKey =
				hasVersion ? "cannotCreateDuplicateCodeSystemUrlAndVersion" : "cannotCreateDuplicateCodeSystemUrl";
		Object[] args = hasVersion
				? new Object[] {theSystemUri, theSystemVersionId, resourceRef}
				: new Object[] {theSystemUri, resourceRef};
		return myContext.getLocalizer().getMessage(TermReadSvcImpl.class, messageKey, args);
	}

	private void populateCodeSystemVersionProperties(
			TermCodeSystemVersion theCodeSystemVersion,
			CodeSystem theCodeSystemResource,
			ResourceTable theResourceTable) {
		theCodeSystemVersion.setResource(theResourceTable);
		theCodeSystemVersion.setCodeSystemDisplayName(theCodeSystemResource.getName());
		theCodeSystemVersion.setCodeSystemVersionId(theCodeSystemResource.getVersion());
	}

	private int validateConceptForStorage(
			TermConcept theConcept,
			TermCodeSystemVersion theCodeSystemVersion,
			ArrayList<String> theConceptsStack,
			IdentityHashMap<TermConcept, Object> theAllConcepts) {
		ValidateUtil.isTrueOrThrowInvalidRequest(
				theConcept.getCodeSystemVersion() != null, "CodeSystemVersion is null");
		ValidateUtil.isNotBlankOrThrowInvalidRequest(
				theConcept.getCode(), "CodeSystem contains a code with no code value");

		theConcept.setCodeSystemVersion(theCodeSystemVersion);
		if (theConceptsStack.contains(theConcept.getCode())) {
			throw new InvalidRequestException(
					Msg.code(849) + "CodeSystem contains circular reference around code " + theConcept.getCode());
		}
		theConceptsStack.add(theConcept.getCode());

		int retVal = 0;
		if (theAllConcepts.put(theConcept, PLACEHOLDER_OBJECT) == null) {
			if (theAllConcepts.size() % 1000 == 0) {
				ourLog.info("Have validated {} concepts", theAllConcepts.size());
			}
			retVal = 1;
		}

		for (TermConceptParentChildLink next : theConcept.getChildren()) {
			next.setCodeSystem(theCodeSystemVersion);
			retVal +=
					validateConceptForStorage(next.getChild(), theCodeSystemVersion, theConceptsStack, theAllConcepts);
		}

		theConceptsStack.remove(theConceptsStack.size() - 1);

		return retVal;
	}

	@Override
	public StartStagingCodeSystemVersionResponse startStagingCodeSystemVersion(
			String theCodeSystemUrl, String theVersionId) {
		Validate.notBlank(theCodeSystemUrl, "theCodeSystemUrl must not be blank");
		Validate.isTrue(
				!theCodeSystemUrl.contains("|"), "theCodeSystemUrl must not be versioned: %s", theCodeSystemUrl);

		return myTxService.withSystemRequestOnDefaultPartition().execute(() -> {
			TermCodeSystem codeSystem = myCodeSystemDao.findByCodeSystemUri(theCodeSystemUrl);
			Validate.notNull(codeSystem, "CodeSystem not found: %s", theCodeSystemUrl);

			TermCodeSystemVersion version = new TermCodeSystemVersion();
			version.setResource(codeSystem.getResource());
			version.setCodeSystemVersionId(UUID.randomUUID().toString());
			version.setCodeSystemIntendedVersionId(theVersionId);
			version.setCodeSystem(codeSystem);
			version.setCodeSystemDisplayName(codeSystem.getName());

			version = myCodeSystemVersionDao.saveAndFlush(version);
			ourLog.info(
					"Created new CodeSystemVersion[url={}, version={}] for writing with PID: {}",
					theVersionId,
					theCodeSystemUrl,
					version.getId());

			return new StartStagingCodeSystemVersionResponse(version.getCodeSystemVersionId());
		});
	}

	@Override
	public UploadStatistics addCodeSystemConcepts(RequestDetails theRequestDetails, IBaseResource theCodeSystem) {
		CodeSystem codeSystem = myVersionCanonicalizer.codeSystemToCanonical(theCodeSystem);

		String systemUrl = codeSystem.getUrl();
		String systemVersionId = codeSystem.getVersion();

		ValidateUtil.isNotBlankOrThrowInvalidRequest(systemUrl, "CodeSystem must have a URL");
		ValidateUtil.isNotBlankOrThrowInvalidRequest(systemVersionId, "CodeSystem version must not be blank");

		boolean dontPopulateParentPids =
				Boolean.TRUE.equals(theCodeSystem.getUserData(DONT_POPULATE_PARENT_PIDS_CS_USERDATA_KEY));

		delectHierarchyCycleAndThrowExceptionIfFound(codeSystem.getConcept(), new HashSet<>());

		return myTxService.withSystemRequestOnDefaultPartition().execute(() -> {
			TermCodeSystemVersion codeSystemVersionEntity =
					myCodeSystemVersionDao.findByCodeSystemUriAndVersion(systemUrl, systemVersionId);

			// If no TermCodeSystemVersion entity is found, there is no CodeSystem already stored
			// with content=not-present and the correct URL and version. So let's create one.
			if (codeSystemVersionEntity == null) {
				validateOrCreateNotPresentCodeSystem(theRequestDetails, systemUrl, systemVersionId, codeSystem);
			}

			codeSystemVersionEntity = myCodeSystemVersionDao.findByCodeSystemUriAndVersion(systemUrl, systemVersionId);

			ValidateUtil.isTrueOrThrowInvalidRequest(
					codeSystemVersionEntity != null,
					"CodeSystemVersion not found: [url=%s, versionId=%s]",
					systemUrl,
					systemVersionId);

			List<TermConcept> additions = new ArrayList<>();
			for (CodeSystem.ConceptDefinitionComponent sourceConcept : codeSystem.getConcept()) {
				TermConcept concept = convertResourceConceptAndChildrenToStorageConcepts(
						new HashSet<>(), sourceConcept, null, dontPopulateParentPids);
				additions.add(concept);
			}

			IdAndPartitionId termCodeSystemPid = new IdAndPartitionId(
					codeSystemVersionEntity.getCodeSystemPid(),
					codeSystemVersionEntity.getPartitionId().getPartitionId());
			TermCodeSystem termCodeSystem =
					myTermCodeSystemDao.findById(termCodeSystemPid).orElseThrow();
			return addConceptsToCodeSystemVersion(termCodeSystem, codeSystemVersionEntity, additions);
		});
	}

	private void delectHierarchyCycleAndThrowExceptionIfFound(
			List<CodeSystem.ConceptDefinitionComponent> theConcepts, Set<String> theCodesInHierarchy) {
		for (CodeSystem.ConceptDefinitionComponent concept : theConcepts) {
			String code = concept.getCode();
			if (theCodesInHierarchy.contains(code)) {
				throw new InvalidRequestException(Msg.code(926) + "Cycle detected around code " + code);
			}
			theCodesInHierarchy.add(code);
			delectHierarchyCycleAndThrowExceptionIfFound(concept.getConcept(), theCodesInHierarchy);
			theCodesInHierarchy.remove(code);
		}
	}

	private IIdType validateOrCreateNotPresentCodeSystem(
			RequestDetails theRequestDetails, String systemUrl, String systemVersionId, CodeSystem codeSystem) {
		IBaseResource codeSystemResource = myTerminologySvc.fetchCodeSystem(systemUrl + "|" + systemVersionId);
		if (codeSystemResource == null) {
			// Stash the concept list so it isn't saved until later
			List<CodeSystem.ConceptDefinitionComponent> stashedConceptList = codeSystem.getConcept();
			codeSystem.setContent(CodeSystem.CodeSystemContentMode.NOTPRESENT);
			codeSystem.setConcept(new ArrayList<>());

			codeSystemResource = myVersionCanonicalizer.codeSystemFromCanonical(codeSystem);
			IFhirResourceDao codeSystemResourceDao = myDaoRegistry.getResourceDao("CodeSystem");
			if (codeSystemResource.getIdElement().hasIdPart()) {
				codeSystemResourceDao.update(codeSystemResource, theRequestDetails);
			} else {
				codeSystemResourceDao.create(codeSystemResource, theRequestDetails);
			}

			codeSystem.setConcept(stashedConceptList);

		} else {
			CodeSystem canonicalSuppliedCodeSystem = myVersionCanonicalizer.codeSystemToCanonical(codeSystemResource);
			if (canonicalSuppliedCodeSystem.getContent() != CodeSystem.CodeSystemContentMode.NOTPRESENT) {
				String actualContentMode = "(null)";
				if (canonicalSuppliedCodeSystem.getContent() != null) {
					actualContentMode = canonicalSuppliedCodeSystem.getContent().toCode();
				}
				throw new InvalidRequestException(Msg.code(2976) + "CodeSystem[id="
						+ codeSystemResource.getIdElement().getIdPart() + ", url=" + systemUrl + ", version="
						+ systemVersionId + "] has content mode " + actualContentMode + " but must be "
						+ CodeSystem.CodeSystemContentMode.NOTPRESENT.toCode());
			}
		}

		return codeSystemResource.getIdElement();
	}

	@Override
	public UploadStatistics removeCodeSystemConcepts(RequestDetails theRequestDetails, IBaseResource theCodeSystem) {
		CodeSystem codeSystem = myVersionCanonicalizer.codeSystemToCanonical(theCodeSystem);

		String systemUrl = codeSystem.getUrl();
		String systemVersionId = codeSystem.getVersion();

		ValidateUtil.isNotBlankOrThrowInvalidRequest(systemUrl, "CodeSystem must have a URL");
		ValidateUtil.isNotBlankOrThrowInvalidRequest(systemVersionId, "CodeSystem version must not be blank");

		IIdType codeSystemId =
				validateOrCreateNotPresentCodeSystem(theRequestDetails, systemUrl, systemVersionId, codeSystem);
		UploadStatistics retVal = new UploadStatistics(codeSystemId);

		myTxService.withSystemRequestOnDefaultPartition().execute(() -> {
			Set<String> codesToRemove = codeSystem.getConcept().stream()
					.map(CodeSystem.ConceptDefinitionComponent::getCode)
					.collect(Collectors.toSet());
			QueryChunker.chunk(codesToRemove, codes -> {
				TermCodeSystemVersion csv =
						myCodeSystemVersionDao.findByCodeSystemUriAndVersion(systemUrl, systemVersionId);

				// We need to delete all termconcepts, and their children. This stream flattens the TermConcepts and
				// their
				// children into a single set of TermConcept objects retrieved from the DB. Note that we have to do this
				// because
				// deleteById() in JPA doesnt appear to actually commit or flush a transaction until way later, and we
				// end up
				// iterating multiple times over the same elements, which screws up our counter.

				// Grab the actual entities
				List<TermConcept> conceptsToRemove = myConceptDao.findByCodeSystemAndCodeList(csv.getPid(), codes);

				// Iterate over the actual entities and fill out their children
				Set<TermConcept> allFoundTermConcepts = conceptsToRemove.stream()
						.flatMap(concept -> flattenChildren(concept).stream())
						.collect(Collectors.toSet());

				// Delete everything about these codes.
				for (TermConcept code : allFoundTermConcepts) {
					deleteEverythingRelatedToConcept(code, retVal);
				}
			});

			return null;
		});

		myTerminologySvc.invalidateCodeSystemCaches();

		return retVal;
	}

	@Override
	public void activateStagingCodeSystemVersion(
			String theCodeSystemUrl, String theStagingVersionId, boolean theMakeCurrent) {
		Validate.notBlank(theCodeSystemUrl, "theCodeSystemUrl must not be blank");
		Validate.notBlank(theStagingVersionId, "theStagingVersionId must not be blank");

		myTxService.withSystemRequestOnDefaultPartition().execute(() -> {
			boolean makeCurrent = theMakeCurrent;
			TermCodeSystemVersion stagingCodeSystemVersionEntity =
					myCodeSystemVersionDao.findByCodeSystemUriAndVersion(theCodeSystemUrl, theStagingVersionId);
			Validate.notNull(
					stagingCodeSystemVersionEntity,
					"CodeSystemVersion not found: [url=%s, versionId=%s]",
					theCodeSystemUrl,
					theStagingVersionId);

			ourLog.info(
					"Activating staging CodeSystemVersion[url={}, versionId={}] with staging version: {}",
					theCodeSystemUrl,
					stagingCodeSystemVersionEntity.getCodeSystemIntendedVersionId(),
					theStagingVersionId);

			String intendedVersion = stagingCodeSystemVersionEntity.getCodeSystemIntendedVersionId();
			if (isBlank(intendedVersion)) {
				throw new PreconditionFailedException(Msg.code(2926)
						+ "CodeSystem[url=%s, versionId=%s] is not currently being staged. Can not activate.");
			}

			TermCodeSystemVersion existingCodeSystemVersionEntity =
					myCodeSystemVersionDao.findByCodeSystemUriAndVersion(theCodeSystemUrl, intendedVersion);
			boolean deleteExistingVersion = existingCodeSystemVersionEntity != null;
			if (deleteExistingVersion) {
				ourLog.info(
						"Deleting existing CodeSystemVersion[url={}, versionId={}] with PID[{}] in order to activate staging version: {}",
						intendedVersion,
						theCodeSystemUrl,
						existingCodeSystemVersionEntity.getId(),
						theStagingVersionId);
				markCodeSystemVersionForDeletion(existingCodeSystemVersionEntity);

				if (Objects.equals(
						existingCodeSystemVersionEntity,
						existingCodeSystemVersionEntity.getCodeSystem().getCurrentVersion())) {
					ourLog.info(
							"Forcing staging version of CodeSystem[url={}, versionId={}] to be the current version, as the existing current version is being deleted",
							intendedVersion,
							theCodeSystemUrl);
					makeCurrent = true;
				}
			}

			stagingCodeSystemVersionEntity.setCodeSystemVersionId(
					stagingCodeSystemVersionEntity.getCodeSystemIntendedVersionId());
			stagingCodeSystemVersionEntity.setCodeSystemIntendedVersionId(null);
			stagingCodeSystemVersionEntity = myEntityManager.merge(stagingCodeSystemVersionEntity);

			if (makeCurrent) {
				TermCodeSystem codeSystem = stagingCodeSystemVersionEntity.getCodeSystem();
				codeSystem.setCurrentVersionPid(stagingCodeSystemVersionEntity);
			}
		});
	}

	static final String CONCEPT_PROPERTY_PARENT_URI = "http://hl7.org/fhir/concept-properties#parent";
	static final String CONCEPT_PROPERTY_CHILD_URI = "http://hl7.org/fhir/concept-properties#child";

	/**
	 * Builds parent/child links for CodeSystems that express their hierarchy FLAT — i.e. concepts sit in a
	 * flat list and each carries a {@code parent} (or {@code child}) concept-property, rather than being
	 * nested via {@code CodeSystem.concept}. The {@code parent}/{@code child} properties are identified by
	 * their reserved code name; a property the CodeSystem declares with a URI other than the canonical
	 * concept-properties URI ({@value #CONCEPT_PROPERTY_PARENT_URI} / {@value #CONCEPT_PROPERTY_CHILD_URI})
	 * has a different meaning and is skipped. This runs in addition to the nested-concept handling, so a
	 * mixed representation is supported and existing links are never duplicated.
	 */
	static void linkFlatHierarchyFromConceptProperties(CodeSystem theCodeSystem, TermCodeSystemVersion thePersCs) {
		String parentPropertyCode = hierarchyPropertyCodeOrNull(theCodeSystem, "parent", CONCEPT_PROPERTY_PARENT_URI);
		String childPropertyCode = hierarchyPropertyCodeOrNull(theCodeSystem, "child", CONCEPT_PROPERTY_CHILD_URI);
		if (parentPropertyCode == null && childPropertyCode == null) {
			return;
		}
		if (!hasAnyHierarchyProperty(theCodeSystem.getConcept(), parentPropertyCode, childPropertyCode)) {
			// No concept actually carries a flat parent/child property → nothing to link (avoids indexing).
			return;
		}

		Map<String, TermConcept> conceptsByCode = new HashMap<>();
		for (TermConcept concept : thePersCs.getConcepts()) {
			indexConceptsByCode(concept, conceptsByCode);
		}

		linkFlatHierarchy(theCodeSystem.getConcept(), conceptsByCode, parentPropertyCode, childPropertyCode);
	}

	/**
	 * Returns the reserved concept-property code ({@code parent}/{@code child}) so the flat hierarchy is
	 * matched by name, unless the CodeSystem declares a property with that code but a URI other than the
	 * canonical concept-properties URI — in which case its meaning is unknown and {@code null} is returned so
	 * it is not treated as hierarchy.
	 */
	private static String hierarchyPropertyCodeOrNull(
			CodeSystem theCodeSystem, String theReservedCode, String theCanonicalUri) {
		for (CodeSystem.PropertyComponent property : theCodeSystem.getProperty()) {
			if (theReservedCode.equals(property.getCode())
					&& property.hasUri()
					&& !theCanonicalUri.equals(property.getUri())) {
				ourLog.info(
						"CodeSystem {} declares property '{}' with URI '{}' rather than the standard '{}'; it is not treated as a hierarchy property and no parent/child links are built from it.",
						theCodeSystem.getUrl(),
						theReservedCode,
						property.getUri(),
						theCanonicalUri);
				return null;
			}
		}
		return theReservedCode;
	}

	/**
	 * Cheap pre-check (no allocation) for whether any concept carries a flat {@code parent}/{@code child}
	 * property, so CodeSystems without a flat hierarchy skip the concept indexing entirely.
	 */
	private static boolean hasAnyHierarchyProperty(
			List<CodeSystem.ConceptDefinitionComponent> theConcepts, String theParentCode, String theChildCode) {
		for (CodeSystem.ConceptDefinitionComponent concept : theConcepts) {
			for (CodeSystem.ConceptPropertyComponent property : concept.getProperty()) {
				String code = property.getCode();
				if ((theParentCode != null && theParentCode.equals(code))
						|| (theChildCode != null && theChildCode.equals(code))) {
					return true;
				}
			}
			if (hasAnyHierarchyProperty(concept.getConcept(), theParentCode, theChildCode)) {
				return true;
			}
		}
		return false;
	}

	private static void indexConceptsByCode(TermConcept theConcept, Map<String, TermConcept> theConceptsByCode) {
		theConceptsByCode.putIfAbsent(theConcept.getCode(), theConcept);
		for (TermConceptParentChildLink childLink : theConcept.getChildren()) {
			indexConceptsByCode(childLink.getChild(), theConceptsByCode);
		}
	}

	private static void linkFlatHierarchy(
			List<CodeSystem.ConceptDefinitionComponent> theSourceConcepts,
			Map<String, TermConcept> theConceptsByCode,
			String theParentPropertyCode,
			String theChildPropertyCode) {
		for (CodeSystem.ConceptDefinitionComponent sourceConcept : theSourceConcepts) {
			TermConcept concept = theConceptsByCode.get(sourceConcept.getCode());
			if (concept != null) {
				for (CodeSystem.ConceptPropertyComponent property : sourceConcept.getProperty()) {
					if (!property.hasValue() || !property.getValue().isPrimitive()) {
						continue;
					}
					String relatedCode = property.getValue().primitiveValue();
					if (isBlank(relatedCode)) {
						continue;
					}
					if (theParentPropertyCode != null && theParentPropertyCode.equals(property.getCode())) {
						addChildLinkIfAbsent(theConceptsByCode.get(relatedCode), concept);
					} else if (theChildPropertyCode != null && theChildPropertyCode.equals(property.getCode())) {
						addChildLinkIfAbsent(concept, theConceptsByCode.get(relatedCode));
					}
				}
			}
			linkFlatHierarchy(
					sourceConcept.getConcept(), theConceptsByCode, theParentPropertyCode, theChildPropertyCode);
		}
	}

	private static void addChildLinkIfAbsent(TermConcept theParent, TermConcept theChild) {
		if (theParent == null || theChild == null || theParent == theChild) {
			return;
		}
		boolean alreadyLinked = theChild.getParents().stream().anyMatch(link -> theParent.equals(link.getParent()));
		if (!alreadyLinked) {
			theParent.addChild(theChild, TermConceptParentChildLink.RelationshipTypeEnum.ISA);
		}
	}

	private TermConcept convertResourceConceptAndChildrenToStorageConcepts(
			Set<String> theSeenCodes,
			CodeSystem.ConceptDefinitionComponent theSourceConcept,
			TermConcept theParentConcept,
			boolean theDontPopulateParentPids) {
		TermConcept targetConcept = new TermConcept();
		targetConcept.setCode(theSourceConcept.getCode());
		targetConcept.setDisplay(theSourceConcept.getDisplay());
		if (theDontPopulateParentPids) {
			targetConcept.setDontPopulateParentPids(true);
		}

		// Populate designations
		for (CodeSystem.ConceptDefinitionDesignationComponent sourceDesignation : theSourceConcept.getDesignation()) {
			TermConceptDesignation targetDesignation = new TermConceptDesignation();
			targetConcept.getDesignations().add(targetDesignation);
			targetDesignation.setConcept(targetConcept);
			targetDesignation.setLanguage(sourceDesignation.getLanguage());
			if (sourceDesignation.hasUse()) {
				targetDesignation.setUseSystem(sourceDesignation.getUse().getSystem());
				targetDesignation.setUseCode(sourceDesignation.getUse().getCode());
			}
			targetDesignation.setValue(sourceDesignation.getValue());
		}

		// Populate properties
		for (CodeSystem.ConceptPropertyComponent sourceProperty : theSourceConcept.getProperty()) {
			TermConceptProperty targetProperty = new TermConceptProperty();
			targetConcept.getProperties().add(targetProperty);
			targetProperty.setConcept(targetConcept);
			targetProperty.setPartitionId(targetConcept.getPartitionId());
			targetProperty.setKey(sourceProperty.getCode());

			populateTermConceptPropertyValue(sourceProperty, targetProperty);
		}

		// Parent Concept
		if (theParentConcept != null) {
			theParentConcept.addChild(targetConcept, TermConceptParentChildLink.RelationshipTypeEnum.ISA);
		}

		// Child Concepts
		for (CodeSystem.ConceptDefinitionComponent child : theSourceConcept.getConcept()) {
			if (theSeenCodes.add(child.getCode())) {
				convertResourceConceptAndChildrenToStorageConcepts(
						theSeenCodes, child, targetConcept, theDontPopulateParentPids);
			}
		}

		return targetConcept;
	}

	private Set<String> findAllCodes(List<TermConcept> theCodeSystem) {
		Set<String> codes = new HashSet<>();
		findAllCodes(codes, theCodeSystem);
		return codes;
	}

	private void findAllCodes(Set<String> theCodesToPopulate, List<TermConcept> theConcepts) {
		for (TermConcept next : theConcepts) {
			theCodesToPopulate.add(next.getCode());
			findAllCodes(theCodesToPopulate, next.getChildCodes());
			for (TermConceptParentChildLink parentLink : next.getParents()) {
				theCodesToPopulate.add(parentLink.getParent().getCode());
			}
		}
	}

	public static void populateTermConceptPropertyValue(
			CodeSystem.ConceptPropertyComponent theConceptPropertyComponent, TermConceptProperty theStorageProperty) {
		if (theConceptPropertyComponent.getValue() instanceof CodeType) {
			theStorageProperty.setType(TermConceptPropertyTypeEnum.CODE);
			theStorageProperty.setValue(((CodeType) theConceptPropertyComponent.getValue()).getValueAsString());
		} else if (theConceptPropertyComponent.getValue() instanceof StringType) {
			theStorageProperty.setType(TermConceptPropertyTypeEnum.STRING);
			theStorageProperty.setValue(
					theConceptPropertyComponent.getValueStringType().getValue());
		} else if (theConceptPropertyComponent.getValue() instanceof BooleanType) {
			theStorageProperty.setType(TermConceptPropertyTypeEnum.BOOLEAN);
			theStorageProperty.setValue(((BooleanType) theConceptPropertyComponent.getValue()).getValueAsString());
		} else if (theConceptPropertyComponent.getValue() instanceof IntegerType) {
			theStorageProperty.setType(TermConceptPropertyTypeEnum.INTEGER);
			theStorageProperty.setValue(((IntegerType) theConceptPropertyComponent.getValue()).getValueAsString());
		} else if (theConceptPropertyComponent.getValue() instanceof DecimalType) {
			theStorageProperty.setType(TermConceptPropertyTypeEnum.DECIMAL);
			theStorageProperty.setValue(((DecimalType) theConceptPropertyComponent.getValue()).getValueAsString());
		} else if (theConceptPropertyComponent.getValue() instanceof DateTimeType) {
			theStorageProperty.setType(TermConceptPropertyTypeEnum.DATETIME);
			theStorageProperty.setValue(((DateTimeType) theConceptPropertyComponent.getValue()).getValueAsString());
		} else if (theConceptPropertyComponent.getValue() instanceof Coding) {
			Coding nextCoding = theConceptPropertyComponent.getValueCoding();
			theStorageProperty.setType(TermConceptPropertyTypeEnum.CODING);
			theStorageProperty.setCodeSystem(nextCoding.getSystem());
			theStorageProperty.setValue(nextCoding.getCode());
			theStorageProperty.setDisplay(nextCoding.getDisplay());
		} else if (theConceptPropertyComponent.getValue() != null) {
			throw new InvalidRequestException(Msg.code(2927) + "Don't know how to handle concept properties of type: "
					+ theConceptPropertyComponent.getValue().getClass());
		}
	}

	private record DesignationKey(String language, String useSystem, String useCode) {
		public DesignationKey(TermConceptDesignation theDesignation) {
			this(theDesignation.getLanguage(), theDesignation.getUseSystem(), theDesignation.getUseCode());
		}
	}
}
