package ca.uhn.fhir.jpa.term;

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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.dao.BaseHapiFhirDao;
import ca.uhn.fhir.jpa.dao.data.IResourceTableDao;
import ca.uhn.fhir.jpa.dao.data.ITermCodeSystemDao;
import ca.uhn.fhir.jpa.dao.data.ITermCodeSystemVersionDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptDesignationDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptParentChildLinkDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptPropertyDao;
import ca.uhn.fhir.jpa.entity.TermCodeSystem;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.entity.TermConceptDesignation;
import ca.uhn.fhir.jpa.entity.TermConceptParentChildLink;
import ca.uhn.fhir.jpa.entity.TermConceptProperty;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemStorageSvc;
import ca.uhn.fhir.jpa.term.api.ITermDeferredStorageSvc;
import ca.uhn.fhir.jpa.term.api.ITermReadSvc;
import ca.uhn.fhir.jpa.term.api.ITermVersionAdapterSvc;
import ca.uhn.fhir.jpa.term.custom.CustomTerminologySet;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.util.ObjectUtil;
import ca.uhn.fhir.util.ValidateUtil;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.ValueSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.Nonnull;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static ca.uhn.fhir.jpa.api.dao.IDao.RESOURCE_PID_KEY;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.hl7.fhir.common.hapi.validation.support.ValidationConstants.LOINC_LOW;

public class TermCodeSystemStorageSvcImpl implements ITermCodeSystemStorageSvc {
	private static final Logger ourLog = LoggerFactory.getLogger(TermCodeSystemStorageSvcImpl.class);
	private static final Object PLACEHOLDER_OBJECT = new Object();
	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	protected EntityManager myEntityManager;
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
	protected IIdHelperService myIdHelperService;
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
	private DaoConfig myDaoConfig;
	@Autowired
	private IResourceTableDao myResourceTableDao;

	@Autowired
	private TermConceptDaoSvc myTermConceptDaoSvc;


	@Transactional
	@Override
	public UploadStatistics applyDeltaCodeSystemsAdd(String theSystem, CustomTerminologySet theAdditions) {
		ValidateUtil.isNotBlankOrThrowInvalidRequest(theSystem, "No system provided");
		validateDstu3OrNewer();
		theAdditions.validateNoCycleOrThrowInvalidRequest();

		TermCodeSystem cs = myCodeSystemDao.findByCodeSystemUri(theSystem);
		if (cs == null) {
			CodeSystem codeSystemResource = new CodeSystem();
			codeSystemResource.setUrl(theSystem);
			codeSystemResource.setContent(CodeSystem.CodeSystemContentMode.NOTPRESENT);
			if (isBlank(codeSystemResource.getIdElement().getIdPart()) && theSystem.contains(LOINC_LOW)) {
				codeSystemResource.setId(LOINC_LOW);
			}
			myTerminologyVersionAdapterSvc.createOrUpdateCodeSystem(codeSystemResource);

			cs = myCodeSystemDao.findByCodeSystemUri(theSystem);
		}

		TermCodeSystemVersion csv = cs.getCurrentVersion();
		Validate.notNull(csv);

		CodeSystem codeSystem = myTerminologySvc.fetchCanonicalCodeSystemFromCompleteContext(theSystem);
		if (codeSystem.getContent() != CodeSystem.CodeSystemContentMode.NOTPRESENT) {
			throw new InvalidRequestException(Msg.code(844) + "CodeSystem with url[" + Constants.codeSystemWithDefaultDescription(theSystem) + "] can not apply a delta - wrong content mode: " + codeSystem.getContent());
		}

		Validate.notNull(cs);
		Validate.notNull(cs.getPid());

		IIdType codeSystemId = cs.getResource().getIdDt();

		UploadStatistics retVal = new UploadStatistics(codeSystemId);
		HashMap<String, TermConcept> codeToConcept = new HashMap<>();

		// Add root concepts
		for (TermConcept nextRootConcept : theAdditions.getRootConcepts()) {
			List<String> parentCodes = Collections.emptyList();
			addConceptInHierarchy(csv, parentCodes, nextRootConcept, retVal, codeToConcept, 0);
		}

		return retVal;
	}

	@Transactional
	@Override
	public UploadStatistics applyDeltaCodeSystemsRemove(String theSystem, CustomTerminologySet theValue) {
		ValidateUtil.isNotBlankOrThrowInvalidRequest(theSystem, "No system provided");
		validateDstu3OrNewer();

		TermCodeSystem cs = myCodeSystemDao.findByCodeSystemUri(theSystem);
		if (cs == null) {
			throw new InvalidRequestException(Msg.code(845) + "Unknown code system: " + theSystem);
		}
		IIdType target = cs.getResource().getIdDt();

		AtomicInteger removeCounter = new AtomicInteger(0);

		//We need to delete all termconcepts, and their children. This stream flattens the TermConcepts and their
		//children into a single set of TermConcept objects retrieved from the DB. Note that we have to do this because
		//deleteById() in JPA doesnt appear to actually commit or flush a transaction until way later, and we end up
		//iterating multiple times over the same elements, which screws up our counter.


		//Grab the actual entities
		List<TermConcept> collect = theValue.getRootConcepts().stream()
			.map(val -> myTerminologySvc.findCode(theSystem, val.getCode()))
			.filter(Optional::isPresent)
			.map(Optional::get)
			.collect(Collectors.toList());

		//Iterate over the actual entities and fill out their children
		Set<TermConcept> allFoundTermConcepts = collect
			.stream()
			.flatMap(concept -> flattenChildren(concept).stream())
			.map(suppliedTermConcept -> myTerminologySvc.findCode(theSystem, suppliedTermConcept.getCode()))
			.filter(Optional::isPresent)
			.map(Optional::get)
			.collect(Collectors.toSet());

		//Delete everything about these codes.
		for (TermConcept code : allFoundTermConcepts) {
			deleteEverythingRelatedToConcept(code, removeCounter);
		}

		return new UploadStatistics(removeCounter.get(), target);
	}

	private void deleteEverythingRelatedToConcept(TermConcept theConcept, AtomicInteger theRemoveCounter) {

		for (TermConceptParentChildLink nextParent : theConcept.getParents()) {
			nextParent.getParent().getChildren().remove(nextParent);
			myConceptParentChildLinkDao.deleteById(nextParent.getId());
		}
		for (TermConceptParentChildLink nextChild : theConcept.getChildren()) {
			nextChild.getChild().getParents().remove(nextChild);
			myConceptParentChildLinkDao.deleteById(nextChild.getId());
		}

		for (TermConceptDesignation next : theConcept.getDesignations()) {
			myConceptDesignationDao.deleteById(next.getPid());
		}
		theConcept.getDesignations().clear();
		for (TermConceptProperty next : theConcept.getProperties()) {
			myConceptPropertyDao.deleteById(next.getPid());
		}
		theConcept.getProperties().clear();

		ourLog.info("Deleting concept {} - Code {}", theConcept.getId(), theConcept.getCode());

		myConceptDao.deleteById(theConcept.getId());
//		myEntityManager.remove(theConcept);

		theRemoveCounter.incrementAndGet();
	}

	private List<TermConcept> flattenChildren(TermConcept theTermConcept) {
		if (theTermConcept.getChildren().isEmpty()) {
			return Arrays.asList(theTermConcept);
		}

		//Recursively flatten children
		List<TermConcept> childTermConcepts = theTermConcept.getChildren().stream()
			.map(TermConceptParentChildLink::getChild)
			.flatMap(childConcept -> flattenChildren(childConcept).stream())
			.collect(Collectors.toList());

		//Add itself before its list of children
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

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public void storeNewCodeSystemVersionIfNeeded(CodeSystem theCodeSystem, ResourceTable theResourceEntity, RequestDetails theRequestDetails) {
		if (theCodeSystem != null && isNotBlank(theCodeSystem.getUrl())) {
			String codeSystemUrl = theCodeSystem.getUrl();
			if (theCodeSystem.getContent() == CodeSystem.CodeSystemContentMode.COMPLETE || theCodeSystem.getContent() == null || theCodeSystem.getContent() == CodeSystem.CodeSystemContentMode.NOTPRESENT) {
				ourLog.info("CodeSystem {} has a status of {}, going to store concepts in terminology tables", theResourceEntity.getIdDt().getValue(), theCodeSystem.getContentElement().getValueAsString());

				Long pid = (Long)theCodeSystem.getUserData(RESOURCE_PID_KEY);
				assert pid != null;
				ResourcePersistentId codeSystemResourcePid = new ResourcePersistentId(pid);

				/*
				 * If this is a not-present codesystem and codesystem version already exists, we don't want to
				 * overwrite the existing version since that will wipe out the existing concepts. We do create
				 * or update the TermCodeSystem table though, since that allows the DB to reject changes that would
				 * result in duplicate CodeSystem.url values.
				 */
				if (theCodeSystem.getContent() == CodeSystem.CodeSystemContentMode.NOTPRESENT) {
					TermCodeSystem termCodeSystem = myCodeSystemDao.findByCodeSystemUri(theCodeSystem.getUrl());
					if (termCodeSystem != null) {
						TermCodeSystemVersion codeSystemVersion = getExistingTermCodeSystemVersion(termCodeSystem.getPid(), theCodeSystem.getVersion());
						if (codeSystemVersion != null) {
							TermCodeSystem myCodeSystemEntity = getOrCreateDistinctTermCodeSystem(codeSystemResourcePid, theCodeSystem.getUrl(), theCodeSystem.getUrl(), theCodeSystem.getVersion(), theResourceEntity);
							return;
						}
					}
				}

				TermCodeSystemVersion persCs = new TermCodeSystemVersion();

				populateCodeSystemVersionProperties(persCs, theCodeSystem, theResourceEntity);

				persCs.getConcepts().addAll(BaseTermReadSvcImpl.toPersistedConcepts(theCodeSystem.getConcept(), persCs));
				ourLog.debug("Code system has {} concepts", persCs.getConcepts().size());
				storeNewCodeSystemVersion(codeSystemResourcePid, codeSystemUrl, theCodeSystem.getName(),
					theCodeSystem.getVersion(), persCs, theResourceEntity, theRequestDetails);
			}

		}
	}

	@Override
	@Transactional
	public IIdType storeNewCodeSystemVersion(CodeSystem theCodeSystemResource, TermCodeSystemVersion theCodeSystemVersion,
														  RequestDetails theRequest, List<ValueSet> theValueSets, List<ConceptMap> theConceptMaps) {
		assert TransactionSynchronizationManager.isActualTransactionActive();

		Validate.notBlank(theCodeSystemResource.getUrl(), "theCodeSystemResource must have a URL");

		// Note that this creates the TermCodeSystem and TermCodeSystemVersion entities if needed
		IIdType csId = myTerminologyVersionAdapterSvc.createOrUpdateCodeSystem(theCodeSystemResource, theRequest);

		ResourcePersistentId codeSystemResourcePid = myIdHelperService.resolveResourcePersistentIds(RequestPartitionId.allPartitions(), csId.getResourceType(), csId.getIdPart());
		ResourceTable resource = myResourceTableDao.getOne(codeSystemResourcePid.getIdAsLong());

		ourLog.info("CodeSystem resource has ID: {}", csId.getValue());

		populateCodeSystemVersionProperties(theCodeSystemVersion, theCodeSystemResource, resource);

		storeNewCodeSystemVersion(codeSystemResourcePid, theCodeSystemResource.getUrl(), theCodeSystemResource.getName(),
			theCodeSystemResource.getVersion(), theCodeSystemVersion, resource, theRequest);

		myDeferredStorageSvc.addConceptMapsToStorageQueue(theConceptMaps);
		myDeferredStorageSvc.addValueSetsToStorageQueue(theValueSets);

		return csId;
	}

	@Override
	@Transactional
	public void storeNewCodeSystemVersion(ResourcePersistentId theCodeSystemResourcePid, String theSystemUri,
													  String theSystemName, String theCodeSystemVersionId, TermCodeSystemVersion theCodeSystemVersion,
													  ResourceTable theCodeSystemResourceTable, RequestDetails theRequestDetails) {
		assert TransactionSynchronizationManager.isActualTransactionActive();

		ourLog.debug("Storing code system");

		TermCodeSystemVersion codeSystemToStore = theCodeSystemVersion;
		ValidateUtil.isTrueOrThrowInvalidRequest(codeSystemToStore.getResource() != null, "No resource supplied");
		ValidateUtil.isNotBlankOrThrowInvalidRequest(theSystemUri, "No system URI supplied");

		TermCodeSystem codeSystem = getOrCreateDistinctTermCodeSystem(theCodeSystemResourcePid, theSystemUri, theSystemName, theCodeSystemVersionId, theCodeSystemResourceTable);

		List<TermCodeSystemVersion> existing = myCodeSystemVersionDao.findByCodeSystemResourcePid(theCodeSystemResourcePid.getIdAsLong());
		for (TermCodeSystemVersion next : existing) {
			if (Objects.equals(next.getCodeSystemVersionId(), theCodeSystemVersionId) && myConceptDao.countByCodeSystemVersion(next.getPid()) == 0) {

				/*
				 * If we already have a CodeSystemVersion that matches the version we're storing, we
				 * can reuse it.
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
				next.setCodeSystemVersionId("DELETED_" + UUID.randomUUID().toString());
				myCodeSystemVersionDao.saveAndFlush(next);
				myDeferredStorageSvc.deleteCodeSystemVersion(next);

			}
		}

		/*
		 * Do the upload
		 */

		codeSystemToStore.setCodeSystem(codeSystem);
		codeSystemToStore.setCodeSystemDisplayName(theSystemName);
		codeSystemToStore.setCodeSystemVersionId(theCodeSystemVersionId);

		ourLog.debug("Validating all codes in CodeSystem for storage (this can take some time for large sets)");

		// Validate the code system
		ArrayList<String> conceptsStack = new ArrayList<>();
		IdentityHashMap<TermConcept, Object> allConcepts = new IdentityHashMap<>();
		int totalCodeCount = 0;
		Collection<TermConcept> conceptsToSave = theCodeSystemVersion.getConcepts();
		for (TermConcept next : conceptsToSave) {
			totalCodeCount += validateConceptForStorage(next, codeSystemToStore, conceptsStack, allConcepts);
		}

		ourLog.debug("Saving version containing {} concepts", totalCodeCount);
		if (codeSystemToStore.getPid() == null) {
			codeSystemToStore = myCodeSystemVersionDao.saveAndFlush(codeSystemToStore);
		}

		boolean isMakeVersionCurrent = ITermCodeSystemStorageSvc.isMakeVersionCurrent(theRequestDetails);
		if (isMakeVersionCurrent) {
			codeSystem.setCurrentVersion(codeSystemToStore);
			if (codeSystem.getPid() == null) {
				codeSystem = myCodeSystemDao.saveAndFlush(codeSystem);
			}
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
		if (!myDeferredStorageSvc.isStorageQueueEmpty()) {
			ourLog.info("Note that some concept saving has been deferred");
		}
	}


	private TermCodeSystemVersion getExistingTermCodeSystemVersion(Long theCodeSystemVersionPid, String theCodeSystemVersion) {
		TermCodeSystemVersion existing;
		if (theCodeSystemVersion == null) {
			existing = myCodeSystemVersionDao.findByCodeSystemPidVersionIsNull(theCodeSystemVersionPid);
		} else {
			existing = myCodeSystemVersionDao.findByCodeSystemPidAndVersion(theCodeSystemVersionPid, theCodeSystemVersion);
		}

		return existing;
	}


	private void validateDstu3OrNewer() {
		Validate.isTrue(myContext.getVersion().getVersion().isEqualOrNewerThan(FhirVersionEnum.DSTU3), "Terminology operations only supported in DSTU3+ mode");
	}

	private void addConceptInHierarchy(TermCodeSystemVersion theCsv, Collection<String> theParentCodes, TermConcept theConceptToAdd, UploadStatistics theStatisticsTracker, Map<String, TermConcept> theCodeToConcept, int theSequence) {
		TermConcept conceptToAdd = theConceptToAdd;
		List<TermConceptParentChildLink> childrenToAdd = theConceptToAdd.getChildren();

		String nextCodeToAdd = conceptToAdd.getCode();
		String parentDescription = "(root concept)";

		ourLog.info("Saving concept {} with parent {}", theStatisticsTracker.getUpdatedConceptCount(), parentDescription);

		Optional<TermConcept> existingCodeOpt = myConceptDao.findByCodeSystemAndCode(theCsv, nextCodeToAdd);
		List<TermConceptParentChildLink> existingParentLinks;
		if (existingCodeOpt.isPresent()) {
			TermConcept existingCode = existingCodeOpt.get();
			existingCode.setIndexStatus(null);
			existingCode.setDisplay(conceptToAdd.getDisplay());
			conceptToAdd = existingCode;
			existingParentLinks = conceptToAdd.getParents();
		} else {
			existingParentLinks = Collections.emptyList();
		}

		Set<TermConcept> parentConceptsWeShouldLinkTo = new HashSet<>();
		for (String nextParentCode : theParentCodes) {

			// Don't add parent links that already exist for the code
			if (existingParentLinks.stream().anyMatch(t -> t.getParent().getCode().equals(nextParentCode))) {
				continue;
			}

			TermConcept nextParentOpt = theCodeToConcept.get(nextParentCode);
			if (nextParentOpt == null) {
				nextParentOpt = myConceptDao.findByCodeSystemAndCode(theCsv, nextParentCode).orElse(null);
			}
			if (nextParentOpt == null) {
				throw new InvalidRequestException(Msg.code(846) + "Unable to add code \"" + nextCodeToAdd + "\" to unknown parent: " + nextParentCode);
			}
			parentConceptsWeShouldLinkTo.add(nextParentOpt);
		}

		if (conceptToAdd.getSequence() == null) {
			conceptToAdd.setSequence(theSequence);
		}

		// Null out the hierarchy PIDs for this concept always. We do this because we're going to
		// force a reindex, and it'll be regenerated then
		conceptToAdd.setParentPids(null);
		conceptToAdd.setCodeSystemVersion(theCsv);

		if (conceptToAdd.getProperties() != null)
			conceptToAdd.getProperties().forEach(termConceptProperty -> {
				termConceptProperty.setConcept(theConceptToAdd);
				termConceptProperty.setCodeSystemVersion(theCsv);
			});
		if (theStatisticsTracker.getUpdatedConceptCount() <= myDaoConfig.getDeferIndexingForCodesystemsOfSize()) {
			saveConcept(conceptToAdd);
			Long nextConceptPid = conceptToAdd.getId();
			Validate.notNull(nextConceptPid);
		} else {
			myDeferredStorageSvc.addConceptToStorageQueue(conceptToAdd);
		}

		theCodeToConcept.put(conceptToAdd.getCode(), conceptToAdd);

		theStatisticsTracker.incrementUpdatedConceptCount();

		// Add link to new child to the parent
		for (TermConcept nextParentConcept : parentConceptsWeShouldLinkTo) {
			TermConceptParentChildLink parentLink = new TermConceptParentChildLink();
			parentLink.setParent(nextParentConcept);
			parentLink.setChild(conceptToAdd);
			parentLink.setCodeSystem(theCsv);
			parentLink.setRelationshipType(TermConceptParentChildLink.RelationshipTypeEnum.ISA);
			nextParentConcept.getChildren().add(parentLink);
			conceptToAdd.getParents().add(parentLink);
			ourLog.info("Saving parent/child link - Parent[{}] Child[{}]", parentLink.getParent().getCode(), parentLink.getChild().getCode());

			if (theStatisticsTracker.getUpdatedConceptCount() <= myDaoConfig.getDeferIndexingForCodesystemsOfSize()) {
				myConceptParentChildLinkDao.save(parentLink);
			} else {
				myDeferredStorageSvc.addConceptLinkToStorageQueue(parentLink);
			}

		}

		ourLog.trace("About to save parent-child links");

		// Save children recursively
		int childIndex = 0;
		for (TermConceptParentChildLink nextChildConceptLink : new ArrayList<>(childrenToAdd)) {

			TermConcept nextChild = nextChildConceptLink.getChild();

			for (int i = 0; i < nextChild.getParents().size(); i++) {
				if (nextChild.getParents().get(i).getId() == null) {
					String parentCode = nextChild.getParents().get(i).getParent().getCode();
					TermConcept parentConcept = theCodeToConcept.get(parentCode);
					if (parentConcept == null) {
						parentConcept = myConceptDao.findByCodeSystemAndCode(theCsv, parentCode).orElse(null);
					}
					if (parentConcept == null) {
						throw new IllegalArgumentException(Msg.code(847) + "Unknown parent code: " + parentCode);
					}

					nextChild.getParents().get(i).setParent(parentConcept);
				}
			}

			Collection<String> parentCodes = nextChild.getParents().stream().map(t -> t.getParent().getCode()).collect(Collectors.toList());
			addConceptInHierarchy(theCsv, parentCodes, nextChild, theStatisticsTracker, theCodeToConcept, childIndex);

			childIndex++;
		}

	}

	private void persistChildren(TermConcept theConcept, TermCodeSystemVersion theCodeSystem, IdentityHashMap<TermConcept, Object> theConceptsStack, int theTotalConcepts) {
		if (theConceptsStack.put(theConcept, PLACEHOLDER_OBJECT) != null) {
			return;
		}

		if ((theConceptsStack.size() + 1) % 10000 == 0) {
			float pct = (float) theConceptsStack.size() / (float) theTotalConcepts;
			ourLog.info("Have processed {}/{} concepts ({}%)", theConceptsStack.size(), theTotalConcepts, (int) (pct * 100.0f));
		}

		theConcept.setCodeSystemVersion(theCodeSystem);
		theConcept.setIndexStatus(BaseHapiFhirDao.INDEX_STATUS_INDEXED);

		if (theConceptsStack.size() <= myDaoConfig.getDeferIndexingForCodesystemsOfSize()) {
			saveConcept(theConcept);
		} else {
			myDeferredStorageSvc.addConceptToStorageQueue(theConcept);
		}

		for (TermConceptParentChildLink next : theConcept.getChildren()) {
			persistChildren(next.getChild(), theCodeSystem, theConceptsStack, theTotalConcepts);
		}

		for (TermConceptParentChildLink next : theConcept.getChildren()) {
			if (theConceptsStack.size() <= myDaoConfig.getDeferIndexingForCodesystemsOfSize()) {
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

	private int ensureParentsSaved(Collection<TermConceptParentChildLink> theParents) {
		ourLog.trace("Checking {} parents", theParents.size());
		int retVal = 0;

		for (TermConceptParentChildLink nextLink : theParents) {
			if (nextLink.getRelationshipType() == TermConceptParentChildLink.RelationshipTypeEnum.ISA) {
				TermConcept nextParent = nextLink.getParent();
				retVal += ensureParentsSaved(nextParent.getParents());
				if (nextParent.getId() == null) {
					nextParent.setUpdated(new Date());
					myConceptDao.saveAndFlush(nextParent);
					retVal++;
					ourLog.debug("Saved parent code {} and got id {}", nextParent.getCode(), nextParent.getId());
				}
			}
		}

		return retVal;
	}

	@Nonnull
	private TermCodeSystem getOrCreateDistinctTermCodeSystem(ResourcePersistentId theCodeSystemResourcePid, String theSystemUri, String theSystemName, String theSystemVersionId, ResourceTable theCodeSystemResourceTable) {
		TermCodeSystem codeSystem = myCodeSystemDao.findByCodeSystemUri(theSystemUri);
		if (codeSystem == null) {
			codeSystem = myCodeSystemDao.findByResourcePid(theCodeSystemResourcePid.getIdAsLong());
			if (codeSystem == null) {
				codeSystem = new TermCodeSystem();
			}
		} else {
			checkForCodeSystemVersionDuplicate(codeSystem, theSystemUri, theSystemVersionId, theCodeSystemResourceTable);
		}

		codeSystem.setResource(theCodeSystemResourceTable);
		codeSystem.setCodeSystemUri(theSystemUri);
		codeSystem.setName(theSystemName);
		codeSystem = myCodeSystemDao.save(codeSystem);
		return codeSystem;
	}

	private void checkForCodeSystemVersionDuplicate(TermCodeSystem theCodeSystem, String theSystemUri, String theSystemVersionId, ResourceTable theCodeSystemResourceTable) {
		TermCodeSystemVersion codeSystemVersionEntity;
		String msg = null;
		if (theSystemVersionId == null) {
			// Check if a non-versioned TermCodeSystemVersion entity already exists for this TermCodeSystem.
			codeSystemVersionEntity = myCodeSystemVersionDao.findByCodeSystemPidVersionIsNull(theCodeSystem.getPid());
			if (codeSystemVersionEntity != null) {
				msg = myContext.getLocalizer().getMessage(BaseTermReadSvcImpl.class, "cannotCreateDuplicateCodeSystemUrl", theSystemUri, codeSystemVersionEntity.getResource().getIdDt().toUnqualifiedVersionless().getValue());
			}
		} else {
			// Check if a TermCodeSystemVersion entity already exists for this TermCodeSystem and version.
			codeSystemVersionEntity = myCodeSystemVersionDao.findByCodeSystemPidAndVersion(theCodeSystem.getPid(), theSystemVersionId);
			if (codeSystemVersionEntity != null) {
				msg = myContext.getLocalizer().getMessage(BaseTermReadSvcImpl.class, "cannotCreateDuplicateCodeSystemUrlAndVersion", theSystemUri, theSystemVersionId, codeSystemVersionEntity.getResource().getIdDt().toUnqualifiedVersionless().getValue());
			}
		}
		// Throw exception if the TermCodeSystemVersion is being duplicated.
		if (codeSystemVersionEntity != null) {
			if (!ObjectUtil.equals(codeSystemVersionEntity.getResource().getId(), theCodeSystemResourceTable.getId())) {
				throw new UnprocessableEntityException(Msg.code(848) + msg);
			}
		}
	}

	private void populateCodeSystemVersionProperties(TermCodeSystemVersion theCodeSystemVersion, CodeSystem theCodeSystemResource, ResourceTable theResourceTable) {
		theCodeSystemVersion.setResource(theResourceTable);
		theCodeSystemVersion.setCodeSystemDisplayName(theCodeSystemResource.getName());
		theCodeSystemVersion.setCodeSystemVersionId(theCodeSystemResource.getVersion());
	}


	private int validateConceptForStorage(TermConcept theConcept, TermCodeSystemVersion theCodeSystemVersion, ArrayList<String> theConceptsStack,
													  IdentityHashMap<TermConcept, Object> theAllConcepts) {
		ValidateUtil.isTrueOrThrowInvalidRequest(theConcept.getCodeSystemVersion() != null, "CodeSystemVersion is null");
		ValidateUtil.isNotBlankOrThrowInvalidRequest(theConcept.getCode(), "CodeSystem contains a code with no code value");

		theConcept.setCodeSystemVersion(theCodeSystemVersion);
		if (theConceptsStack.contains(theConcept.getCode())) {
			throw new InvalidRequestException(Msg.code(849) + "CodeSystem contains circular reference around code " + theConcept.getCode());
		}
		theConceptsStack.add(theConcept.getCode());

		int retVal = 0;
		if (theAllConcepts.put(theConcept, theAllConcepts) == null) {
			if (theAllConcepts.size() % 1000 == 0) {
				ourLog.info("Have validated {} concepts", theAllConcepts.size());
			}
			retVal = 1;
		}

		for (TermConceptParentChildLink next : theConcept.getChildren()) {
			next.setCodeSystem(theCodeSystemVersion);
			retVal += validateConceptForStorage(next.getChild(), theCodeSystemVersion, theConceptsStack, theAllConcepts);
		}

		theConceptsStack.remove(theConceptsStack.size() - 1);

		return retVal;
	}


}
